package com.redhat.cleanbase.ddd.entity;

import com.redhat.cleanbase.ddd.vo.IdValueObject;

public interface BaseDomainObject<IdVO extends IdValueObject<?>> {
    IdVO getIdVO();

    void setIdVO(IdVO idVO);
}