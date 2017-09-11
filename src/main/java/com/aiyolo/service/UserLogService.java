package com.aiyolo.service;

import com.aiyolo.entity.UserLog;
import com.aiyolo.repository.UserLogRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by xujun on 2017/5/15.
 */
@Service
public class UserLogService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    UserLogRepository userLogRepository;

    @Async
    public void store(UserLog userLog) {
        try {
            userLogRepository.save(userLog);
        } catch (Exception e) {
            errorLogger.error("操作日志存储异常！", e);
        }
    }

}
