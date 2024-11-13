package com.redhat.cleanbase.batch.param.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class ParamValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null || parameters.isEmpty()) {
            throw new JobParametersInvalidException("無任何參數不給過");
        }
        System.out.println("驗證成功");
    }
}