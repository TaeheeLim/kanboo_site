package com.kanboo.www.util;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SendSMS {

    private static final Logger logger = LoggerFactory.getLogger(SendSMS.class);

    public static void send(String receiver,String pw){
        String api_key = "NCS6W8GBMLCNYMLJ";
        String api_secret = "M23HGCAO0WVQYZEE4Q2FYTCMXQDGLPTL";
        Message coolsms = new Message(api_key, api_secret);
        // 4 params(to, from, type, text) are mandatory. must be filled

        HashMap<String, String> params = new HashMap<>();
        params.put("to", receiver);   // 수신전화번호
        params.put("from", "01068591546");   // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "안녕하세요 깐부입니다. 임시비밀번호는 " +pw+"입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
        } catch (CoolsmsException e) {
            logger.info(e.getMessage());
        }
    }


}