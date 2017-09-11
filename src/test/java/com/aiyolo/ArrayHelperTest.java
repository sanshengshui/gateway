package com.aiyolo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aiyolo.common.ArrayHelper;

import net.sf.json.JSONArray;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArrayHelperTest {

    @Test
    public void testRemoveNullElementSuccess() {
        String[] testArray = new String[5];
        testArray[1] = "test";
        String[] newArray = (String[]) ArrayHelper.removeNullElement(testArray);

        System.out.println(JSONArray.fromObject(testArray).toString());
        System.out.println(JSONArray.fromObject(newArray).toString());

        String[] matchArray = new String[1];
        matchArray[0] = "test";
        assertThat(newArray).isEqualTo(matchArray);
    }

}
