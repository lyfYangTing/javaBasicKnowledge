package redisTest;

/**
 * Created by 18435 on 2018/6/25.
 */
public enum Votes {

    upVotes("支持票"),
    downVotes("反对票");

    private String desc;

    Votes(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }
}
