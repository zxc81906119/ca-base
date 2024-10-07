package com.redhat.cleanbase.ddd.entity;


import com.redhat.cleanbase.ddd.vo.IdValueObject;
import lombok.Data;

@Data
public abstract class DomainObject<IdVO extends IdValueObject<?>> implements BaseDomainObject<IdVO> {

    private IdVO idVO;

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