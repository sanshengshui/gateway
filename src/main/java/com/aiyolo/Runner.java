package com.aiyolo;

import com.aiyolo.cache.CommonCache;
import com.aiyolo.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    CommonCache commonCache;

    @Autowired
    StorageService storageService;

    @Override
    public void run(String... args) throws Exception {
        commonCache.save("appLaunchTime", System.currentTimeMillis());
        storageService.init();

//        System.out.println(System.getProperty("java.io.tmpdir"));
    }

}
