package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.PaymentDto;
import com.jungsan.backend.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface PaymentMapper {

    Payment toEntity(PaymentDto.Create dto);

    void updateEntity(PaymentDto.Update dto, @MappingTarget Payment entity);

    PaymentDto.Response toResponse(Payment entity);

    List<PaymentDto.Response> toResponseList(List<Payment> entities);
}
