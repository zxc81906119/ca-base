package com.redhat.cleanbase.ca.entity;

import com.redhat.cleanbase.ca.vo.IdValueObject;

public interface BaseDomainEntity<IdVO extends IdValueObject<?>> {
    IdVO getIdVO();

    void setIdVO(IdVO idVO);
}