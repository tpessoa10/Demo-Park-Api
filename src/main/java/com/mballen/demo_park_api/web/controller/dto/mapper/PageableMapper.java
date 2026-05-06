package com.mballen.demo_park_api.web.controller.dto.mapper;

import com.mballen.demo_park_api.web.controller.dto.PageableDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper<T> {

    public static <T> PageableDto<T> toDto(Page<T> page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}
