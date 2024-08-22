package com.redhat.cleanbase.ddd.entity;


import com.redhat.cleanbase.ddd.vo.IdValueObject;

public abstract class DomainEntity<IdVO extends IdValueObject<?>> implements BaseDomainEntity<IdVO> {

    private IdVO idVO;

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