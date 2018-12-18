package redisTest;

/**
 * Created by 18435 on 2018/6/26.
 */
public enum Option {
    add("添加"),
    remove("删除");

    private String desc;

    Option(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }
}
