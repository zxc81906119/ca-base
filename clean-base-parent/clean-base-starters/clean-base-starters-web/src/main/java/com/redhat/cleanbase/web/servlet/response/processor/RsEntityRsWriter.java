package com.redhat.cleanbase.web.servlet.response.processor;

import com.redhat.cleanbase.common.func.Operator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class RsEntityRsWriter {

    @NonNull
    private final Operator<Object, String, ?> converter;

    @SneakyThrows
    public void write(HttpServletResponse response, ResponseEntity<?> entity) {

        if (entity == null) {
            return;
        }

        val statusCode = entity.getStatusCode();
        response.setStatus(statusCode.value());

        val headers = entity.getHeaders();
        if (!headers.isEmpty()) {
            headers.forEach((name, values) -> {
                if (name != null && !CollectionUtils.isEmpty(values)) {
                    for (String value : values) {
                        response.addHeader(name, value);
                    }
                }
            });
        }

        val body = entity.getBody();
        if (body != null) {
            val responseBody =
                    body instanceof String stringBody
                            ? stringBody
                            : converter.operate(body);

            if (response.isCommitted()) {
                throw new RuntimeException("rs has committed , can't write");
            }

            response.resetBuffer();
            response.getWriter()
                    .write(responseBody);
        }
    }

    public Consumer<ResponseEntity<?>> lazyWrite(HttpServletResponse response) {
        return (entity) ->
                write(response, entity);
    }

    public Consumer<Supplier<ResponseEntity<?>>> lazyWriteWithSupplier(HttpServletResponse response) {
        return (entitySupplier) ->
                write(response, entitySupplier.get());
    }

}
