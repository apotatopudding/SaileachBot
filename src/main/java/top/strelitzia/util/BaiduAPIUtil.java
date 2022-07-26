package top.strelitzia.util;

import com.baidu.aip.client.BaseClient;
import com.baidu.aip.http.AipRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 单例模式获取的百度云的配置
 * 在首次使用时，需要使用setInstance写入百度云提供的数据而获取返回值
 * 后续可以直接使用getInstance读取已存储的静态值
 * @author strelitzia
 * @Date 2022/05/03 14:38
 **/
public class BaiduAPIUtil{

    /**
     * 单例实现
     */
    private static volatile BaiduAPIUtil identifyInstance = null;

    private static volatile BaiduAPIUtil auditInstance = null;

    public String APP_ID;
    public String API_KEY;
    public String SECRET_KEY;

    public BaiduAPIUtil(String APP_ID, String API_KEY, String SECRET_KEY) {
        this.APP_ID = APP_ID;
        this.API_KEY = API_KEY;
        this.SECRET_KEY = SECRET_KEY;
    }

    //分离首次读取和后续读取
    public static BaiduAPIUtil getInstance(int a) {
        if (a == 1) {
            return identifyInstance;
        }else {
            return auditInstance;
        }
    }

    /**
     * 可选的单例模式
     * @param a 1为调用百度识图的单例，2为调用百度审核的单例
     * @param APP_ID 百度云提供的APP_ID
     * @param API_KEY 百度云提供的API_KEY
     * @param SECRET_KEY 百度云提供的SECRET_KEY
     * @return 写入完毕的当前静态单例值
     */
    public static BaiduAPIUtil setInstance(int a,String APP_ID, String API_KEY, String SECRET_KEY){
        if (a == 1) {
            if (identifyInstance == null) {
                synchronized (BaiduAPIUtil.class) {
                    if (identifyInstance == null) {
                        identifyInstance = new BaiduAPIUtil(APP_ID, API_KEY, SECRET_KEY);
                    }
                }
            }
            return identifyInstance;
        }else {
            if (auditInstance == null) {
                synchronized (BaiduAPIUtil.class) {
                    if (auditInstance == null) {
                        auditInstance = new BaiduAPIUtil(APP_ID, API_KEY, SECRET_KEY);
                    }
                }
            }
            return auditInstance;
        }
    }

    /**
     * 根据图片url，调用百度识图api获取文字列表
     * 白底黑字的截图有可能一行读取为一个单词
     *
     * @param url
     * @return
     */
    public String[] BaiduOCRGetTags(String url) {
        // 初始化一个AipOcr
        MyApiOcr client = new MyApiOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        //只筛选识图的这些词语，其他词语抛弃
        String[] all = {"远程位", "治疗", "支援机械", "医疗干员", "近战位", "支援", "近卫干员", "新手"
                , "先锋干员", "术师干员", "狙击干员", "重装干员", "费用回复", "输出", "群攻", "减速", "辅助干员"
                , "生存", "防护", "削弱", "快速复活", "特种干员", "位移", "资深干员", "高级资深干员", "爆发", "控场", "召唤"};
        List<String> allTag = Arrays.asList(all);

        // 调用接口
        JSONObject res = client.basicAccurateGeneral(1, url, new HashMap<>());
        JSONArray words_result = new JSONArray(res.get("words_result").toString());
        List<String> str = new ArrayList<>();
        for (int i = 0; i < words_result.length(); i++) {
            String words = words_result.getJSONObject(i).getString("words");
            if (words.contains("高级资深")) words = "高级资深干员";
            if (allTag.contains(words)) str.add(words);

        }
        String[] s = new String[str.size()];
        str.toArray(s);
        return s;
    }

    /**
     *  百度图片审核方法
     * @param url 图片url
     * @return
     */
    public String BaiduCheck(String url) {
        // 初始化一个AipOcr
        MyApiOcr client = new MyApiOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.basicAccurateGeneral(2, url, new HashMap<>());
        return res.getString("conclusion");
    }

    //public byte[] base64ToImgByteArray(String base64) throws IOException {
    //    Decoder decoder = Base64.getDecoder();
        //因为参数base64来源不一样，需要将附件数据替换清空掉。如果此入参来自canvas.toDataURL("image/png");
    //    base64 = base64.replaceAll("data:image/png;base64,", "");
        //base64解码并转为二进制数组
    //    byte[] bytes = decoder.decode(base64);
    //    for (int i = 0; i < bytes.length; ++i) {
    //        if (bytes[i] < 0) {// 调整异常数据
    //            bytes[i] += 256;
    //        }
    //    }
    //    return bytes;
    //}
}

class MyApiOcr extends BaseClient{
    /**
     * 我他妈直接重写百度的高精度sdk方法
     * @param appId
     * @param apiKey
     * @param secretKey
     */
    protected MyApiOcr(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }

    //图像识别为1，图像审核为2
    public JSONObject basicAccurateGeneral(int way, String url, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);

        if (way == 1) {
            request.addBody("url", url);
        }else {
            request.addBody("imgUrl", url);
        }
        if (options != null) {
            request.addBody(options);
        }
        if (way == 1) {
            request.setUri("https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic");
        }else {
            request.setUri("https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined");
        }
        postOperation(request);
        return requestServer(request);
    }
}