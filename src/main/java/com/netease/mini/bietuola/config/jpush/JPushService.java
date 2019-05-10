package com.netease.mini.bietuola.config.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Description 极光推送服务
 * @Author ttw
 * @Date 2019/5/8
 **/
@Service
public class JPushService {
    private static final Logger LOG = LoggerFactory.getLogger(JPushService.class);

    private static final String appKey = "8e0890d0c7eea09febe12876";
    private static final String masterSecret = "d96c79555db5ae0f9a603ad5";
    ClientConfig clientConfig = ClientConfig.getInstance();
    final JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);

    public static PushPayload buildPushObject_android_tag_alertWithTitle(String alert, String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("team"))
                .setNotification(Notification.android(alert, title, null))
                .build();
    }


    public void SendCheckMessage(String teamName, String startTime, String endTime, String sendTime, String checkTime){
        final PushPayload payload = buildPushObject_android_tag_alertWithTitle("您的小组" + teamName + "打卡时间为" + checkTime, "打卡时间马上就要到啦~不要忘记打卡噢");

        try {
            //PushResult result = jpushClient.sendPush(payload);
            ScheduleResult result = jpushClient.createDailySchedule("打卡提醒", startTime, endTime, sendTime, 1, payload);
            LOG.info("Got result - " + result);


        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
        }
    }

    public void SendTeamStartMessage(String teamName, String sendTime){
        final PushPayload payload = buildPushObject_android_tag_alertWithTitle("您的小组\"" + teamName + "\"明天就要开始打卡啦~", "小组开始");

        try {
            //PushResult result = jpushClient.sendPush(payload);
            ScheduleResult result = jpushClient.createSingleSchedule("小队开始提醒", sendTime, payload);
            LOG.info("Got result - " + result);


        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
        }
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JPushService jPushService = new JPushService();
        jPushService.SendTeamStartMessage("梦之队", sdf.format(new Date(System.currentTimeMillis() + 5 * 1000)));
    }

}
