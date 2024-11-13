package com.redhat.cleanbase.web.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redhat.cleanbase.web.model.request.paging.ReqPageInfo;
import com.redhat.cleanbase.web.model.info.impl.ClientAppInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "請求物件")
public class GenericRequest<T> implements WrapRequest<T, ClientAppInfo> {

    @Schema(description = "請求的交易內容")
    private T data;

    @Schema(description = "交易結果回應內容是否為分頁模式", defaultValue = "false")
    private Boolean pageable = false;

    private ReqPageInfo reqPageInfo;

    private ClientAppInfo clientAppInfo;

    @Override
    public ClientAppInfo newClientAppInfo() {
        return new ClientAppInfo();
    }
}
