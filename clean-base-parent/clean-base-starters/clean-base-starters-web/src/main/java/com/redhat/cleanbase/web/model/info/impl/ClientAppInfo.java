package com.redhat.cleanbase.web.model.info.impl;

import com.redhat.cleanbase.web.model.info.IClientAppInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Schema(description = "前端追蹤資訊")
public class ClientAppInfo implements IClientAppInfo {

    @Schema(description = "交易ID")
    private String functionId;

    @Schema(description = "交易明細ID")
    private String transactionId;

    @Schema(description = "交易流水號")
    private String txSeqNo;

    @Schema(description = "交易時間")
    private String requestTime;

}
