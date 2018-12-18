package czbk.io;

import java.io.*;
import java.util.Arrays;

/**
 * Created by 18435 on 2018/11/30.
 *
 * 转换流：InputStreamReader  OutputStreamWriter
 *
 * 编码：字符串变成字节数组  String --> byte[]  str.getBytes(charsetName)
 * 解码：字符数组变成字符串  byte[] ---> new String(byte[],charsetName)
 */
public class EncodeStream {
    public static void main(String[] args) {
        //writeText();
        //readText();
        //codeDemo();
        System.out.println("@Column(name = \"order_date\")\n".toUpperCase() +
                "    private String  orderDate;          //订单日期\n" +
                "    @Column(name = \"order_time\")\n".toUpperCase() +
                "    private String  orderTime;          //订单时间\n" +
                "    @Column(name = \"merchant_id\")\n".toUpperCase() +
                "    private String  merchantId;         //商户编号\n" +
                "    @Column(name = \"trade_no\")\n".toUpperCase() +
                "    private String  tradeNo;            //支付交易流水号\n" +
                "    @Column(name = \"refund_trade_no\")\n".toUpperCase() +
                "    private String  refundTradeNo;      //退款请求流水号\n" +
                "    @Column(name = \"bill_date\")\n".toUpperCase() +
                "    private String  billDate;           //系统记账日期\n" +
                "    @Column(name = \"order_status\")\n".toUpperCase() +
                "    private String  orderStatus;        // 支付成功/退款成功\n" +
                "    @Column(name = \"order_amount\")\n".toUpperCase() +
                "    private BigDecimal orderAmount;          //订单金额\n" +
                "    @Column(name = \"order_type\")\n".toUpperCase() +
                "    private String orderType;           //02-支付交易 20-退款交易\n" +
                "    @Column(name = \"remarks\")\n".toUpperCase() +
                "    private String remarks;             //备注\n" +
                "    @Column(name = \"district_code\")\n".toUpperCase() +
                "    private String districtCode;        //账单归属区".toUpperCase());
    }

    public static void writeText(){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("utf.txt"),"GBK");
            osw.write("你好");
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readText(){
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("utf.txt"),"GBK");
            char[] data = new char[10];
            int length = isr.read(data);
            System.out.println(new String(data,0,length));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void codeDemo(){

        byte[] datas = new byte[10];//编码
        try {
            datas = "你好".getBytes();//默认编码  "UTF-8"
            System.out.println(Arrays.toString(datas));

            String s1 = new String(datas,"ISO-8859-1");//解码错误
            System.out.println("s1 : "+s1);
            byte[] datass = s1.getBytes("ISO-8859-1");//重新编码
            System.out.println("ISO重新编码 :"+ Arrays.toString(datass));
            String s2 = new String(datas);//按照正确的字符集解码
            System.out.println("s2 :" + s2);//可以正常还原  因为ISO-8859-1不能表示中文

            String s3 = new String(datas,"GBK");//解码错误
            System.out.println("s3 : "+s3);
            byte[] data2 = s3.getBytes("GBK");//重新编码
            System.out.println("GBK重新编码 :" + Arrays.toString(data2));
            String s4 = new String(data2);//按照正确的字符集解码
            System.out.println("s4 :" + s4);

            byte[] data3 = " 鍝堝搱".getBytes("GBK");
            System.out.println(Arrays.toString(data3));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
