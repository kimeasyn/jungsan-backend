package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.GameRoundDto;
import com.jungsan.backend.entity.GameRound;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class, PaymentMapper.class})
public interface GameRoundMapper {

    GameRound toEntity(GameRoundDto.Create dto);

    void updateEntity(GameRoundDto.Update dto, @MappingTarget GameRound entity);

    GameRoundDto.Response toResponse(GameRound entity);

    GameRoundDto.Simple toSimple(GameRound entity);

    List<GameRoundDto.Response> toResponseList(List<GameRound> entities);

    List<GameRoundDto.Simple> toSimpleList(List<GameRound> entities);
}
