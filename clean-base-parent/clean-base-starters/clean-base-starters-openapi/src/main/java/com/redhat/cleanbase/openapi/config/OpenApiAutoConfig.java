package com.redhat.cleanbase.openapi.config;

import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.exception.context.GenericExceptionContext;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import com.redhat.cleanbase.code.response.ResponseCode;
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
            if (GenericExceptionContext.isGenericException(exceptionType)
                    && ReflectionUtils.canInstance(exceptionType)
            ) {
                GenericExceptionContext.getExceptionInfoOpt(exceptionType)
                        .ifPresent((exceptionInfo) -> {
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
                // 規格就直接用預設值即可
                val message = responseCode.getRoot().getDefaultMessage();
                val title = getTitle(exceptionInfoDetail, message, exceptionInfo.title());
                val example = getExample(exceptionInfo, responseCode, message);
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
        return entry.getValue().stream()
                .sorted(
                        Comparator.comparingInt(exceptionInfoDetail ->
                                Integer.parseInt(exceptionInfoDetail.exceptionInfo().code().getValue()))
                )
                .toList();
    }

    private static void putTileExample(Map<String, Example> examples, String title, String example, ExceptionInfo exceptionInfo) {
        examples.put(title,
                new Example()
                        .value(example)
                        .description(exceptionInfo.desc())
        );
    }

    private static String getExample(ExceptionInfo exceptionInfo, ResponseCode responseCode, String message) {
        val exceptionRespBodyTemplate = """
                    {
                        "code": %s ,
                        "message": "%s",
                        "data": %s,
                        "traceId": "string"
                    }
                """;

        val annoExample = exceptionInfo.example();
        return exceptionRespBodyTemplate.formatted(
                responseCode.getValue()
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

    private record ExceptionInfoDetail(
            @NonNull ExceptionInfo exceptionInfo,
            @NonNull Class<?> clazz
    ) {
    }

}
