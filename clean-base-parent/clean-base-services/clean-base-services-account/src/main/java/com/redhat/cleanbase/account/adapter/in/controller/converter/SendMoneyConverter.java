package com.redhat.cleanbase.account.adapter.in.controller.converter;

import com.redhat.cleanbase.account.adapter.in.controller.model.SendMoneyDto;
import com.redhat.cleanbase.account.application.port.usecase.model.SendMoneyCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface SendMoneyConverter {

    @Mapping(source = "sourceAccountId", target = "sourceAccountId.value")
    @Mapping(source = "targetAccountId", target = "targetAccountId.value")
    @Mapping(source = "amount", target = "moneyVo.amount")
    SendMoneyCommand dtoToCommand(SendMoneyDto.Req sendMoneyDto);

}
