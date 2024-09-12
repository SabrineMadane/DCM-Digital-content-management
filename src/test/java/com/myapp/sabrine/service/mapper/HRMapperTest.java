package com.myapp.sabrine.service.mapper;

import static com.myapp.sabrine.domain.HRAsserts.*;
import static com.myapp.sabrine.domain.HRTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HRMapperTest {

    private HRMapper hRMapper;

    @BeforeEach
    void setUp() {
        hRMapper = new HRMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHRSample1();
        var actual = hRMapper.toEntity(hRMapper.toDto(expected));
        assertHRAllPropertiesEquals(expected, actual);
    }
}
