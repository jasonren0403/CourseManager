package com.ren.CourseManager.utils;

import javax.swing.*;
import java.awt.*;

public class FrameManager {
    public static final int FRAME_MANAGER_REGISTER_WINDOW = 0;
    public static final int FRAME_MANAGER_LOGIN_WINDOW = 1;
    public static final int FRAME_MANAGER_MAINFUNC_WINDOW = 2;

    public static void FrameSetup(JFrame f, int width, int height, LayoutManager manager, boolean setResizablebool) {
        f.setSize(width, height); //设置窗口的大小
        f.setLayout(manager); //设置程序默认布局格式为空，以便于后期自己简单的设置布局
        f.setResizable(setResizablebool); //设置可缩放
    }

    public static void setWindowLoc(JFrame f, int mode) {
        //给屏幕的宽度高度，程序窗口的宽度高度赋值
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
