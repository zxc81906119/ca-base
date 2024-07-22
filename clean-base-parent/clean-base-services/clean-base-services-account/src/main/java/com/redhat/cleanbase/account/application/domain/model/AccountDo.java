package com.redhat.cleanbase.account.application.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * An account that holds a certain amount of money. An {@link AccountDo} object only
 * contains a window of the latest account activities. The total balance of the account is
 * the sum of a baseline balance that was valid before the first activity in the
 * window and the sum of the activity values.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountDo {

    /**
     * The unique ID of the account.
     */
    private final AccountId id;

    /**
     * The baseline balance of the account. This was the balance of the account before the first
     * activity in the activityWindow.
     */
    @Getter
    private final MoneyVo baselineBalance;

    /**
     * The window of latest activities on this account.
     */
    @Getter
    private final ActivityWindowVo activityWindowVo;

    /**
     * Creates an {@link AccountDo} entity with an ID. Use to reconstitute a persisted entity.
     */
    public static AccountDo withId(
            AccountId accountId,
            MoneyVo baselineBalance,
            ActivityWindowVo activityWindowVo) {
        return new AccountDo(accountId, baselineBalance, activityWindowVo);
    }

    public Optional<AccountId> getId() {
        return Optional.ofNullable(this.id);
    }

    /**
     * Calculates the total balance of the account by adding the activity values to the baseline balance.
     */
    public MoneyVo calculateBalance() {
        return MoneyVo.add(
                this.baselineBalance,
                this.activityWindowVo.calculateBalance(this.id));
    }

    /**
     * Tries to withdraw a certain amount of money from this account.
     * If successful, creates a new activity with a negative value.
     *
     * @return true if the withdrawal was successful, false if not.
     */
    public boolean withdraw(MoneyVo moneyVo, AccountId targetAccountId) {

        if (!mayWithdraw(moneyVo)) {
            return false;
        }

        val withdrawal = new ActivityDo(
                this.id,
                this.id,
                targetAccountId,
                LocalDateTime.now(),
                moneyVo);
        this.activityWindowVo.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(MoneyVo moneyVo) {
        return MoneyVo.add(
                        this.calculateBalance(),
                        moneyVo.negate())
                .isPositiveOrZero();
    }

    /**
     * Tries to deposit a certain amount of money to this account.
     * If successful, creates a new activity with a positive value.
     *
     * @return true if the deposit was successful, false if not.
     */
    public boolean deposit(MoneyVo moneyVo, AccountId sourceAccountId) {
        val deposit = new ActivityDo(
                this.id,
                sourceAccountId,
                this.id,
                LocalDateTime.now(),
                moneyVo);
        this.activityWindowVo.addActivity(deposit);
        return true;
    }

    public record AccountId(@NonNull Long value) {
    }

}
