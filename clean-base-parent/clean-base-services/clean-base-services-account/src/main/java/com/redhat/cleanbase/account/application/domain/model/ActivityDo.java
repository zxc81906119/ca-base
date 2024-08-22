package com.redhat.cleanbase.account.application.domain.model;

import com.redhat.cleanbase.common.mapstruct.constructor.Default;
import com.redhat.cleanbase.ddd.entity.DomainEntity;
import com.redhat.cleanbase.ddd.vo.IdValueObject;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
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

    public ActivityDo(
            @NonNull AccountDo.AccountId ownerAccountId,
            @NonNull AccountDo.AccountId sourceAccountId,
            @NonNull AccountDo.AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull MoneyVo moneyVo
    ) {
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.moneyVo = moneyVo;
    }

    @Default
    public ActivityDo(
            ActivityIdVo id,
            @NonNull AccountDo.AccountId ownerAccountId,
            @NonNull AccountDo.AccountId sourceAccountId,
            @NonNull AccountDo.AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull MoneyVo moneyVo
    ) {
        setIdVO(id);
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.moneyVo = moneyVo;
    }

    @SuperBuilder
    public static class ActivityIdVo extends IdValueObject<Long> {
    }

}
