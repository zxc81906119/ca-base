package com.redhat.cleanbase.web.model.info.impl;

import com.redhat.cleanbase.code.response.enums.StatusEnum;
import com.redhat.cleanbase.web.model.info.IServiceAppInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Server端AP執行的相關資訊")
public class ServiceAppInfo implements IServiceAppInfo {

    @Schema(description = "回覆代碼")
    private String code;

    @Schema(description = "訊息抬頭")
    private String title;

    @Schema(description = "回覆訊息")
    private String message;

    @Schema(description = "狀態 (SUCCESS or FAIL)")
    private StatusEnum status;

    @Schema(description = "追蹤ID")
    private String traceId;

    @Schema(description = "交易開始時間")
    private Date startTime;

    @Schema(description = "交易結束時間")
    private Date endTime;

    @Schema(description = "交易執行花費時間(單位：毫秒)")
    private Long execTimeMillis;

}
