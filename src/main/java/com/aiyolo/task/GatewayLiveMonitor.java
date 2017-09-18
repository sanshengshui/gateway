package com.aiyolo.task;

import com.aiyolo.cache.GatewayLiveStatusCache;
import com.aiyolo.service.GatewayStatusService;
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
public class GatewayLiveMonitor {

    private static final Log taskLogger = LogFactory.getLog("taskLog");
    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired CacheManager cacheManager;

    @Autowired GatewayLiveStatusCache gatewayLiveStatusCache;

    @Autowired GatewayStatusService gatewayStatusService;

    @Value("${gateway.live.beat.period}")
    private long gatewayLiveBeatPeriod;

    @SuppressWarnings("unchecked")
    @Scheduled(fixedDelay = 60000)
    public void checkGatewayLive() {
        try {
            taskLogger.info("check gateway live start...");

            long currentTime = System.currentTimeMillis();
            int liveCheckCount = 0;
            int liveChangeCount = 0;

            Cache cache = cacheManager.getCache("gatewayBeat");
            Object nativeCache = cache.getNativeCache();
            if (nativeCache instanceof net.sf.ehcache.Ehcache) {
                net.sf.ehcache.Ehcache ehCache = (net.sf.ehcache.Ehcache) nativeCache;
                List<Object> keys = ehCache.getKeys();

                if (keys.size() > 0) {
                    for (Object key : keys) {
                        Element element = ehCache.get(key);
                        if (element != null) {
                            String glId = (String) key;
                            long gatewayBeat = (long) element.getObjectValue();
                            int gatewayLiveStatus = gatewayLiveStatusCache.getByGlId(glId);

                            int newLiveStatus;
                            if (currentTime - gatewayBeat > gatewayLiveBeatPeriod) {
                                newLiveStatus = GatewayLiveStatusCache.DEAD;
                            } else {
                                newLiveStatus = GatewayLiveStatusCache.LIVE;
                            }

                            if (gatewayLiveStatus != newLiveStatus) {
                                gatewayLiveStatusCache.save(glId, newLiveStatus);
                                if (gatewayLiveStatus != GatewayLiveStatusCache.UNKNOWN) {
                                    gatewayStatusService.notifyGatewayLiveStatusChange(glId);
                                    liveChangeCount++;
                                }
                            }

                            liveCheckCount++;
                        }
                    }
                }
            }

            taskLogger.info("check gateway live completed.(" + liveCheckCount + " checked, " + liveChangeCount + " changed)");
        } catch (Exception e) {
            errorLogger.error("GatewayLiveMonitor异常！", e);
        }
    }

}
