package com.redhat.cleanbase.account.application.domain.model;

import lombok.NonNull;
import lombok.val;

import java.util.Collections;
import java.util.List;

/**
 * A window of account activities.
 *
 * @param activities The list of account activities within this window.
 */
public record ActivityWindowVo(@NonNull List<ActivityDo> activities) {

    /**
     * Calculates the balance by summing up the values of all activities within this window.
     */
    public MoneyVo calculateBalance(AccountDo.AccountId accountId) {
        val depositBalance = activities.stream()
                .filter(activityDo -> activityDo.getTargetAccountId().equals(accountId))
                .map(ActivityDo::getMoneyVo)
                .reduce(MoneyVo.ZERO, MoneyVo::add);

        val withdrawalBalance = activities.stream()
                .filter(activityDo -> activityDo.getSourceAccountId().equals(accountId))
                .map(ActivityDo::getMoneyVo)
                .reduce(MoneyVo.ZERO, MoneyVo::add);

        return MoneyVo.add(depositBalance, withdrawalBalance.negate());
    }

    @Override
    public List<ActivityDo> activities() {
        return Collections.unmodifiableList(this.activities);
    }

    public void addActivity(ActivityDo activity) {
        this.activities.add(activity);
    }
}
