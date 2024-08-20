package com.redhat.cleanbase.ca.entity;


import com.redhat.cleanbase.ca.vo.IdVo;

public abstract class DomainEntity<ID> implements BaseDomainEntity<ID> {
    private IdVo<ID> idVo;

    @Override
    public IdVo<ID> getIdVo() {
        return idVo;
    }

    @Override
    public void setIdVo(IdVo<ID> idVo) {
        if (this.idVo == null) {
            throw new IllegalStateException("id can't change");
        }
        this.idVo = idVo;
    }
}