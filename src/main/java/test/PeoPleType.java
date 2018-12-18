package test;

/**
 * Created by 18435 on 2018/5/8.
 */
public enum PeoPleType {

    man("男人"),
    other("其他"),
    woman("女人");

    private String desc;

    PeoPleType(String desc){
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
