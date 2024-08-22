package com.redhat.cleanbase.ca.entity;


import com.redhat.cleanbase.ca.vo.IdValueObject;

public abstract class DomainEntity<IdVO extends IdValueObject<?>> implements BaseDomainEntity<IdVO> {

    protected IdVO idVO;

    @Override
    public IdVO getIdVO() {
        return idVO;
    }

    @Override
    public void setIdVO(IdVO idVO) {
        if (idVO == null) {
            return;
        }

        if (this.idVO != null) {
            throw new IllegalStateException("idVO can't change");
        }

        this.idVO = idVO;
    }
}