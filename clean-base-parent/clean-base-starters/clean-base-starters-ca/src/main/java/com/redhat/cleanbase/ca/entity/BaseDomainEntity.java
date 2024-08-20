package com.redhat.cleanbase.ca.entity;

import com.redhat.cleanbase.ca.vo.IdVo;

public interface BaseDomainEntity<ID> {
    IdVo<ID> getIdVo();

    void setIdVo(IdVo<ID> id);
}