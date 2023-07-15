package com.example.lmsbackend.services.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static com.example.lmsbackend.utils.DateTimeUtils.getGoogleDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DateTimeUtilsTest {

    @Test
    public void testABCXYZ() {
        var current = LocalDateTime.now();
        var googleDateTime = getGoogleDateTime(current);
    }
}
