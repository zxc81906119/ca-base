package com.redhat.cleanbase.adapter.out.persistence;

import com.redhat.cleanbase.adapter.out.persistence.repository.ActivityRepository;
import com.redhat.cleanbase.adapter.out.persistence.repository.SpringDataAccountRepository;
import com.redhat.cleanbase.adapter.out.persistence.repository.converter.AccountConverter;
import com.redhat.cleanbase.application.domain.model.AccountDo;
import com.redhat.cleanbase.application.domain.model.AccountDo.AccountId;
import com.redhat.cleanbase.application.domain.model.ActivityDo;
import com.redhat.cleanbase.application.port.out.LoadAccountPort;
import com.redhat.cleanbase.application.port.out.UpdateAccountStatePort;
import com.redhat.cleanbase.common.exception.DbDataNotFoundException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Observed
public class AccountPersistenceAdapter implements
        LoadAccountPort,
        UpdateAccountStatePort {

    private final SpringDataAccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final AccountConverter accountConverter;

    @Override
    public AccountDo loadAccount(
            AccountId accountId,
            LocalDateTime baselineDate) {

        val account =
                accountRepository.findById(accountId.value())
                        .orElseThrow(DbDataNotFoundException::new);

        val activities =
                activityRepository.findByOwnerSince(
                        accountId.value(),
                        baselineDate);

        val withdrawalBalance = activityRepository
                .getWithdrawalBalanceUntil(
                        accountId.value(),
                        baselineDate)
                .orElse(0L);

        val depositBalance = activityRepository
                .getDepositBalanceUntil(
                        accountId.value(),
                        baselineDate)
                .orElse(0L);

        return accountConverter.mapToDomainEntity(
                account,
                activities,
                withdrawalBalance,
                depositBalance);

    }

    @Override
    public void updateActivities(AccountDo accountDo) {
        for (ActivityDo activity : accountDo.getActivityWindowVo().activities()) {
            if (activity.getId() == null) {
                activityRepository.save(accountConverter.mapToJpaEntity(activity));
            }
        }
    }

}
