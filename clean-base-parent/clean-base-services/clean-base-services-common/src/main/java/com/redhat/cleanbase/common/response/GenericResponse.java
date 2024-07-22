package com.redhat.cleanbase.common.response;


import com.redhat.cleanbase.common.i18n.context.I18nContext;
import com.redhat.cleanbase.common.response.code.ResponseCode;
import com.redhat.cleanbase.common.response.code.ResponseCodeEnum;
import com.redhat.cleanbase.i18n.msg_source.input.GenericI18nInput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {

    @Schema(description = "回傳碼")
    private int code;

    @Schema(description = "回傳訊息")
    private String message;

    @Schema(description = "回傳資料")
    private T data;

    @Schema(description = "追蹤編號")
    private String traceId;

    public static <T> GenericResponse<T> ok(T data, String traceId) {
        return GenericResponse.body(ResponseCodeEnum.API_SUCCESS, data, traceId);
    }

    public static <T> GenericResponse<T> fail(T data, String traceId) {
        return GenericResponse.body(ResponseCodeEnum.API_FAILED, data, traceId);
    }

    public static <T> GenericResponse<T> body(ResponseCode responseCode, T data, String traceId) {
        val i18nValue = I18nContext.getMessageSource()
                .getMessage(new GenericI18nInput(responseCode.getRoot().getI18nKey()));
        return GenericResponse.body(responseCode.getValue(), i18nValue, data, traceId);
    }

    private static <T> GenericResponse<T> body(int code, String message, T data, String traceId) {
        return new GenericResponse<>(code, message, data, traceId);
    }

    public static <T> ResponseEntity<GenericResponse<T>> response(ResponseCode responseCode, T data, String traceId) {
        return GenericResponse.response(responseCode, data, null, traceId);
    }

    public static <T> ResponseEntity<GenericResponse<T>> response(ResponseCode responseCode, T data, HttpHeaders httpHeaders, String traceId) {
        val genericResponse = GenericResponse.body(responseCode, data, traceId);
        val root = responseCode.getRoot();
        return new ResponseEntity<>(genericResponse, httpHeaders, root.getHttpStatus());
    }

}
