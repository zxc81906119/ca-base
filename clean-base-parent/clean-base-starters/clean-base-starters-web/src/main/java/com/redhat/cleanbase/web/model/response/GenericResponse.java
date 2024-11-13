package com.redhat.cleanbase.web.model.response;


import com.redhat.cleanbase.i18n.msg_source.context.I18nContext;
import com.redhat.cleanbase.i18n.msg_source.input.GenericI18nInput;
import com.redhat.cleanbase.code.response.ResponseCode;
import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.web.model.info.impl.ServiceAppInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "交易結果物件")
public class GenericResponse<T> implements WrapResponse<T, ServiceAppInfo> {

    @Schema(description = "交易結果")
    private T data;

    private ServiceAppInfo serviceAppInfo;

    public static <T> GenericResponse<T> ok(T data) {
        return GenericResponse.body(ResponseCodeEnum.API_SUCCESS, data);
    }

    public static <T> GenericResponse<T> fail(T data) {
        return GenericResponse.body(ResponseCodeEnum.API_FAILED, data);
    }

    public static <T> GenericResponse<T> body(ResponseCode responseCode, T data) {
        val codeRoot = responseCode.getRoot();

        val i18nInput = new GenericI18nInput(codeRoot.getI18nKey(), null, codeRoot.getDefaultMessage());
        val message = I18nContext.getMessageSource().getMessage(i18nInput);

        val appInfo =
                ServiceAppInfo.builder()
                        .code(responseCode.getValue())
                        .message(message)
                        .title(codeRoot.getTitle())
                        .status(codeRoot.getStatus())
                        .build();

        return new GenericResponse<>(data, appInfo);
    }

    public static <T> ResponseEntity<GenericResponse<T>> response(ResponseCode responseCode, T data) {
        return GenericResponse.response(responseCode, data, null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> response(ResponseCode responseCode, T data, HttpHeaders httpHeaders) {
        val genericResponse = GenericResponse.body(responseCode, data);
        val codeRoot = responseCode.getRoot();
        return new ResponseEntity<>(genericResponse, httpHeaders, codeRoot.getHttpStatus());
    }

    @Override
    public ServiceAppInfo newServiceAppInfo() {
        return new ServiceAppInfo();
    }

}
