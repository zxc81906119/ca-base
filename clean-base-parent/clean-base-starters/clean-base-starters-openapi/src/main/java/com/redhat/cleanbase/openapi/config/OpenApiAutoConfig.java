package com.redhat.cleanbase.openapi.config;

import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.exception.context.GenericExceptionContext;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
public class OpenApiAutoConfig {

    private static void addGenericExceptionResponseExample(Operation operation, HandlerMethod handlerMethod) {
        val exceptionTypes =
                Arrays.stream(handlerMethod.getMethod().getExceptionTypes())
                        .collect(Collectors.toSet());
        exceptionTypes.addAll(GenericExceptionContext.getGenericRtExceptions());

        val exceptionRespMap = new LinkedMultiValueMap<String, ExceptionInfoDetail>();
        for (Class<?> exceptionType : exceptionTypes) {
            if (GenericExceptionContext.isGenericException(exceptionType) && ReflectionUtils.canInstance(exceptionType)) {
                val exceptionInfoOpt = GenericExceptionContext.getExceptionInfoOpt(exceptionType);
                exceptionInfoOpt.ifPresent((exceptionInfo) -> {
                    val statusKey = String.valueOf(exceptionInfo.code().getRoot().getHttpStatus().value());
                    exceptionRespMap.add(statusKey, new ExceptionInfoDetail(exceptionInfo, exceptionType));
                });
            }
        }

        // 造出每個狀態碼的 response
        for (Map.Entry<String, List<ExceptionInfoDetail>> entry : exceptionRespMap.entrySet()) {
            val titleExampleMap = new LinkedHashMap<String, Example>();

            val exceptionDetails = getSortedExceptionDetails(entry);

            for (ExceptionInfoDetail exceptionInfoDetail : exceptionDetails) {

                val exceptionInfo = exceptionInfoDetail.exceptionInfo();

                val responseCode = exceptionInfo.code();

                val i18nKey = responseCode.getRoot().getI18nKey();

                val annoTitle = exceptionInfo.title();

                val title = getTitle(exceptionInfoDetail, i18nKey, annoTitle);

                val example = getExample(exceptionInfo, responseCode, i18nKey);

                putTileExample(titleExampleMap, title, example, exceptionInfo);
            }

            val httpStatus = entry.getKey();
            setApiResponse(operation, httpStatus, titleExampleMap);
        }
    }

    private static void setApiResponse(Operation operation, String httpStatus, Map<String, Example> titleExampleMap) {
        operation.getResponses()
                .addApiResponse(
                        httpStatus,
                        new ApiResponse()
                                .description("")
                                .content(
                                        new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                        new MediaType()
                                                                .examples(titleExampleMap)
                                                )
                                )
                );
    }

    private static List<ExceptionInfoDetail> getSortedExceptionDetails(Map.Entry<String, List<ExceptionInfoDetail>> entry) {
        val exceptionDetails = entry.getValue();
        exceptionDetails.sort(Comparator.comparingInt(exceptionInfoDetail -> Integer.parseInt(exceptionInfoDetail.exceptionInfo().code().getValue())));
        return exceptionDetails;
    }

    private static void putTileExample(Map<String, Example> examples, String title, String example, ExceptionInfo exceptionInfo) {
        examples.put(title,
                new Example()
                        .value(example)
                        .description(exceptionInfo.desc())
        );
    }

    private static String getExample(ExceptionInfo exceptionInfo, ResponseCodeEnum responseCodeEnum, String message) {
        val exceptionRespBodyTemplate = """
                    {
                        "code": %s ,
                        "message": "%s",
                        "data": {
                            "timestamp": "2022-11-28T08:31:52.766+00:00",
                            "path": "/api-path",
                            "details": %s
                        },
                        "traceId": "string"
                    }
                """;

        val annoExample = exceptionInfo.example();
        return exceptionRespBodyTemplate.formatted(
                responseCodeEnum.getValue()
                , message
                , StringUtils.hasText(annoExample) ?
                        annoExample
                        : "{}"
        );
    }

    private static String getTitle(ExceptionInfoDetail exceptionInfoDetail, String message, String title) {
        return StringUtils.hasText(title) ?
                title
                : exceptionInfoDetail.clazz().getSimpleName() + "-" + message;
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            addGenericExceptionResponseExample(operation, handlerMethod);
            return operation;
        };
    }

    private record ExceptionInfoDetail(@NonNull ExceptionInfo exceptionInfo, @NonNull Class<?> clazz) {
    }

}
