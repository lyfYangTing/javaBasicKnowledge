package redisTest;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.JedisPoolUntil;

import java.util.*;

/**
 * Created by 18435 on 2018/6/25.
 * 利用redis  实现文章发布网站的   文章发布满一周不能再投票
 * 发布功能，投支持票功能，投反对票功能，获取文章内容功能，对文章进行分组功能，
 */
public class ArticleReleaseWebsite {
    private final static int voteScore = 432;
    private final static int oneWeekInSeconds = 7 * 24 * 3600;
    private final static String VOTED_USER = "votedUser";

    /**
     * 发布文章
     * @param article
     */
    public void releaseArticle(Article article){
        article.setTime(System.currentTimeMillis());
        article.setVotes(0);
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Transaction transaction  = jedis.multi(); //开启事务
        //将发布的文章放到   redis  文章的hash结构中
        Map<String,String> paramMap = new HashedMap();
        Map<String,Object> map = JSONObject.parseObject(JSONObject.toJSONString(article));
        map.forEach((k,v) -> paramMap.put(k,String.valueOf(v)));
        transaction.hmset(article.getId(), paramMap);

        //将文章加入到  以发布时间排序的Zset集合中
        transaction.zadd("articleForReleaseTime",article.getTime()/1000,article.getId());
        //将文章加入到  以评分排序的Zset集合中  初始化评分：发布时间
        transaction.zadd("articleForScores",article.getTime()/1000,article.getId());
        transaction.exec();
        JedisPoolUntil.release(JedisPoolUntil.getJedisPoolInstance(),jedis);
    }

    /**
     * 获取文章内容
     * @param articleId
     * @return
     */
    public Article getArticle(String articleId){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Map map = jedis.hgetAll(articleId);
//        map.forEach((k,v) -> System.out.println( k + " : "+ v));
        String jsonString = JSONObject.toJSONString(map);
        Article article = JSONObject.parseObject(jsonString,Article.class);
        return article;
    }

    /**
     * 批量获取文章
     * @return
     */
    public List<Article> getArticles(int pageNum,int onePage,SortType sortType){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        List<Article> articles = new LinkedList<>();
        Set<String> articleIds = new HashSet<>();
        int startIndex = pageNum > 1 ? (pageNum-1)*onePage : 0;
        int endIndex = pageNum * onePage - 1;
        if(sortType == SortType.by_release_time){
            articleIds = jedis.zrange("articleForReleaseTime",startIndex,endIndex);
        }else {
            articleIds = jedis.zrange("articleForScores",startIndex,endIndex);
        }
        articleIds.forEach(v -> articles.add(JSONObject.parseObject(JSONObject.toJSONString(jedis.hgetAll(v)),Article.class)));
        return articles;
    }

    /**
     * 投票
     * @param user   投票用户
     * @param articleId   文章id
     * @param votes  投支持票/反对票
     */
    public void upOrDownVotes(User user,String articleId,Votes votes){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Article article = this.getArticle(articleId);
        long voteTime = System.currentTimeMillis();
        boolean isExpired = voteTime/1000 - article.getTime() > ArticleReleaseWebsite.oneWeekInSeconds ? false : true;

        if(!isExpired){
            System.out.println("发布一个星期内可被投票，投票时间已过");
            JedisPoolUntil.release(JedisPoolUntil.getJedisPoolInstance(),jedis);
            return ;
        }
        //将投用户放入  验证该用户是否已投过票
        String key = ArticleReleaseWebsite.VOTED_USER + articleId;
        Boolean isVoted = jedis.sismember(key,user.getUserId());
        if(isVoted){
            System.out.println("已经投过票了");
            JedisPoolUntil.release(JedisPoolUntil.getJedisPoolInstance(),jedis);
            return ;
        }

        jedis.watch(articleId);
        Transaction transaction = jedis.multi();
        //将投用户放入  已投票用户集合（1个人只能对1篇文章投一次票）
        transaction.sadd(key,user.getUserId());
        //修改文章评分  和 以分数排序
        if(votes == Votes.upVotes){
            transaction.hincrBy(articleId,"votes",1);
            transaction.zincrby("articleForScores",ArticleReleaseWebsite.voteScore,articleId);
        }else{
            transaction.hincrBy(articleId,"votes",-1);
            transaction.zincrby("articleForScores",-ArticleReleaseWebsite.voteScore,articleId);
        }

        transaction.exec();
        jedis.unwatch();
        JedisPoolUntil.release(JedisPoolUntil.getJedisPoolInstance(),jedis);
    }

    /**
     * 将文章添加或移除出群组
     * @param group
     * @param articleId
     * @param option
     */
    public void addOrRemoveArticle(String group,String articleId,Option option){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        if(option == Option.remove){
            boolean isExist = jedis.sismember(group,articleId);
            if (!isExist){
                System.out.println("本篇文章不在该群组中，请确认后操作");
                return;
            }
        }
        Transaction transaction = jedis.multi();
        if(option == Option.add){
            transaction.sadd(group,articleId);
        }else {
            transaction.srem(group,articleId);
        }
        transaction.exec();
    }

    /**
     * 获取分组内的所有文章
     * @param group
     * @param sortType
     * @param pageNum
     * @param onePage
     * @return
     */
    public List<Article> getArticlesByGroup(String group,SortType sortType,int pageNum,int onePage){

        List<Article> groupArticles = new LinkedList<>();
        Set<String> articles = new HashSet<>();
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        String temkey = "temporary"+ group ;
        boolean isExist = jedis.exists(temkey);
        if(!isExist){
            if(sortType == SortType.by_release_time){
                jedis.zinterstore(temkey,group,"articleForReleaseTime");
            }else{
                jedis.zinterstore(temkey,group,"articleForScores");
            }
            jedis.pexpire(temkey,60);
        }
        int startIndex = pageNum > 1 ? (pageNum-1)*onePage : 0;
        int endIndex = pageNum * onePage - 1;
        articles = jedis.zrange(temkey,startIndex,endIndex);
        articles.forEach(v -> groupArticles.add(JSONObject.parseObject(JSONObject.toJSONString(jedis.hgetAll(v)),Article.class)));
        return groupArticles;
    }


    public static void main(String[] args) {
        ArticleReleaseWebsite articleReleaseWebsite = new ArticleReleaseWebsite();
//        Article article = new Article();
//        article.setId("92611");
//        article.setTitle("Go to statement considered harmful");
//        article.setLink("http://goo.gl/kZUSu");
//        User author = new User();
//        author.setUserId("00009");
//        author.setUserName("开司");
//        article.setUser(author.getUserId());
//        articleReleaseWebsite.releaseArticle(article);

//        Article article = articleReleaseWebsite.getArticle("92610");
//        System.out.println(article);
//
//        User user = new User();
//        user.setUserId("00005");
//
//        articleReleaseWebsite.upOrDownVotes(user,article.getId(),Votes.upVotes);
//
//        Article article1 = articleReleaseWebsite.getArticle("92610");
//        System.out.println(article1);
//        List<Article> list = articleReleaseWebsite.getArticles(1,5,SortType.by_vote_scores);
//        list.forEach( article -> System.out.println(article));
        articleReleaseWebsite.addOrRemoveArticle("allArticle","92610",Option.add);
        articleReleaseWebsite.addOrRemoveArticle("allArticle","92611",Option.add);
        articleReleaseWebsite.addOrRemoveArticle("allArticle","92617",Option.add);

        articleReleaseWebsite.addOrRemoveArticle("oneGroup","92617", Option.remove);
        articleReleaseWebsite.addOrRemoveArticle("oneGroup","92611",Option.add);
        articleReleaseWebsite.addOrRemoveArticle("oneGroup","92610",Option.add);

        articleReleaseWebsite.addOrRemoveArticle("twoGroup","92617",Option.add);

        List<Article> list = articleReleaseWebsite.getArticlesByGroup("oneGroup",SortType.by_release_time,1,5);
        list.forEach(v -> System.out.println(v));

    }
}
