package moe.characterrevolution.charkointelligine.Subscribe;

import moe.characterrevolution.charkointelligine.CharkoIntelligine;
import moe.characterrevolution.charkointelligine.Config.UserIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

/**
 * SubscribePerformer 类
 * 继承自 TimerTask
 * 用于实现定时订阅功能
 */
public class SubscribePerformer extends TimerTask {
    Subscribe subscribe;
    CharkoIntelligine charkoIntelligine;

    /**
     * 构造函数
     * @param subscribe 订阅项
     * @param charkoIntelligine 传递主类
     */
    public SubscribePerformer(Subscribe subscribe, CharkoIntelligine charkoIntelligine) {
        this.subscribe = subscribe;
        this.charkoIntelligine = charkoIntelligine;
    }

    /**
     * 定时任务
     * 从url获取订阅内容，更新至目标数据库
     * @see moe.characterrevolution.charkointelligine.EntryPackage.PackageLoader#leadIn(long, File, StringBuilder)
     */
    @Override
    public void run() {
        charkoIntelligine.getLogger().info("现在开始更新订阅：" + subscribe.getUrl());

        try {
            URL url = new URL(subscribe.getUrl());
            URLConnection urlConn = url.openConnection();
            urlConn.connect();

            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            int HttpResult = httpConn.getResponseCode(); //试图连接并取得状态码
            if(HttpResult != HttpURLConnection.HTTP_OK) {
                charkoIntelligine.getLogger().warning(subscribe.getUrl() + " 连接失败！");
                return;
            }

            InputStreamReader isReader = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(isReader);
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine(); //每行读取的内容

            while(line != null) {
                builder.append(line);
                builder.append(" ");
                line = reader.readLine();
            }

            reader.close();
            isReader.close();
            httpConn.disconnect(); //关闭连接

            File file = new File(charkoIntelligine.getDataFolder().getAbsolutePath(), "subscribing.json");
            UserIO.writeFile(file, builder.toString()); //写入临时文件

            StringBuilder ErrorInfo = new StringBuilder();

            if(!charkoIntelligine.pl.leadIn(subscribe.getGroupId(), file, ErrorInfo)) { //调用导入函数
                charkoIntelligine.getLogger().error("更新订阅" + subscribe.getUrl() + "至群" + subscribe.getGroupId() + "出错了！\n" + ErrorInfo.toString());
            } else {
                charkoIntelligine.getLogger().info("更新订阅完成：" + subscribe.getUrl());
            }

            System.gc(); //需要垃圾回收，否则无法删除文件
            file.getAbsoluteFile().delete();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
