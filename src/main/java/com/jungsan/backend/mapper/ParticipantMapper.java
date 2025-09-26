package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant toEntity(ParticipantDto.Create dto);

    void updateEntity(ParticipantDto.Update dto, @MappingTarget Participant entity);

    ParticipantDto.Response toResponse(Participant entity);

    ParticipantDto.Simple toSimple(Participant entity);

    List<ParticipantDto.Response> toResponseList(List<Participant> entities);

    List<ParticipantDto.Simple> toSimpleList(List<Participant> entities);
}
