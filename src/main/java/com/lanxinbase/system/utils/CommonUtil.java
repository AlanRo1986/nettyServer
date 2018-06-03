package com.lanxinbase.system.utils;


import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Map;

/**
 * Created by alan.luo on 2017/8/10.
 */
public class CommonUtil {

    public static String getOrderSn(){
        String str = "800";
        str += DateUtils.getFullDateQ(null) + NumberUtils.getRandom(1111,9999);
        str += String.valueOf(DateUtils.getTime()).substring(5) + "" + NumberUtils.getRandom(111,999);
        return str;
    }

    /**
     * 取支付编码
     * @return
     */
    public static String getPaySn(){
        String str = "100";
        str += DateUtils.getFullDateQ(null)+ NumberUtils.getRandom(1111,9999);
        str += String.valueOf(DateUtils.getTime()).substring(7) + "" + NumberUtils.getRandom(111,999);
        return str;
    }





    /**
     * 格式化金钱，返回00.00格式
     * @param money
     * @return
     */
    public static String moneyFormat(Double money){
        if (money == null){
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(money);
    }

    /**
     * 格式化数字
     * @param number
     * @param pattern 比如:00.0
     * @return
     */
    public static String formatNumber(Float number,String pattern){
        if (pattern == null){
            pattern = "#0.00";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    public static Double formatNumber(Double number,String pattern){
        if (pattern == null){
            pattern = "#0.00000";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return Double.valueOf(df.format(number));
    }

    /**
     * 用户手机号码隐藏
     * @param mobile
     * @return
     */
    public static String hideMobile(String mobile) {
        return mobile.substring(0,4) + "****" + mobile.substring(9);
    }

    public static String hideIdno(String idno) {
        return idno.substring(0,1) + "************" + idno.substring(idno.length()-1);
    }

    /**
     * 解析double
     * @param a
     * @return
     */
    public static Double parseDouble(String a) {
        if (StringUtils.isEmpty(a)){
            return 0.0;
        }

        return Double.parseDouble(a);
    }

    public static boolean testMobile(String mobile){
        String regx = "^1[3|4|5|6|7|8]\\d{1}\\d{8}$";
        return mobile.matches(regx);
    }

    public static boolean testNumber(String num){
        String regx = "^[0-9]*$";
        return num.matches(regx);
    }

    public static boolean testDate(String date){
        String regx = "^\\d{4}+\\-\\d{2}+\\-\\d{2}$";
        return date.matches(regx);
    }

    public static boolean testDateTime(String time){
        String regx = "^\\d{4}+\\-\\d{2}+\\-\\d{2} \\d{2}+\\:\\d{2}+\\:\\d{2}$";
        return time.matches(regx);
    }

    public static boolean testDouble(String test) {
        String regx = "^[-+]?[0-9]+(\\.[0-9]+)?$";
        return test.matches(regx);
    }

}
