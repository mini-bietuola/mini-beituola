package com.netease.mini.bietuola.config.im;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.mini.bietuola.web.util.CheckSumBuilder;
import com.netease.mini.bietuola.web.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 云信验证码服务
 * @Auther ctl
 * @Date 2019/5/2
 */
@Service
public class CaptchaService {
    private static final Logger LOG = LoggerFactory.getLogger(CaptchaService.class);

    private ObjectMapper mapper = new ObjectMapper();

    //发送验证码的请求路径URL
    private static final String SEND_CODE_SERVER_URL="https://api.netease.im/sms/sendcode.action";
    //验证短信验证码的请求路径URL
    private static final String VERIFY_CODE_URL = "https://api.netease.im/sms/verifycode.action";
    //网易云信分配的账号
    private static final String APP_KEY="f7afe252675ae77963e5beef2b0e7c24";
    //网易云信分配的密钥
    private static final String APP_SECRET="452794b08c04";
    //短信模板ID
    private static final String AUTH_VERIFY_CODE_TEMPLATEID="4093383";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN="6";

    public boolean sendAuthVerifyCode(String phone) {
        Map<String, String> headers = createAuthHeader();
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("templateid", AUTH_VERIFY_CODE_TEMPLATEID);
        params.put("codeLen", CODELEN);
        try {
            HttpClientUtils.Response resp = HttpClientUtils.post(SEND_CODE_SERVER_URL, headers, params, null);
            if (resp.getStatusCode() != 200) {
                LOG.error("sendAuthVerifyCode fail, phone: {}, fail http status: {}", phone, resp.getStatusCode());
                return false;
            }
            JsonNode root = mapper.readTree(resp.getResponseBody());
            int code = root.get("code").asInt();
            return code == 200;
        } catch (IOException e) {
            LOG.error("sendAuthVerifyCode fail, phone: {}", phone, e);
            return false;
        }
    }

    public boolean verifyAuthVerifyCode(String phone, String verifyCode) {
        Map<String, String> headers = createAuthHeader();
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("code", verifyCode);
        try {
            HttpClientUtils.Response resp = HttpClientUtils.post(VERIFY_CODE_URL, headers, params, null);
            if (resp.getStatusCode() != 200) {
                LOG.error("verifyAuthVerifyCode fail, phone: {}, fail http status: {}", phone, resp.getStatusCode());
                return false;
            }
            JsonNode root = mapper.readTree(resp.getResponseBody());
            int code = root.get("code").asInt();
            return code == 200;
        } catch (IOException e) {
            LOG.error("verifyAuthVerifyCode fail, phone: {}", phone, e);
            return false;
        }
    }

    private Map<String, String> createAuthHeader() {
        String curTime = String.valueOf((new Date()).getTime() / 1000);
        String nonce = UUID.randomUUID().toString();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, nonce, curTime);
        Map<String, String> headers = new HashMap<>();
        headers.put("AppKey", APP_KEY);
        headers.put("Nonce", nonce);
        headers.put("CurTime", curTime);
        headers.put("CheckSum", checkSum);
        return headers;
    }


    public static void main(String[] args) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        String s = "{\n" +
//                "  \"code\": 200,\n" +
//                "  \"msg\": \"88\",\n" +
//                "  \"obj\": \"1908\"\n" +
//                "}";
//        JsonNode jsonNode = mapper.readTree(s);
//        System.out.println(jsonNode.get("code"));
//        System.out.println(jsonNode.get("code").asText());
//        System.out.println(jsonNode.get("code").asInt());

        String curTime = String.valueOf((new Date()).getTime() / 1000);
        String nonce = UUID.randomUUID().toString();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, nonce, curTime);
        Map<String, String> headers = new HashMap<>();
        headers.put("AppKey", APP_KEY);
        headers.put("Nonce", nonce);
        headers.put("CurTime", curTime);
        headers.put("CheckSum", checkSum);
        Map<String, String> params = new HashMap<>();
        params.put("mobile", "18817878571");
        params.put("templateid", "1234567");
        params.put("codeLen", CODELEN);
        HttpClientUtils.Response resp = HttpClientUtils.post(SEND_CODE_SERVER_URL, headers, params, null);
        System.out.println(resp);
    }

}
