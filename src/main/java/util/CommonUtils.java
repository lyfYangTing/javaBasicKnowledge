package util;

import com.thoughtworks.xstream.XStream;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 18435 on 2018/3/12.
 * 系统静态辅助方法
 */
public class CommonUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 返回指定长度的数字随机数，在前面补0
     *
     * @param strLength
     * @return
     */
    public static String getFixLengthString(int strLength) {
        Random rm = new Random();
        // 获得随机数   Math.pow(底数,几次方)  当strLength>=7时，pross会用科学计数法表示该值
        // 例如: 当strLength = 7 时，fixLengthString = 1.8947846622806522E10 则返回的随机数为 .894784662
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLengthString = strLength >= 7 ? (new BigDecimal(pross)).toPlainString() : String.valueOf(pross);
        logger.info(fixLengthString);

        // 返回固定的长度的随机数  该子字符串从指定的 beginIndex 处开始，直到索引 endIndex - 1 处的字符。
        return fixLengthString.substring(1, strLength + 1);
    }

    public static String getRelativeUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getHeader("host") + request.getContextPath();
    }


    /**
     * 汉字转拼音缩写
     *
     * @param str //要转换的汉字字符串
     * @return String //拼音缩写
     */
    public static String getPYString(String str) {
        String tempStr = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c >= 33 && (int) c <= 126) {// 字母和符号原样保留
                tempStr += String.valueOf(c);
            } else {// 累加拼音声母
                tempStr += getPYChar(String.valueOf(c));
            }
        }
        return tempStr;
    }

    /**
     * 取单个字符的拼音声母
     *
     * @param c //要转换的单个汉字
     * @return String 拼音声母
     * @throws java.io.UnsupportedEncodingException
     */
    private static String getPYChar(String c) {
        try {
            byte[] array = new byte[2];
            array = String.valueOf(c).getBytes("GBK");
            int i = (short) (array[0] - '\0' + 256) * 256
                    + ((short) (array[1] - '\0' + 256));
            if (i < 0xB0A1)
                return "*";
            if (i < 0xB0C5)
                return "a";
            if (i < 0xB2C1)
                return "b";
            if (i < 0xB4EE)
                return "c";
            if (i < 0xB6EA)
                return "d";
            if (i < 0xB7A2)
                return "e";
            if (i < 0xB8C1)
                return "f";
            if (i < 0xB9FE)
                return "g";
            if (i < 0xBBF7)
                return "h";
            if (i < 0xBFA6)
                return "j";
            if (i < 0xC0AC)
                return "k";
            if (i < 0xC2E8)
                return "l";
            if (i < 0xC4C3)
                return "m";
            if (i < 0xC5B6)
                return "n";
            if (i < 0xC5BE)
                return "o";
            if (i < 0xC6DA)
                return "p";
            if (i < 0xC8BB)
                return "q";
            if (i < 0xC8F6)
                return "r";
            if (i < 0xCBFA)
                return "s";
            if (i < 0xCDDA)
                return "t";
            if (i < 0xCEF4)
                return "w";
            if (i < 0xD1B9)
                return "x";
            if (i < 0xD4D1)
                return "y";
            if (i < 0xD7FA)
                return "z";
            return "*";
        } catch (Exception e) {
            return "*";
        }
    }

    /**
     * 数组转字符串
     *
     * @param array
     * @param split
     * @return
     */
    public static String arrayToString(Object[] array, String split) {
        String s = "";
        for (int i = 0; i < array.length; i++) {
            s += array[i];
            if (i + 1 < array.length)
                s += split;
        }
        return s;
    }

    /**
     * 数组转字符串
     *
     * @param array
     * @param split
     * @return
     */
    public static String arrayToQuoteString(Object[] array, String split) {
        String s = "";
        for (int i = 0; i < array.length; i++) {
            s += "'" + array[i] + "'";
            if (i + 1 < array.length)
                s += split;
        }
        return s;
    }


    public static Boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        else if (obj.getClass().isArray()) {
            if (((Object[]) obj).length <= 0)
                return true;
        } else if (obj.toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    public static Map<String, String> listToMap(List<?> objs, String key,
                                                String value) throws Exception {// "id","pe.name"
        Map<String, String> resultMap = new HashMap<String, String>();
        String[] keyString = key.split("\\.");
        String[] valueString = value.split("\\.");
        Object keyTemp, valueTemp;
        for (int i = 0; i < keyString.length; i++) {
            String first = keyString[i].charAt(0) + "";
            keyString[i] = "get"
                    + keyString[i].replaceFirst(first, first.toUpperCase());
        }
        for (int i = 0; i < valueString.length; i++) {
            String first = valueString[i].charAt(0) + "";
            valueString[i] = "get"
                    + valueString[i].replaceFirst(first, first.toUpperCase());
        }
        for (Object obj : objs) {
            keyTemp = obj;
            for (String k : keyString) {
                keyTemp = keyTemp.getClass().getMethod(k).invoke(keyTemp);
            }
            valueTemp = obj;
            for (String v : valueString) {
                valueTemp = valueTemp.getClass().getMethod(v).invoke(valueTemp);
            }
            resultMap.put(keyTemp.toString(), valueTemp.toString());
        }
        return resultMap;
    }





    /**
     * 字符串格式化，取消空格和换行
     *
     * @param param
     * @return
     */
    public static String DelFormat(String param) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher ms = p.matcher(param);
        param = ms.replaceAll("");
        String temps = "";
        // 把可能存在的换行剔除
        StringReader srs = new StringReader(param);
        BufferedReader brs = new BufferedReader(srs);
        String lines = null;
        try {
            while ((lines = brs.readLine()) != null) {
                temps += lines;
            }
        } catch (IOException e1) {
            logger.info("字符串格式化,取消空格和换行>>>",e1.getMessage(),e1);
            //  e1.printStackTrace();
        }
        temps = temps.replace("\r\n", "\\r\\n");
        //去除换行和空格
        return temps;
    }



    /**
     * 转换xml
     *
     * @param params
     * @return
     */
    public static String toXml(SortedMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (String name : params.keySet()) {
            sb.append("<" + name + ">");
            sb.append(params.get(name));
            sb.append("</" + name + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 解析xml数据
     *
     * @param xmlContent
     * @return
     * @throws Exception
     */
    public static SortedMap<String, String> parseDataXml(String xmlContent) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
            // 得到xml根元素
            Element root = document.getDocumentElement();
            // 得到根元素的所有子节点
            NodeList nodes = root.getChildNodes();

            SortedMap<String, String> data = new TreeMap<>();
            // 遍历所有子节点
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node book = nodes.item(i);
                    if (book.getFirstChild() != null) {
                        data.put(book.getNodeName(), book.getFirstChild().getNodeValue());
                    }
                }
            }
            return data;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }


    /**
     * 通过反射的方式遍历对象的属性和属性值，方便调试
     *
     * @param o 要遍历的对象
     * @throws Exception
     */
    public static void reflect(Object o) throws Exception {
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            log(f.getName() + " -> " + f.get(o));
        }
    }

    public static byte[] readInput(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }


    public static InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
        }
        return tInputStringStream;
    }

    public static Object getObjectFromXML(String xml, Class tClass) {
        //将从API返回的XML数据映射到Java对象
        XStream xStreamForResponseData = new XStream();
        xStreamForResponseData.alias("xml", tClass);
        xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
        return xStreamForResponseData.fromXML(xml);
    }

    public static String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
        if (key == "" || key == null) {
            return defaultValue;
        }
        String result = (String) map.get(key);
        if (result == null) {
            return defaultValue;
        } else {
            return result;
        }
    }

    public static int getIntFromMap(Map<String, Object> map, String key) {
        if (key == "" || key == null) {
            return 0;
        }
        if (map.get(key) == null) {
            return 0;
        }
        return Integer.parseInt((String) map.get(key));
    }

    /**
     * 打log接口
     *
     * @param log 要打印的log字符串
     * @return 返回log
     */
    public static String log(Object log) {
        logger.info(log.toString());
        return log.toString();
    }

    /**
     * 读取本地的xml数据，一般用来自测用
     *
     * @param localPath 本地xml文件路径
     * @return 读到的xml字符串
     */
    public static String getLocalXMLString(String localPath) throws IOException {
        return inputStreamToString(CommonUtils.class.getResourceAsStream(localPath));
    }

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 解析xml
     *
     * @param xmlDoc
     * @return
     * @throws IOException
     */
    public static Map<String, String> getMapFromXML(String xmlDoc) {
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        Map<String, String> map = new HashMap<>();
        try {
            //通过输入源构造一个Document
            org.jdom.Document doc = sb.build(source);
            //取的根元素
            org.jdom.Element root = doc.getRootElement();
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            org.jdom.Element etTag = null;
            org.jdom.Element etName = null;
            for (int i = 0; i < jiedian.size(); i++) {
                etTag = (org.jdom.Element) jiedian.get(i);//循环依次得到子元素
                etName = (org.jdom.Element) jiedian.get(i);//获取子元素
                List zjiedian = etName.getChildren();
                if (zjiedian.size() > 0) {
                    for (int j = 0; j < zjiedian.size(); j++) {
                        org.jdom.Element xet = (org.jdom.Element) zjiedian.get(j);
                        map.put(xet.getName(), etTag.getChild(xet.getName()).getText());
                    }
                } else {
                    map.put(etTag.getName(), etTag.getText());
                }
            }
            return map;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    public static void main(String[] args){
        //String randString = CommonUtils.getFixLengthString(19);
//        String psw = CommonUtils.getUserPasswordWithMD5("123456");
//        System.out.println("加密后密码:"+psw);
    }


}
