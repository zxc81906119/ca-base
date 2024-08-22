package com.redhat.cleanbase.account.application.domain.model;

import com.redhat.cleanbase.ddd.entity.DomainEntity;
import com.redhat.cleanbase.ddd.vo.IdValueObject;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class AccountDo extends DomainEntity<AccountDo.AccountId> {

    private AccountDo(AccountId accountId, MoneyVo baselineBalance, ActivityWindowVo activityWindowVo) {
        setIdVO(accountId);
        this.baselineBalance = baselineBalance;
        this.activityWindowVo = activityWindowVo;
    }

    private final MoneyVo baselineBalance;

    private final ActivityWindowVo activityWindowVo;

    public static AccountDo withId(
            AccountId accountId,
            MoneyVo baselineBalance,
            ActivityWindowVo activityWindowVo
    ) {
        return new AccountDo(accountId, baselineBalance, activityWindowVo);
    }

    public Optional<AccountId> getId() {
        return Optional.ofNullable(this.getIdVO());
    }

    public MoneyVo calculateBalance() {
        return MoneyVo.add(
                this.baselineBalance,
                this.activityWindowVo.calculateBalance(this.getIdVO())
        );
    }

    public boolean withdraw(MoneyVo moneyVo, AccountId targetAccountId) {

        if (!mayWithdraw(moneyVo)) {
            return false;
        }

        val withdrawal = new ActivityDo(
                this.getIdVO(),
                this.getIdVO(),
                targetAccountId,
                LocalDateTime.now(),
                moneyVo
        );

        this.activityWindowVo.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(MoneyVo moneyVo) {
        return MoneyVo.add(
                        this.calculateBalance(),
                        moneyVo.negate()
                )
                .isPositiveOrZero();
    }

    public boolean deposit(MoneyVo moneyVo, AccountId sourceAccountId) {
        val deposit = new ActivityDo(
                this.getIdVO(),
                sourceAccountId,
                this.getIdVO(),
                LocalDateTime.now(),
                moneyVo
        );
        this.activityWindowVo.addActivity(deposit);
        return true;
    }

    @SuperBuilder
    public static class AccountId extends IdValueObject<Long> {
    }

}
