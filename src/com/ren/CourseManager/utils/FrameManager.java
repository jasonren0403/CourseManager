package com.ren.CourseManager.utils;

import javax.swing.*;
import java.awt.*;

public class FrameManager {
    public static final int FRAME_MANAGER_REGISTER_WINDOW = 0;
    public static final int FRAME_MANAGER_LOGIN_WINDOW = 1;
    public static final int FRAME_MANAGER_MAINFUNC_WINDOW = 2;

    public static void FrameSetup(JFrame f, int width, int height, LayoutManager manager, boolean setResizablebool) {
        f.setSize(width, height); //���ô��ڵĴ�С
        f.setLayout(manager); //���ó���Ĭ�ϲ��ָ�ʽΪ�գ��Ա��ں����Լ��򵥵����ò���
        f.setResizable(setResizablebool); //���ÿ�����
    }

    public static void setWindowLoc(JFrame f, int mode) {
        //����Ļ�Ŀ�ȸ߶ȣ����򴰿ڵĿ�ȸ߶ȸ�ֵ
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int screenSizeWidth = (int) dimension.getWidth();
        int screenSizeHeight = (int) dimension.getHeight();
        int windowWidth = f.getWidth();
        int windowHeight = f.getHeight();
        switch (mode) {
            case FRAME_MANAGER_REGISTER_WINDOW:
            case FRAME_MANAGER_MAINFUNC_WINDOW: {
                f.setLocation(screenSizeWidth / 3 - windowWidth / 3,
                        screenSizeHeight / 3 - windowHeight / 3);
                break;
            }
            case FRAME_MANAGER_LOGIN_WINDOW: {
                f.setLocation(screenSizeWidth / 2 - windowWidth / 2,
                        screenSizeHeight / 2 - windowHeight / 2);
                break;
            }
        }
    }

    public static void clearField(JTextField... cps) {
        for (JTextField jtf : cps) {
            jtf.setText("");
        }
    }

    public static void addComponents(JFrame f, JComponent... cps) {
        for (JComponent c : cps) {
            f.add(c);
        }
    }

    public static void addComponents(JPanel p, JComponent... cps) {
        for (JComponent c : cps) {
            p.add(c);
        }
    }

    public static void addComponents(Box b, JComponent... cps) {
        for (JComponent c : cps) {
            b.add(c);
        }
    }

    public static boolean check_field_is_filled(JTextField... tfs) {
        for (JTextField jtf : tfs) {
            String temp = jtf.getText();
            if (temp == null || temp.isEmpty()) return false;
        }
        return true;
    }
}
