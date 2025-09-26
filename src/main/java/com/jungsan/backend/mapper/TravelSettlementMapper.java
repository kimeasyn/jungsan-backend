package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.TravelSettlementDto;
import com.jungsan.backend.entity.TravelSettlement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TravelExpenseMapper.class})
public interface TravelSettlementMapper {

    TravelSettlement toEntity(TravelSettlementDto.Create dto);

    void updateEntity(TravelSettlementDto.Update dto, @MappingTarget TravelSettlement entity);

    TravelSettlementDto.Response toResponse(TravelSettlement entity);

    TravelSettlementDto.Simple toSimple(TravelSettlement entity);

    List<TravelSettlementDto.Response> toResponseList(List<TravelSettlement> entities);

    List<TravelSettlementDto.Simple> toSimpleList(List<TravelSettlement> entities);
}
