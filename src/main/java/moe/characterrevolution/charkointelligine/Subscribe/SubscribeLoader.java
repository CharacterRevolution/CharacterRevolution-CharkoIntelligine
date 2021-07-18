package moe.characterrevolution.charkointelligine.Subscribe;

import com.alibaba.fastjson.JSONArray;
import moe.characterrevolution.charkointelligine.CharkoIntelligine;
import moe.characterrevolution.charkointelligine.Config.UserIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Timer;

/**
 * SubscribeLoader 类
 * 用于加载订阅配置
 */
public class SubscribeLoader {

    String path;

    /**
     * 初始化 subscribe.json
     */
    void initFile() {
        File file = new File(path,"subscribe.json");
        if(!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fop = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fop,"UTF-8");

                writer.append("[\n" +
                        "]"
                );

                writer.close();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 初始化
     * @param path 配置路径
     * @param charkoIntelligine 传递主类
     */
    public void init(String path, CharkoIntelligine charkoIntelligine) {
        this.path = path;

        File file = new File(path,"subscribe.json");
        if(!file.exists()) initFile();

        load(charkoIntelligine);
    }

    /**
     * 加载 subscribe.json
     * @param charkoIntelligine 传递主类
     */
    public void load(CharkoIntelligine charkoIntelligine) {
        File file = new File(path,"subscribe.json");

        StringBuffer sb = UserIO.readFile(file);
        List<Subscribe> subscribeList;

        try {
            subscribeList = JSONArray.parseArray(sb.toString(), Subscribe.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for(Subscribe ssc: subscribeList) {
            Timer timer = new Timer();
            timer.schedule(new SubscribePerformer(ssc, charkoIntelligine), 0, ssc.getPeriod() * 60 * 1000);
        }

    }

}
