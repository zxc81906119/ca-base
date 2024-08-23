package com.redhat.cleanbase.account.application.domain.model;

import com.redhat.cleanbase.ddd.entity.DomainEntity;
import com.redhat.cleanbase.ddd.vo.IdValueObject;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value
public class ActivityDo extends DomainEntity<ActivityDo.ActivityIdVo> {

    @NonNull
    AccountDo.AccountId ownerAccountId;

    @NonNull
    AccountDo.AccountId sourceAccountId;

    @NonNull
    AccountDo.AccountId targetAccountId;

    @NonNull
    LocalDateTime timestamp;

    @NonNull
    MoneyVo moneyVo;

    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Data
    @SuperBuilder
    public static class ActivityIdVo extends IdValueObject<Long> {
    }

}
