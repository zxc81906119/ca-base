package com.redhat.cleanbase.web.model.request.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分頁資訊")
public class ReqPageInfo {

    @Schema(description = "第幾頁", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "0")
    private Integer pageIndex;

    @Schema(description = "每頁筆數", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "20")
    private Integer pageSize;

}
