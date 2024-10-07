package com.redhat.cleanbase.jpa.mapper;

import com.redhat.cleanbase.jpa.po.BasePo;
import org.hibernate.Hibernate;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.mapstruct.Condition;

import java.util.Collection;

// ref: https://github.com/mapstruct/mapstruct/issues/2776#issuecomment-1055485208
public interface HibernateConditions {
    @Condition
    static boolean isCollectionAvailable(Collection<?> collection) {
        if (collection instanceof PersistentCollection<?> pc) {
            return pc.wasInitialized();
        }
        return true;
    }

    @Condition
    static boolean isHibernateProxyAvailable(BasePo<?> basePo) {
        if (basePo instanceof HibernateProxy hibernateProxy) {
            return Hibernate.isInitialized(hibernateProxy);
        }
        return true;
    }
}
