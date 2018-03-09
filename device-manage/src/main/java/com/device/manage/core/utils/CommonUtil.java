package com.device.manage.core.utils;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  on 7/14/16.
 */
public class CommonUtil {
    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);

    static final Gson gson = new Gson();

    /**
     * 手机号码验证
     *
     * @param content
     * @return
     */
    public static boolean isMobileNum(String content) {
        String str = "^[1][3,5,7,8][0-9]{9}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(content);
        return m.matches();
    }

    /**
     * 身份证号码验证
     *
     * @param content
     * @return
     */
    public static boolean isIdCardNum(String content) {
        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
        Pattern p = Pattern.compile(rgx);
        Matcher m = p.matcher(content);
        return m.matches();
    }


    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String reg = "[\\w]+@[\\w]+.[\\w]+";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 对汉字的长度进行验证等yes 的时候满足输入的要求
     *
     * @param kanji 汉字的类容
     * @param count 需要验证的长度
     * @return
     */
    public static String handleLeng(String kanji, int count) {
        String regEx = "[\u4e00-\u9fa5]";
        int num = 0;//汉字长度
        for (int i = 0; i < kanji.length(); i++) {
            if (Pattern.compile(regEx).matches(regEx, kanji.substring(i, i + 1))) {
                num++;
            }
        }
        if (num > count) {
            return "king-size";
        }
        if (num < 0) {
            return "not-null";
        }
        return "yes";
    }

    /**
     * 性别的验证
     */
    public static boolean handleSex(String Sex) {
        String reg = "^[\\u7537\\u5973]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(Sex);
        return m.matches();
    }

    /**
     * 真实姓名的验证
     */
    public static boolean realName(String realName) {
        String reg = "^[\\w\\u4E00-\\uFA29]{2,6}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(realName);
        return m.matches();
    }

    /**
     * 实体类转成MAP
     */
    public static Map<String, Object> ConvertObjToMap(Object obj) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (obj == null) {
            return null;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    if (o != null && !"".equals(o)) {
                        reMap.put(fields[i].getName(), o);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    /**
     * @param str
     * @return true 包含中文字符
     */
    public static boolean isContainsChinese(String str) {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * double 保留两位小数
     *
     * @param number
     * @return
     */
    public static String doubleFormat(double number) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(number);
    }


    /**
     * 在指定路径下生成当天文件夹
     *
     * @return 返回文件夹名称
     */
    public static String createFile(String pjoPath) {
        String nowFileName = DateUtil.nowTimeDate("yyyyMMdd");//文件夹名称
        File file = new File(pjoPath + "/" + nowFileName);
        if (!file.exists()) {
            file.mkdir();
        }
        return nowFileName;
    }

    /**
     * 判断单词是否是大写
     *
     * @param str
     * @return
     */
    public static boolean isUpperCase(String str) {
        boolean isInSmallCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                isInSmallCase = true;
                break;
            }
        }
        return isInSmallCase;
    }

    /**
     * 判断一组以逗号分割的字符串中是否存在某个元素
     *
     * @param strS
     * @param ele
     * @return
     */
    public static boolean isContainsEleByStr(String strS, String ele) {
        String[] values = strS.split(",");
        List<String> list = Arrays.asList(values);
        if (list.contains(ele)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除一组以逗号分割的字符串中某个元素
     *
     * @param str       以逗号分割的字符串
     * @param removeEle 要移除的字符串
     * @return
     */
    public static String removeEleByStr(String str, String removeEle) {
        String result;
        if (str == null || "".equals(str)) {
            return "";
        }
        List list = new ArrayList<>();
        String[] array = str.split(",");
        for (String a : array) {
            if (!removeEle.equals(a)) {
                list.add(a);
            }
        }
        result = StringUtils.join(list.toArray(), ",");
        return result;
    }

    /**
     * 去除以逗号分割的字符串中重复的元素
     *
     * @param str 以逗号分割的字符串
     * @return
     */
    public static String removeRepeatEleByStr(String str) {
        String result;
        if (str == null || "".equals(str)) {
            return "";
        }
        List list = new ArrayList<>();
        String[] array = str.split(",");
        for (String a : array) {
            if (!list.contains(a)) {
                list.add(a);
            }
        }
        result = StringUtils.join(list.toArray(), ",");
        return result;
    }

    /**
     * 数字转中文
     *
     * @param number
     * @return
     */
    public static String numToHanStr(Integer number) {
        String result = "";
        String[] hanArr = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unitArr = {"十", "百", "千", "万", "十", "白", "千", "亿", "十", "百", "千"};
        String numStr = number + "";
        int numLen = numStr.length();
        for (int i = 0; i < numLen; i++) {
            int num = numStr.charAt(i) - 48;
            if (i != numLen - 1 && num != 0) {
                result += hanArr[num] + unitArr[numLen - 2 - i];
                if (number >= 10 && number < 20) {
                    result = result.substring(1);
                }
            } else {
                if (!(number >= 10 && number % 10 == 0)) {
                    result += hanArr[num];
                }
            }
        }
        return result;
    }

    /**
     * 求2个集合的补集
     *
     * @param U
     * @param S
     * @return
     */
    public static List buji(List U, List S) {
        U.removeAll(S);
        return U;
    }

    /**
     * List集合移除指定元素[equals]
     *
     * @param list
     * @param obj
     */
    public void iteratorRemove(List<T> list, T obj) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T item = it.next();
            if (item.equals(obj)) {
                it.remove();
            }
        }
    }

    /**
     * Map集合移除包含元素[contains]
     *
     * @param map
     * @param ele
     */
    public static void iteratorRemove(Map<String, ?> map, String ele) {
        Iterator<? extends Map.Entry<String, ?>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ?> entry = it.next();
            if (entry.getKey().startsWith(ele)) {
                it.remove();
            }
        }
    }

    /**
     * 判断一个字符串是否都为数字
     *
     * @param strNum
     * @return
     */
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    /**
     * 截取数字
     *
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 截取非数字
     *
     * @param content
     * @return
     */
    public static String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 提取一个字符串中的整数和小数部分
     */
    public static String getNumbers2(String str) {
        // 需要取整数和小数的字符串
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }

    public static void main(String[] args) {
        //System.out.println(removeEleByStr("160,95,94,23,58", "160"));
        //System.out.println(isContainsEleByStr("1", "1"));
        //System.out.println(numToHanStr(16));

        //Map map = new HashMap<>(16);
        //map.put("aa_1", true);
        //map.put("aa_2", true);
        //map.put("bb_3", true);
        //map.put("bb_4", true);
        //iteratorRemove(map, "aa_");
        //System.out.println(gson.toJson(map));

        System.out.println(getNumbers2("1.8%&nbsp;↑"));
    }
}
