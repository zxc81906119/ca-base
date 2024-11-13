package com.redhat.cleanbase.convert.json.model.base;

import com.redhat.cleanbase.common.wrapper.Wrapper;

import java.util.Map;

/**
 * @author ming
 */
public interface BaseMapWrapper extends Wrapper<Map<String, Object>> {

    @Override
    default Map<String, Object> unwrap() {
        return null;
    }

}