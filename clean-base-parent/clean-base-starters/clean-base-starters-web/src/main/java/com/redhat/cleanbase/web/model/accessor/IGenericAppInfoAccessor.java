package com.redhat.cleanbase.web.model.accessor;

import com.redhat.cleanbase.web.model.info.IGenericAppInfo;

public interface IGenericAppInfoAccessor<G extends IGenericAppInfo> {
    G getGenericAppInfo();

    void setGenericAppInfo(G g);

    G newGenericAppInfo();

    default G findGenericAppInfoOrNew() {
        G genericAppInfo = getGenericAppInfo();
        if (genericAppInfo == null) {
            setGenericAppInfo(genericAppInfo = newGenericAppInfo());
        }
        return genericAppInfo;
    }
}
