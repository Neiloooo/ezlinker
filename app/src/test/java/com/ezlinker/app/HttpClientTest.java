package com.ezlinker.app;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * @program: ezlinker
 * @description:
 * @author: wangwenhai
 * @create: 2019-12-13 15:16
 **/
public class HttpClientTest {
    public static void main(String[] args) {


        String result = HttpUtil.get("http://whois.pconline.com.cn/ipJson.jsp?ip=12.77.80.162&json=true");

        JSONObject data = JSONObject.parseObject(result.trim().replace("if(window.IPCallBack) {IPCallBack(", "").replace(");}", ""));
        String a = "IP地址:" + data.getString("ip")
                + ";所在省:" + data.getString("pro")
                + ";所在城市:" + data.getString("city")
                + ";运营商信息:" + data.getString("addr");
        System.out.println(a);
    }
}
