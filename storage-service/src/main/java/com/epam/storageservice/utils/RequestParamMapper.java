package com.epam.storageservice.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestParamMapper {
    public List<Integer> mapStringIdsToListOfIntegers(String var1) {
        return Arrays.stream(var1.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
