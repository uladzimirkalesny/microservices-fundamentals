package com.epam.resourceservice.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ContextConfiguration(classes = {RequestParamMapper.class})
@ExtendWith(SpringExtension.class)
class RequestParamMapperTest {

    @Autowired
    private RequestParamMapper requestParamMapper;

    @Test
    @DisplayName("Check the request parameters mapper")
    void testMapStringIdsToListOfIntegers() {
        var idsAsString = "1,2";
        var actual = requestParamMapper.mapStringIdsToListOfIntegers(idsAsString);

        assertEquals(List.of(1, 2), actual);
    }
}

