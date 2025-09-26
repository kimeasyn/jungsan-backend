package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.TravelExpenseDto;
import com.jungsan.backend.entity.TravelExpense;
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
public class TravelExpenseMapperImpl implements TravelExpenseMapper {

    @Override
    public TravelExpense toEntity(TravelExpenseDto.Create dto) {
        if ( dto == null ) {
            return null;
        }

        TravelExpense travelExpense = new TravelExpense();

        return travelExpense;
    }

    @Override
    public void updateEntity(TravelExpenseDto.Update dto, TravelExpense entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public TravelExpenseDto.Response toResponse(TravelExpense entity) {
        if ( entity == null ) {
            return null;
        }

        TravelExpenseDto.Response response = new TravelExpenseDto.Response();

        return response;
    }

    @Override
    public List<TravelExpenseDto.Response> toResponseList(List<TravelExpense> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TravelExpenseDto.Response> list = new ArrayList<TravelExpenseDto.Response>( entities.size() );
        for ( TravelExpense travelExpense : entities ) {
            list.add( toResponse( travelExpense ) );
        }

        return list;
    }
}
