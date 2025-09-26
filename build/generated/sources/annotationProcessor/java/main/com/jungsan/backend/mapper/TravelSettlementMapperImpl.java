package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.TravelSettlementDto;
import com.jungsan.backend.entity.TravelSettlement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-26T13:10:18+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.16 (Homebrew)"
)
@Component
public class TravelSettlementMapperImpl implements TravelSettlementMapper {

    @Override
    public TravelSettlement toEntity(TravelSettlementDto.Create dto) {
        if ( dto == null ) {
            return null;
        }

        TravelSettlement travelSettlement = new TravelSettlement();

        return travelSettlement;
    }

    @Override
    public void updateEntity(TravelSettlementDto.Update dto, TravelSettlement entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public TravelSettlementDto.Response toResponse(TravelSettlement entity) {
        if ( entity == null ) {
            return null;
        }

        TravelSettlementDto.Response response = new TravelSettlementDto.Response();

        return response;
    }

    @Override
    public TravelSettlementDto.Simple toSimple(TravelSettlement entity) {
        if ( entity == null ) {
            return null;
        }

        TravelSettlementDto.Simple simple = new TravelSettlementDto.Simple();

        return simple;
    }

    @Override
    public List<TravelSettlementDto.Response> toResponseList(List<TravelSettlement> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TravelSettlementDto.Response> list = new ArrayList<TravelSettlementDto.Response>( entities.size() );
        for ( TravelSettlement travelSettlement : entities ) {
            list.add( toResponse( travelSettlement ) );
        }

        return list;
    }

    @Override
    public List<TravelSettlementDto.Simple> toSimpleList(List<TravelSettlement> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TravelSettlementDto.Simple> list = new ArrayList<TravelSettlementDto.Simple>( entities.size() );
        for ( TravelSettlement travelSettlement : entities ) {
            list.add( toSimple( travelSettlement ) );
        }

        return list;
    }
}
