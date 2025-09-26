package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.TravelExpenseDto;
import com.jungsan.backend.entity.TravelExpense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface TravelExpenseMapper {

    TravelExpense toEntity(TravelExpenseDto.Create dto);

    void updateEntity(TravelExpenseDto.Update dto, @MappingTarget TravelExpense entity);

    TravelExpenseDto.Response toResponse(TravelExpense entity);

    List<TravelExpenseDto.Response> toResponseList(List<TravelExpense> entities);
}
