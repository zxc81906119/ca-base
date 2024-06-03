package com.redhat.cleanbase.application.domain.model;

import com.redhat.cleanbase.common.convert.Default;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * A money transfer activity between {@link AccountDo}s.
 */
@Value
public class ActivityDo {

    ActivityIdVo id;

    /**
     * The account that owns this activity.
     */
    @NonNull
    AccountDo.AccountId ownerAccountId;

    /**
     * The debited account.
     */
    @NonNull
    AccountDo.AccountId sourceAccountId;

    /**
     * The credited account.
     */
    @NonNull
    AccountDo.AccountId targetAccountId;

    /**
     * The timestamp of the activity.
     */
    @NonNull
    LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    @NonNull
    MoneyVo moneyVo;

    public ActivityDo(
            @NonNull AccountDo.AccountId ownerAccountId,
            @NonNull AccountDo.AccountId sourceAccountId,
            @NonNull AccountDo.AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull MoneyVo moneyVo) {
        this.id = null;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.moneyVo = moneyVo;
    }

    @Default
    public ActivityDo(ActivityIdVo id, @NonNull AccountDo.AccountId ownerAccountId, @NonNull AccountDo.AccountId sourceAccountId, @NonNull AccountDo.AccountId targetAccountId, @NonNull LocalDateTime timestamp, @NonNull MoneyVo moneyVo) {
        this.id = id;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.moneyVo = moneyVo;
    }

    public record ActivityIdVo(@NotNull Long value) {
    }

}
