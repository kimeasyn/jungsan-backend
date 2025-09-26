package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.PaymentDto;
import com.jungsan.backend.entity.Payment;
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
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentDto.Create dto) {
        if ( dto == null ) {
            return null;
        }

        Payment payment = new Payment();

        return payment;
    }

    @Override
    public void updateEntity(PaymentDto.Update dto, Payment entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public PaymentDto.Response toResponse(Payment entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentDto.Response response = new PaymentDto.Response();

        return response;
    }

    @Override
    public List<PaymentDto.Response> toResponseList(List<Payment> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaymentDto.Response> list = new ArrayList<PaymentDto.Response>( entities.size() );
        for ( Payment payment : entities ) {
            list.add( toResponse( payment ) );
        }

        return list;
    }
}
