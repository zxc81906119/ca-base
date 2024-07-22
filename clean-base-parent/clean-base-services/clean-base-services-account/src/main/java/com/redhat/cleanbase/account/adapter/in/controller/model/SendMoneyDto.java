package com.redhat.cleanbase.account.adapter.in.controller.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public sealed interface SendMoneyDto permits SendMoneyDto.Res, SendMoneyDto.Req {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    final class Req implements SendMoneyDto {

        @Schema(description = "來源帳戶編號")
        @NotNull
        private Long sourceAccountId;

        @Schema(description = "目標帳戶編號")
        @NotNull
        private Long targetAccountId;

        @Schema(description = "轉帳金額")
        @NotNull
        private Long amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    final class Res implements SendMoneyDto {
        @Schema(description = "是否轉帳成功")
        private Boolean isSend;
    }
}
