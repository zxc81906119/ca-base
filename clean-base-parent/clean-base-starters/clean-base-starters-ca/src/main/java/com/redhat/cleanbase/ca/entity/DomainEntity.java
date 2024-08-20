package com.redhat.cleanbase.ca.entity;


import com.redhat.cleanbase.ca.vo.IdValueObject;

public abstract class DomainEntity<ID> implements BaseDomainEntity<ID> {

    private IdValueObject<ID> idVo;

    @Override
    public IdValueObject<ID> getIdVo() {
        return idVo;
    }

    @Override
    public void setIdVo(IdValueObject<ID> idVo) {
        if (this.idVo == null) {
            throw new IllegalStateException("id can't change");
        }
        this.idVo = idVo;
    }
}