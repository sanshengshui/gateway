package com.aiyolo.service.alarm;

import com.aiyolo.entity.DeviceAlarm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AlarmService {

    public static Log errorLogger = LogFactory.getLog("errorLog");

    public abstract void run(DeviceAlarm deviceAlarm, Object params) throws Exception;

}
