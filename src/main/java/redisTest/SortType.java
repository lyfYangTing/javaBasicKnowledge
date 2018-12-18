package redisTest;

/**
 * Created by 18435 on 2018/6/26.
 */
public enum  SortType {
    by_release_time("发布时间排序"),
    by_vote_scores("评分排序");

    private String desc;

    SortType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }
}
