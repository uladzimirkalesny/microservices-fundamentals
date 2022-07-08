package com.epam.songservice.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {RequestParamMapper.class})
@ExtendWith(SpringExtension.class)
class RequestParamMapperTest {

    @Autowired
    private RequestParamMapper requestParamMapper;

    @Test
    @DisplayName("Check the request parameters mapper")
    void testMapStringIdsToListOfIntegers2() {
        var idsAsString = "1,2";
        List<Integer> actual = requestParamMapper.mapStringIdsToListOfIntegers(idsAsString);
        assertEquals(List.of(1, 2), actual);
    }
}

