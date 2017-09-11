package com.aiyolo;

import com.aiyolo.cache.DeviceLiveStatusCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceLiveStatusCacheTest {

    @Autowired
    private DeviceLiveStatusCache deviceLiveStatusCache;

    @Test
    public void testSaveDeviceLiveStatusCacheSuccess() {
        int deviceLiveStatus0 = deviceLiveStatusCache.getByGlId("521621757602365447");
        System.out.println(deviceLiveStatus0);

//        try {
//            Thread.sleep(190000);
//        } catch (InterruptedException e) {
//            System.out.println("Error");
//        }

        int deviceLiveStatus1 = deviceLiveStatusCache.getByGlId("521621757602365447");
        System.out.println(deviceLiveStatus1);

        deviceLiveStatusCache.save("521621757602365447", DeviceLiveStatusCache.DEAD);

        int deviceLiveStatus2 = deviceLiveStatusCache.getByGlId("521621757602365447");
        System.out.println(deviceLiveStatus2);

        int deviceLiveStatus3 = deviceLiveStatusCache.getByGlId("521621757602365447");
        System.out.println(deviceLiveStatus3);

        deviceLiveStatusCache.save("521621757602365447", DeviceLiveStatusCache.LIVE);

        int deviceLiveStatus4 = deviceLiveStatusCache.getByGlId("521621757602365447");
        System.out.println(deviceLiveStatus4);

        int deviceLiveStatus5 = deviceLiveStatusCache.getByGlId("521621757602365447");
        System.out.println(deviceLiveStatus5);

        assertThat(deviceLiveStatus5).isEqualTo(DeviceLiveStatusCache.LIVE);
    }

}
