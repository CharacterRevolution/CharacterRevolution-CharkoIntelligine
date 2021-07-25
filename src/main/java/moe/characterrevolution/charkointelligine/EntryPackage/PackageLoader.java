package moe.characterrevolution.charkointelligine.EntryPackage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import moe.characterrevolution.charkointelligine.Config.UserIO;
import moe.characterrevolution.charkointelligine.Database.*;
import moe.characterrevolution.charkointelligine.Matcher.MatchLoader;
import moe.characterrevolution.charkointelligine.Matcher.MatchValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * PackageLoader 类
 * 负责对词条库进行导入与导出
 */
public class PackageLoader {

    Database db;

    /**
     * 初始化
     */
    public void init() {
        db = new Database();
    }

    /**
     * 构造函数
     * 自动初始化
     */
    public PackageLoader() {
        init();
    }

    /**
     * 从文件导入词条至目标群数据库
     * @param groupId 目标群号
     * @param file 词条包文件
     * @param ErrorInfo 传递错误信息
     * @return 导入状态
     */
    public boolean leadIn(long groupId, File file, StringBuilder ErrorInfo) {
        StringBuffer sb = UserIO.readFile(file);
        List<PackageValue> packageList;

        try {
            packageList = JSONArray.parseArray(sb.toString(), PackageValue.class);
        } catch (Exception e) {
            ErrorInfo.append("json 反序列化失败，或许是文件格式不正确？\n");
            e.printStackTrace();
            return false;
        }

        for(PackageValue pv: packageList) {
            File database = new File("data/CharkoIntelligine/databases/", groupId + ".db");

            System.gc(); //需要垃圾回收，否则无法删除文件
            database.getAbsoluteFile().delete(); //删除数据库

            if(!db.connect(groupId)) {
                ErrorInfo.append("无法连接至数据库！");
                return false;
            }

            db.close();
            for(QueryValue qv: pv.getHistory())db.insert(groupId, pv.getTitle(), qv.getContent(), pv.getMode(), ErrorInfo);
        }

        return true;
    }

    /**
     * 从群数据库导出词条库至目标文件
     * @param ml 提供匹配器
     * @param groupId 目标群号
     * @param file 词条包文件
     * @param ErrorInfo 传递错误信息
     * @return 导出状态
     */
    public boolean leadOut(MatchLoader ml, long groupId, File file, StringBuilder ErrorInfo) {
        List<MatchValue> matchList = ml.all(groupId);
        List<PackageValue> packageList = new ArrayList<>();

        for(MatchValue mv: matchList) {
            List<QueryValue> history = db.history(groupId, mv.getId(), ErrorInfo);

            PackageValue pv = new PackageValue(mv.getTitle(), mv.getType(), history);

            packageList.add(pv);
        }

        UserIO.writeFile(file, JSON.toJSONString(packageList));

        return ErrorInfo.length() == 0;
    }

}
