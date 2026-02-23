package com.mballen.demo_park_api.web.controller.dto.mapper;

import com.mballen.demo_park_api.entity.Vaga;
import com.mballen.demo_park_api.web.controller.dto.VagaCreateDto;
import com.mballen.demo_park_api.web.controller.dto.VagaResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
