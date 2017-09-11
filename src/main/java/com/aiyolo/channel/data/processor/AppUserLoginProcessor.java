package com.aiyolo.channel.data.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.AppUserSession;
import com.aiyolo.repository.AppUserSessionRepository;

public class AppUserLoginProcessor extends Processor {

    private static Log appUserLoginLogger = LogFactory.getLog("appUserLoginLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            String userId = messageBodyJson.getString("user_id");
            String mobileId = messageBodyJson.getString("mobile_id");
            String cid = messageBodyJson.getString("cid");
            String session = messageBodyJson.getString("session");

            AppUserSessionRepository appUserSessionRepository = (AppUserSessionRepository) SpringUtil.getBean("appUserSessionRepository");

            // 查询记录是否存在
            AppUserSession appUserSession = appUserSessionRepository.findFirstByUserIdOrderByIdDesc(userId);
            if (appUserSession != null) {
                appUserSession.setMobileId(mobileId);
                appUserSession.setCid(cid);
                appUserSession.setSession(session);
            } else {
                appUserSession = new AppUserSession(userId, mobileId, cid, session);
            }
            appUserSessionRepository.save(appUserSession);

            // 写入文件待后续处理
            appUserLoginLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("AppUserLoginProcessor异常！message:" + message, e);
        }
    }

}
