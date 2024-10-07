package com.redhat.cleanbase.ddd.mapper;

import com.redhat.cleanbase.ddd.vo.IdValueObject;

public interface IdIdVOConverter<Id, IdVO extends IdValueObject<Id>> {
    default Id toId(IdVO idVO) {
        return idVO == null ?
                null
                : idVO.getValue();
    }

    default IdVO toIdVO(Id id) {
        throw new UnsupportedOperationException("請自行實做轉換!");
    }

}
