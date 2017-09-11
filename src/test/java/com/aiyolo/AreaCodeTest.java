package com.aiyolo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aiyolo.service.AreaCodeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaCodeTest {

    @Autowired
    private AreaCodeService areaCodeService;

    @Test
    public void testGetAreaNameSuccess() {
        String areaName = areaCodeService.getAreaName("110101");
        System.out.println(areaName);

        assertThat(areaName).isEqualTo("北京市北京市市辖区东城区");
    }

    @Test
    public void testGetAreaNameError() {
        String areaName = areaCodeService.getAreaName("999999");
        System.out.println(areaName);

        assertThat(areaName).isEqualTo(null);
    }

}
