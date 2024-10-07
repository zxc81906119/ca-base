package com.redhat.cleanbase.ddd.entity;

import com.redhat.cleanbase.ddd.vo.IdValueObject;

public abstract class AggregateRoot<IdVO extends IdValueObject<?>> extends DomainObject<IdVO> {
}
