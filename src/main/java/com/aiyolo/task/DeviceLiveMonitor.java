package com.aiyolo.task;

import com.aiyolo.cache.DeviceLiveStatusCache;
import com.aiyolo.service.NoticeWarningService;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceLiveMonitor {

    private static final Log taskLogger = LogFactory.getLog("taskLog");
    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired CacheManager cacheManager;

    @Autowired DeviceLiveStatusCache deviceLiveStatusCache;

    @Autowired NoticeWarningService noticeWarningService;

    @Value("${device.live.beat.period}")
    private long deviceLiveBeatPeriod;

    @SuppressWarnings("unchecked")
    @Scheduled(fixedDelay = 60000)
    public void checkDeviceLive() {
        try {
            taskLogger.info("check device live start...");

            long currentTime = System.currentTimeMillis();
            int liveCheckCount = 0;
            int liveChangeCount = 0;

            Cache cache = cacheManager.getCache("deviceBeat");
            Object nativeCache = cache.getNativeCache();
            if (nativeCache instanceof net.sf.ehcache.Ehcache) {
                net.sf.ehcache.Ehcache ehCache = (net.sf.ehcache.Ehcache) nativeCache;
                List<Object> keys = ehCache.getKeys();

                if (keys.size() > 0) {
                    for (Object key : keys) {
                        Element element = ehCache.get(key);
                        if (element != null) {
                            String glId = (String) key;
                            long deviceBeat = (long) element.getObjectValue();
                            int deviceLiveStatus = deviceLiveStatusCache.getByGlId(glId);

                            int newLiveStatus;
                            if (currentTime - deviceBeat > deviceLiveBeatPeriod) {
                                newLiveStatus = DeviceLiveStatusCache.DEAD;
                            } else {
                                newLiveStatus = DeviceLiveStatusCache.LIVE;
                            }

                            if (deviceLiveStatus != newLiveStatus) {
                                deviceLiveStatusCache.save(glId, newLiveStatus);
                                if (deviceLiveStatus != DeviceLiveStatusCache.UNKNOWN) { // TODO:重启后缓存丢失，待优化（持久化||使用独立缓存）
                                    noticeWarningService.notifyDeviceLiveStatusChange(glId);
                                    liveChangeCount++;
                                }
                            }

                            liveCheckCount++;
                        }
                    }
                }
            }

            taskLogger.info("check device live completed.(" + liveCheckCount + " checked, " + liveChangeCount + " changed)");
        } catch (Exception e) {
            errorLogger.error("DeviceLiveMonitor异常！", e);
        }
    }

}
