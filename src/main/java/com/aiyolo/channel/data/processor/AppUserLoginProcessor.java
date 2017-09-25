package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.AppUser;
import com.aiyolo.entity.AppUserSession;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.repository.AppUserSessionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
            String phone = messageBodyJson.getString("phone");

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

            AppUserRepository appUserRepository = (AppUserRepository) SpringUtil.getBean("appUserRepository");
            AppUser appUser = appUserRepository.findFirstByUserIdOrderByIdDesc(userId);
            if (appUser != null) {
                appUser.setPhone(phone);
            } else {
                appUser = new AppUser(userId);
                appUser.setPhone(phone);
            }
            appUserRepository.save(appUser);

            // 写入文件待后续处理
            appUserLoginLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("AppUserLoginProcessor异常！message:" + message, e);
        }
    }

}
