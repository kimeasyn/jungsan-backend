package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.GameDto;
import com.jungsan.backend.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class, GameRoundMapper.class})
public interface GameMapper {

    Game toEntity(GameDto.Create dto);

    void updateEntity(GameDto.Update dto, @MappingTarget Game entity);

    GameDto.Response toResponse(Game entity);

    GameDto.Simple toSimple(Game entity);

    List<GameDto.Response> toResponseList(List<Game> entities);

    List<GameDto.Simple> toSimpleList(List<Game> entities);
}
