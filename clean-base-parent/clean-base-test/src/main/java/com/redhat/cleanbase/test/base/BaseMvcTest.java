package com.redhat.cleanbase.test.base;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.cleanbase.test.annotation.FileUploadInfo;
import com.redhat.cleanbase.test.annotation.MvcInfo;
import com.redhat.cleanbase.test.model.MvcTestInfo;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@AutoConfigureMockMvc
public abstract class BaseMvcTest extends BaseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String ROOT_TEST_DATA_PATH = "test_data/controller";
    public static final String RQ_DATA_FILE_NAME = "rqData.json";

    @Autowired
    protected MockMvc mvc;

    @SneakyThrows
    protected MockHttpServletRequestBuilder getMockRequestBuilder() {
        val thisClass = getClass();

        val mvcTestInfo =
                Arrays.stream(Thread.currentThread().getStackTrace())
                        .filter((stackTraceElement) -> isTestClass(thisClass, stackTraceElement))
                        .map(StackTraceElement::getMethodName)
                        .map((methodName) -> getMvcInfo(thisClass, methodName))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElseThrow();

        val mvcInfo = mvcTestInfo.mvcInfo();

        val uri = mvcInfo.url();
        val httpMethod = mvcInfo.httpMethod().asHttpMethod();
        val fileUploadInfos = mvcInfo.fileUploadInfos();
        val annotationContentType = mvcInfo.contentType();

        val mockHttpServletRequestBuilder =
                getServletRequestBuilder(
                        uri
                        , httpMethod
                        , fileUploadInfos
                        , annotationContentType
                );

        val contentType = getContentType(annotationContentType, mockHttpServletRequestBuilder);

        mockHttpServletRequestBuilder.contentType(contentType);

        val annotationRequestDataPath = mvcInfo.requestDataPath();
        val requestDataPath =
                annotationRequestDataPath.isEmpty() ?
                        getDefaultTestDataPath(mvcTestInfo)
                        : annotationRequestDataPath;
        val dataContent = getClassPathResourceString(requestDataPath);

        setContentToBody(
                mockHttpServletRequestBuilder
                , contentType
                , dataContent
                , fileUploadInfos
        );

        return mockHttpServletRequestBuilder;
    }

    private static String getClassPathResourceString(String requestDataPath) throws IOException {
        return getClassPathResource(requestDataPath)
                .getContentAsString(StandardCharsets.UTF_8);
    }

    private static InputStream getClassPathResourceInputStream(String requestDataPath) throws IOException {
        return getClassPathResource(requestDataPath)
                .getInputStream();
    }

    private static ClassPathResource getClassPathResource(String requestDataPath) {
        return new ClassPathResource(requestDataPath);
    }

    private static String getContentType(String annotationContentType
            , MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
        return mockHttpServletRequestBuilder instanceof MockMultipartHttpServletRequestBuilder ?
                MediaType.MULTIPART_FORM_DATA_VALUE
                : annotationContentType;
    }

    private static MockHttpServletRequestBuilder getServletRequestBuilder(
            String uri
            , HttpMethod httpMethod
            , FileUploadInfo[] fileUploadInfos
            , String originContentType
    ) {
        return MediaType.MULTIPART_FORM_DATA_VALUE.equals(originContentType)
                || fileUploadInfos.length != 0 ?
                MockMvcRequestBuilders.multipart(uri)
                : MockMvcRequestBuilders.request(httpMethod, uri);
    }

    @SneakyThrows
    private MockMultipartFile getMultipartFile(FileUploadInfo fileUploadInfo) {
        val filePath = fileUploadInfo.filePath();
        String fileName = fileUploadInfo.fileName();
        if (fileName.isEmpty()) {
            val filePathSplit = filePath.split("[/\\\\]");
            fileName = filePathSplit[filePathSplit.length - 1].replaceAll("^[^:]+:", "");
        }
        val contentType = fileUploadInfo.contentType();
        val requestFieldName = fileUploadInfo.requestFieldName();
        return new MockMultipartFile(
                requestFieldName
                , fileName
                , contentType
                , getClassPathResourceInputStream(filePath)
        );
    }

    public void setContentToBody(
            MockHttpServletRequestBuilder mockHttpServletRequestBuilder
            , String contentType
            , String jsonData
            , FileUploadInfo[] fileUploadInfos
    ) throws JsonProcessingException {
        switch (contentType) {
            case MediaType.APPLICATION_JSON_VALUE -> jsonDataProcess(mockHttpServletRequestBuilder, jsonData);
            case MediaType.APPLICATION_FORM_URLENCODED_VALUE ->
                    formDataProcess(mockHttpServletRequestBuilder, jsonData);
            case MediaType.MULTIPART_FORM_DATA_VALUE -> multipartFormDataProcess(
                    (MockMultipartHttpServletRequestBuilder) mockHttpServletRequestBuilder
                    , jsonData
                    , fileUploadInfos
            );
        }
    }

    private void multipartFormDataProcess(
            MockMultipartHttpServletRequestBuilder mockHttpServletRequestBuilder
            , String jsonData
            , FileUploadInfo[] fileUploadInfos
    ) throws JsonProcessingException {
        Arrays.stream(fileUploadInfos)
                .forEach((fileUploadInfo) ->
                        mockHttpServletRequestBuilder
                                .file(getMultipartFile(fileUploadInfo)));
        formDataProcess(mockHttpServletRequestBuilder, jsonData);
    }

    private static void jsonDataProcess(
            MockHttpServletRequestBuilder mockHttpServletRequestBuilder, String jsonData) {
        mockHttpServletRequestBuilder.content(jsonData);
    }

    private static void formDataProcess(
            MockHttpServletRequestBuilder mockHttpServletRequestBuilder, String jsonData)
            throws JsonProcessingException {
        val mapForList = OBJECT_MAPPER.readValue(jsonData,
                new TypeReference<Map<String, List<String>>>() {
                });
        mapForList.forEach((key, value) ->
                mockHttpServletRequestBuilder
                        .param(key, value.toArray(String[]::new))
        );
    }

    private static String getDefaultTestDataPath(MvcTestInfo mvcTestInfo) {
        return String.format(
                ROOT_TEST_DATA_PATH + "/%s/%s/%s"
                , mvcTestInfo.simpleClassName()
                , mvcTestInfo.methodName()
                , RQ_DATA_FILE_NAME
        );
    }

    @SneakyThrows
    private static MvcTestInfo getMvcInfo(
            Class<? extends BaseMvcTest> thisClass
            , String methodName
    ) {
        return Arrays.stream(thisClass.getMethods())
                .filter((method) -> method.getName().equals(methodName))
                .map((method) -> method.getAnnotation(MvcInfo.class))
                .filter(Objects::nonNull)
                .findFirst()
                .map(mvcInfo ->
                        new MvcTestInfo(
                                methodName
                                , thisClass.getSimpleName()
                                , mvcInfo
                        )
                )
                .orElse(null);
    }

    @SneakyThrows
    private static boolean isTestClass(
            Class<? extends BaseMvcTest> thisClass, StackTraceElement stackTraceElement) {
        return thisClass.getName().equals(stackTraceElement.getClassName());
    }


}
