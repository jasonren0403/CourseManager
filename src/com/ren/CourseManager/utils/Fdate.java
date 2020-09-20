package com.ren.CourseManager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 输入日期 并进行验证格式是否正确
 */
public class Fdate {
    public static boolean isLeap(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static boolean validate(String dateStr) {
        String msg = "";
        String[] data = new String[3];
        boolean flag = true; // 若不符合规则将值改为false
        String year = "[0-9]{4}";// 年
        String month = "[0-9]||0[0-9]||1[12]";// 月
        String day = "[0-9]||[0-2][0-9]||3[01]";// 天
        int YEAR = 0;
        String str = dateStr;// 输入的字符串
        data = str.split("[-/.+]");
        // 最基本的检查格式 begin
        if (!data[0].matches(year)) {
            msg = "年不对";
            flag = false;
        }
        if (!data[1].matches(month)) {
            msg = "月不对";
            flag = false;
        }
        if (!data[2].matches(day)) {
            msg = "日不对";
            flag = false;
        }
        // end
        YEAR = Integer.valueOf(data[0]);
        boolean isLeap = isLeap(YEAR);// isLeap 为true是闰年否则是 非闰年
        if (isLeap) {// 闰年
            if (data[1].matches("0[2]||2")) {// 这里是闰年的2月
                if (!data[2].matches("0[1-9]||[1-9]||1[0-9]||2[0-9]")) {
                    flag = false;
                    msg = "2月份的天数不对";
                }
            }
        } else {// 非闰年
            if (data[1].matches("0[2]||2")) {// 这里是平年的2月
                if (!data[2].matches("0[1-9]||[1-9]||1[0-9]||2[0-8]")) {
                    flag = false;
                    msg = "2月份的天数不对";
                }
            }
        }

        // 下面判断除了2月份的大小月天数
        if (data[1].matches("0[13578]||[13578]||1[02]")) {// 这里是大月
            if (!data[2].matches("0[1-9]||[1-9]||[12][0-9]||3[01]")) {
                flag = false;
                msg = data[2] + " 天数不对";
            }
        } else if (data[1].matches("0[469]||[469]||11")) {// 这里是小月
            if (!data[2].matches("0[1-9]||[1-9]||[12][0-9]||30")) {
                flag = false;
                msg = data[2] + " 天数不对";
            }
        }

        if (flag) {
            msg = "日期格式正确";
        }

        return flag;
    }

    public static int convertToNum(String datestr) {
        int num = 0;
        if (validate(datestr)) {
            String[] data = datestr.split("[-/.+]");
            num = Integer.valueOf(data[0]) * 10000 + Integer.valueOf(data[1]) * 100 + Integer.valueOf(data[2]);
            return num;
        } else return num;

    }

    public static int convertToWeek(String datestr) {
        int week = 0;
        if (validate(datestr)) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance(); // 获得一个日历
            Date datet = null;
            try {
                datet = f.parse(datestr);
                cal.setTime(datet);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            week = cal.get(Calendar.DAY_OF_WEEK) - 1;// 指示一个星期中的某天。
            if (week == 0) {
                week = 7;
            }
        }
        return week;
    }
}
