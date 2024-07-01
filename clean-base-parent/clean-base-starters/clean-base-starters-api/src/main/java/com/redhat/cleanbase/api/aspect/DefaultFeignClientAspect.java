package com.redhat.cleanbase.api.aspect;

import feign.*;
import feign.codec.DecodeException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.val;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class DefaultFeignClientAspect extends FeignClientAspect {

    @Override
    protected Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e) {
        if (e instanceof CallNotPermittedException callNotPermittedException) {
            // todo 斷路器開啟狀態,直接拋出此例外
        } else if (e instanceof RetryableException retryableException) {
            // todo 可以拿到被包裝的例外
            val cause = retryableException.getCause();
            // todo call api 發生 io exception or 子 exception
            // todo java.net.SocketTimeoutException 是 timeout 時會拋出之例外,也是 io exception 子類
            // todo default error decoder 看到 response header 有 Retry-After 就會包裝成 RetryableException
            // todo retryer 接到此例外會進行重試判斷,
            //  如未讓 feign client 掛其他 retryer , 預設是不 retry, 直接拋出相同 retryableException
        } else if (e instanceof FeignException feignException) {
            if (feignException instanceof FeignException.FeignClientException feignClientException) {
                if (feignClientException instanceof FeignException.BadRequest badRequest) {
                    // 400
                } else if (feignClientException instanceof FeignException.Conflict conflict) {
                    // 409
                } else if (feignClientException instanceof FeignException.Forbidden forbidden) {
                    // 403
                } else if (feignClientException instanceof FeignException.Gone gone) {
                    // 410
                } else if (feignClientException instanceof FeignException.MethodNotAllowed methodNotAllowed) {
                    // 405
                } else if (feignClientException instanceof FeignException.NotAcceptable notAcceptable) {
                    // 406
                } else if (feignClientException instanceof FeignException.NotFound notFound) {
                    // 404
                } else if (feignClientException instanceof FeignException.TooManyRequests tooManyRequests) {
                    // 429
                } else if (feignClientException instanceof FeignException.Unauthorized unauthorized) {
                    // 401
                } else if (feignClientException instanceof FeignException.UnprocessableEntity unprocessableEntity) {
                    // 422
                } else if (feignClientException instanceof FeignException.UnsupportedMediaType unsupportedMediaType) {
                    // 415
                } else {
                    // others
                }
            } else if (feignException instanceof FeignException.FeignServerException feignServerException) {
                if (feignServerException instanceof FeignException.BadGateway badGateway) {
                    // 502
                } else if (feignServerException instanceof FeignException.InternalServerError internalServerError) {
                    // 500
                } else if (feignServerException instanceof FeignException.GatewayTimeout gatewayTimeout) {
                    // 504
                } else if (feignServerException instanceof FeignException.NotImplemented notImplemented) {
                    // 501
                } else if (feignServerException instanceof FeignException.ServiceUnavailable serviceUnavailable) {
                    // 503
                } else {
                    // others
                }
            } else if (feignException instanceof DecodeException) {
                // todo decode response data 失敗
            } else {
                // other condition
                // response 狀態碼不是 client or server error
            }
        }
        return e;
    }


//    @Override
//    public Object invoke(Object[] argv) throws Throwable {
//        RequestTemplate template = buildTemplateFromArgs.create(argv);
//        Request.Options options = findOptions(argv);
//        Retryer retryer = this.retryer.clone();
//        while (true) {
//            try {
//                return executeAndDecode(template, options);
//            } catch (RetryableException e) {
//                try {
//                    retryer.continueOrPropagate(e);
//                } catch (RetryableException th) {
//                    Throwable cause = th.getCause();
//                    if (propagationPolicy == UNWRAP && cause != null) {
//                        throw cause;
//                    } else {
//                        throw th;
//                    }
//                }
//                if (logLevel != Logger.Level.NONE) {
//                    logger.logRetry(metadata.configKey(), logLevel);
//                }
//                continue;
//            }
//        }
//    }


//    Object executeAndDecode(RequestTemplate template, Request.Options options) throws Throwable {
//        Request request = targetRequest(template);
//
//        if (logLevel != Logger.Level.NONE) {
//            logger.logRequest(metadata.configKey(), logLevel, request);
//        }
//
//        Response response;
//        long start = System.nanoTime();
//        try {
//            response = client.execute(request, options);
//            // ensure the request is set. TODO: remove in Feign 12
//            response = response.toBuilder()
//                    .request(request)
//                    .requestTemplate(template)
//                    .build();
//        } catch (IOException e) {
//            if (logLevel != Logger.Level.NONE) {
//                logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime(start));
//            }
//            throw errorExecuting(request, e);
//        }
//
//        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
//        return responseHandler.handleResponse(
//                metadata.configKey(), response, metadata.returnType(), elapsedTime);
//    }


//    static FeignException errorExecuting(Request request, IOException cause) {
//        final Long nonRetryable = null;
//        return new RetryableException(
//                -1,
//                format("%s executing %s %s", cause.getMessage(), request.httpMethod(), request.url()),
//                request.httpMethod(),
//                cause,
//                nonRetryable, request);
//    }


//    public Object handleResponse(String configKey,
//                                 Response response,
//                                 Type returnType,
//                                 long elapsedTime)
//            throws Exception {
//        try {
//            response = logAndRebufferResponseIfNeeded(configKey, response, elapsedTime);
//            return executionChain.next(
//                    new InvocationContext(configKey, decoder, errorDecoder, dismiss404, closeAfterDecode,
//                            decodeVoid, response, returnType));
//        } catch (final IOException e) {
//            if (logLevel != Logger.Level.NONE) {
//                logger.logIOException(configKey, logLevel, e, elapsedTime);
//            }
//            throw errorReading(response.request(), response, e);
//        } catch (Exception e) {
//            ensureClosed(response.body());
//            throw e;
//        }
//    }


//    public Object proceed() throws Exception {
//        if (returnType == Response.class) {
//            return disconnectResponseBodyIfNeeded(response);
//        }
//
//        try {
//            final boolean shouldDecodeResponseBody =
//                    (response.status() >= 200 && response.status() < 300)
//                            || (response.status() == 404 && dismiss404
//                            && !isVoidType(returnType));
//
//            if (!shouldDecodeResponseBody) {
//                throw decodeError(configKey, response);
//            }
//
//            if (isVoidType(returnType) && !decodeVoid) {
//                ensureClosed(response.body());
//                return null;
//            }
//
//            Class<?> rawType = Types.getRawType(returnType);
//            if (TypedResponse.class.isAssignableFrom(rawType)) {
//                Type bodyType = Types.resolveLastTypeParameter(returnType, TypedResponse.class);
//                return TypedResponse.builder(response)
//                        .body(decode(response, bodyType))
//                        .build();
//            }
//
//            return decode(response, returnType);
//        } finally {
//            if (closeAfterDecode) {
//                ensureClosed(response.body());
//            }
//        }
//    }


//    private Object decode(Response response, Type returnType) {
//        try {
//            return decoder.decode(response, returnType);
//        } catch (final FeignException e) {
//            throw e;
//        } catch (final RuntimeException e) {
//            throw new DecodeException(response.status(), e.getMessage(), response.request(), e);
//        } catch (IOException e) {
//            throw errorReading(response.request(), response, e);
//        }
//    }

//    todo default error decoder 如果看到 response 有 Retry-After 就會包裝成 RetryableException
}
