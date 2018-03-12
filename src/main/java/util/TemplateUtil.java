package util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板工具�?
 *
 * @author wei.liu1@newtouch.cn
 * @version 1.0
 * @date 2014-4-29
 */
public class TemplateUtil {

    /**
     * 组合数据
     *
     * @param paramsMap   参数Map
     * @param templateStr 内容模板
     * @return 数据组合内容
     * @author wei.liu1@newtouch.cn
     */
    public static String SingleTemplate(Map paramsMap, String templateStr) {

        String isDecNumberRegEx = "-?([1-9]+[0-9]*|0)\\.[\\d]+?";
        //		当前时间start
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH: mm: ss");
        String _CREAT_TIME;
        if (!paramsMap.containsKey("_CREAT_TIME")) {
            _CREAT_TIME = format.format(new Date());
            paramsMap.put("_CREAT_TIME", _CREAT_TIME);
        } else {
            _CREAT_TIME = String.valueOf(paramsMap.get("_CREAT_TIME"));
            paramsMap.put("_CREAT_TIME", _CREAT_TIME);
        }
        //		当前时间end
        Matcher m = Pattern.compile("[{]([^{]*?)[}]").matcher(templateStr);
        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            try {
                String paramValue = paramsMap.get(m.group(1)) == null ? ""
                        : paramsMap.get(m.group(1)).toString();

                boolean isDecNumber = Pattern.compile(isDecNumberRegEx).matcher(paramValue).matches();

                if (isDecNumber) {

                    BigDecimal newNum = new BigDecimal(paramValue);
                    BigDecimal setScale = newNum.setScale(2, newNum.ROUND_DOWN);
                    paramValue = setScale.toString();
                }

                m.appendReplacement(buf, paramValue);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        m.appendTail(buf);
        templateStr = buf.toString();

        return templateStr;
    }


    /**
     * 带屏蔽规则模板数据组�?
     *
     * @param paramsMap
     * @param templateStr
     * @param regMap      屏蔽规则 key 屏蔽字段  value 规则
     *                    规则示例:
     *                    HashMap reg=new HashMap();
     *                    reg.put("NAME","1:*"); 将NAME保留为前 1 位内容，后面全部替换�? *
     *                    reg.put("TEL","-4:*"); 将TEL保留为后 4 位内容，前面全部替换�? *
     * @return
     */
    public static String SingleTemplateWithReg(Map paramsMap, String templateStr, Map<String, String> regMap) {

        String isDecNumberRegEx = "-?([1-9]+[0-9]*|0)\\.[\\d]+?";
        //		当前时间start
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH: mm: ss");
        String _CREAT_TIME;
        if (!paramsMap.containsKey("_CREAT_TIME")) {
            _CREAT_TIME = format.format(new Date());
            paramsMap.put("_CREAT_TIME", _CREAT_TIME);
        } else {
            _CREAT_TIME = String.valueOf(paramsMap.get("_CREAT_TIME"));
            paramsMap.put("_CREAT_TIME", _CREAT_TIME);
        }

        paramsMap.put("_CREAT_TIME", _CREAT_TIME);
        //		当前时间end
        Matcher m = Pattern.compile("[{]([^{]*?)[}]").matcher(templateStr);
        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            try {
                String paramValue = paramsMap.get(m.group(1)) == null ? ""
                        : paramsMap.get(m.group(1)).toString();

                //脱敏---begin----
                if (regMap != null) {
                    //获取规则
                    String reg = regMap.get(String.valueOf(m.group(1)));
                    //是否配置规则
                    if (reg != null) {
                        char[] paramValueArray = paramValue.toCharArray();
                        //校验规则格式是否正确
                        String[] regArray = reg.split(":");
                        //位置
                        int local = Integer.valueOf(regArray[0]);
                        //替换字符
                        String repChar = regArray[1];
                        //必须满足替换条件
                        if (paramValue.length() > Math.abs(local)) {

                            if (local > 0) {

                                for (int i = local; i < paramValueArray.length; i++) {
                                    paramValueArray[i] = (char) repChar.toCharArray()[0];
                                }

                            } else {
                                for (int i = 0; i < (paramValueArray.length + local); i++) {
                                    paramValueArray[i] = (char) repChar.toCharArray()[0];
                                }
                            }

                        }


                        paramValue = String.valueOf(paramValueArray);
                    }
                }

                //--------end----------

                boolean isDecNumber = Pattern.compile(isDecNumberRegEx).matcher(paramValue).matches();

                if (isDecNumber) {

                    BigDecimal newNum = new BigDecimal(paramValue);
                    BigDecimal setScale = newNum.setScale(2, newNum.ROUND_DOWN);
                    paramValue = setScale.toString();
                }

                m.appendReplacement(buf, paramValue);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        m.appendTail(buf);
        templateStr = buf.toString();

        return templateStr;
    }

}
