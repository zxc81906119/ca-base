package com.redhat.cleanbase.adapter.in.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public sealed interface TestDto permits TestDto.Res, TestDto.Req {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    final class Req implements TestDto {
        @Schema(description = "任務次數")
        private Integer threads;

        @Schema(description = "任務迴圈次數")
        private Integer loop;

        @Schema(description = "是否印出日誌")
        private Boolean log = true;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    final class Res implements TestDto {
        @Schema(description = "地區")
        private String region;
    }
}
