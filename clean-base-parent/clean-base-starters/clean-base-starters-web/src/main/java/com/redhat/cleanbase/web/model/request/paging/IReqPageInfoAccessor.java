package com.redhat.cleanbase.web.model.request.paging;


public interface IReqPageInfoAccessor {

    void setPageable(Boolean pageable);

    Boolean getPageable();

    void setReqPageInfo(ReqPageInfo pageInfo);

    ReqPageInfo getReqPageInfo();
}
