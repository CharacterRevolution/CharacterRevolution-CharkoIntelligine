package moe.characterrevolution.charkointelligine.ui;

import moe.characterrevolution.charkointelligine.CharkoIntelligine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tray 类
 * 提供系统托盘图标与菜单服务
 * @author Bill Yang
 */
public class Tray {

    CharkoIntelligine charkointelligine;
    private TrayIcon tray = null;
    FloatingWindow fw = null;

    /**
     * 创建系统托盘与控制台悬浮窗（默认隐藏）
     * @param charkointelligine 传递主类提供资源信息
     * @see FloatingWindow
     */
    public void create(CharkoIntelligine charkointelligine) {
        this.charkointelligine = charkointelligine;

        if(GraphicsEnvironment.isHeadless()) {
            charkointelligine.getLogger().warning("无图形环境，停止图形界面加载");
            return;
        }

        fw = new FloatingWindow(charkointelligine);

        if(!SystemTray.isSupported()) {
            charkointelligine.getLogger().warning("系统不支持托盘");
            return;
        }

        InputStream is = charkointelligine.getResourceAsStream("icon.jpg");

        if(is != null) {
            try {
                PopupMenu pop = new PopupMenu();

                MenuItem menu1 = new MenuItem("打开控制台");
                menu1.addActionListener(e -> {
                    fw.setVisible(true);
                });

                pop.add(menu1);

                pop.addSeparator();

                MenuItem menu2 = new MenuItem("关于");
                menu2.addActionListener(e -> JOptionPane.showMessageDialog(
                        null, "CharkoIntelligine " + charkointelligine.getVersion() +
                                "\nhttps://github.com/BillYang2016/CharkoIntelligine" +
                                "\n初版作者 Bill Yang",
                        "关于 CharkoIntelligine", JOptionPane.INFORMATION_MESSAGE
                ));

                pop.add(menu2);

                tray = new TrayIcon(ImageIO.read(is), "CharkoIntelligine 菜单", pop);
                tray.setImageAutoSize(true);
                tray.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getButton() != MouseEvent.BUTTON1) return; //需左键点击
                        if(e.getClickCount() != 2) return; //需要双击
                        fw.setVisible(true);
                    }
                });

                SystemTray systemTray = SystemTray.getSystemTray();
                systemTray.add(tray);
            } catch (IOException | AWTException e) {
                e.printStackTrace();
            }
        } else charkointelligine.getLogger().error("未找到资源文件icon.jpg，无法生成控制台");
    }

    /**
     * 移除托盘图标
     * 未使用
     */
    public void remove() {
        if(tray != null)SystemTray.getSystemTray().remove(tray);
    }

}
