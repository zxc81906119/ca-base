package com.redhat.cleanbase.web.model.info.impl;

import com.redhat.cleanbase.web.model.info.IGenericAppInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GenericAppInfo implements IGenericAppInfo {
    private Date startTime;

    private Date endTime;

    private long execTimeMillis;
}
