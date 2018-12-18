package redisTest;

import java.io.Serializable;

/**
 * Created by 18435 on 2018/6/25.
 * 文章
 */
public class Article implements Serializable {
    private  String id;   //文章id
    private String title; //文章标题
    private String link;  //文章链接
    private String user; //发布用户
    private long time; //发布时间
    private int votes; //票数


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", user='" + user + '\'' +
                ", time=" + time +
                ", votes=" + votes +
                '}';
    }
}
