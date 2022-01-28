package com.ren.CourseManager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * �������� ��������֤��ʽ�Ƿ���ȷ
 */
public class Fdate {
    public static boolean isLeap(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static boolean validate(String dateStr) {
        String msg = "";
        String[] data = new String[3];
        boolean flag = true; // �������Ϲ���ֵ��Ϊfalse
        String year = "[0-9]{4}";// ��
        String month = "[0-9]||0[0-9]||1[12]";// ��
        String day = "[0-9]||[0-2][0-9]||3[01]";// ��
        int YEAR = 0;
        String str = dateStr;// ������ַ���
        data = str.split("[-/.+]");
        // ������ļ���ʽ begin
        if (!data[0].matches(year)) {
            msg = "�겻��";
            flag = false;
        }
        if (!data[1].matches(month)) {
            msg = "�²���";
            flag = false;
        }
        if (!data[2].matches(day)) {
            msg = "�ղ���";
            flag = false;
        }
        // end
        YEAR = Integer.valueOf(data[0]);
        boolean isLeap = isLeap(YEAR);// isLeap Ϊtrue����������� ������
        if (isLeap) {// ����
            if (data[1].matches("0[2]||2")) {// �����������2��
                if (!data[2].matches("0[1-9]||[1-9]||1[0-9]||2[0-9]")) {
                    flag = false;
                    msg = "2�·ݵ���������";
                }
            }
        } else {// ������
            if (data[1].matches("0[2]||2")) {// ������ƽ���2��
                if (!data[2].matches("0[1-9]||[1-9]||1[0-9]||2[0-8]")) {
                    flag = false;
                    msg = "2�·ݵ���������";
                }
            }
        }

        // �����жϳ���2�·ݵĴ�С������
        if (data[1].matches("0[13578]||[13578]||1[02]")) {// �����Ǵ���
            if (!data[2].matches("0[1-9]||[1-9]||[12][0-9]||3[01]")) {
                flag = false;
                msg = data[2] + " ��������";
            }
        } else if (data[1].matches("0[469]||[469]||11")) {// ������С��
            if (!data[2].matches("0[1-9]||[1-9]||[12][0-9]||30")) {
                flag = false;
                msg = data[2] + " ��������";
            }
        }

        if (flag) {
            msg = "���ڸ�ʽ��ȷ";
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
            Calendar cal = Calendar.getInstance(); // ���һ������
            Date datet = null;
            try {
                datet = f.parse(datestr);
                cal.setTime(datet);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            week = cal.get(Calendar.DAY_OF_WEEK) - 1;// ָʾһ�������е�ĳ�졣
            if (week == 0) {
                week = 7;
            }
        }
        return week;
    }
}
