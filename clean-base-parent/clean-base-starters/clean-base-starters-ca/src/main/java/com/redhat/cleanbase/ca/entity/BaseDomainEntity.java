package com.redhat.cleanbase.ca.entity;

import com.redhat.cleanbase.ca.vo.IdValueObject;

public interface BaseDomainEntity<ID> {
    IdValueObject<ID> getIdVo();

    void setIdVo(IdValueObject<ID> id);
}