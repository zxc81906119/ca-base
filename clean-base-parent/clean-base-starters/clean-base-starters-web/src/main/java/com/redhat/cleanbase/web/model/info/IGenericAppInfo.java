package com.redhat.cleanbase.web.model.info;

import java.util.Date;

public interface IGenericAppInfo {

    Date getStartTime();

    void setStartTime(Date startTime);

    Date getEndTime();

    void setEndTime(Date endTime);

    long getExecTimeMillis();

    void setExecTimeMillis(long execTimeMillis);
}