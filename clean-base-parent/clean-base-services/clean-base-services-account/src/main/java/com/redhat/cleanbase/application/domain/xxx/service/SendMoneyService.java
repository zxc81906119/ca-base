package com.redhat.cleanbase.application.domain.xxx.service;

import com.redhat.cleanbase.application.domain.xxx.model.MoneyVo;
import com.redhat.cleanbase.application.port.usecase.SendMoneyUseCase;
import com.redhat.cleanbase.application.port.usecase.model.SendMoneyCommand;
import com.redhat.cleanbase.application.port.out.xxx.AccountLockPort;
import com.redhat.cleanbase.application.port.out.xxx.LoadAccountPort;
import com.redhat.cleanbase.application.port.out.xxx.UpdateAccountStatePort;
import com.redhat.cleanbase.common.exception.AccountIdNotFoundException;
import com.redhat.cleanbase.common.exception.ThresholdExceededException;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class SendMoneyService implements SendMoneyUseCase {
    
    private final MoneyVo maximumTransferThreshold = MoneyVo.of(1_000_000L);

    private final UpdateAccountStatePort updateAccountStatePort;
    private final LoadAccountPort loadAccountPort;
    private final AccountLockPort accountLockPort;

    @Observed
    @Override
    public boolean sendMoney(SendMoneyCommand sendMoneyCommand) {
        // 檢查轉帳金額是否超過上限
        checkThreshold(sendMoneyCommand);
        // 十天前為基準日 , 這只是為了效能考量所定的一個邏輯
        val baselineDate = LocalDateTime.now().minusDays(10);
        // 依照 baselineDate 抓出來源帳戶資訊
        val sourceAccountDo = loadAccountPort.loadAccount(
                sendMoneyCommand.sourceAccountId(),
                baselineDate);
        // 依照 baselineDate 抓出目的帳戶資訊
        val targetAccountDo = loadAccountPort.loadAccount(
                sendMoneyCommand.targetAccountId(),
                baselineDate);
        // 檢查來源帳戶是否存在唯一標示
        val sourceAccountId = sourceAccountDo.getId()
                .orElseThrow(() -> new AccountIdNotFoundException("expected source account ID not to be empty"));
        // 檢查目的帳戶是否存在唯一標示
        val targetAccountId = targetAccountDo.getId()
                .orElseThrow(() -> new AccountIdNotFoundException("expected target account ID not to be empty"));
        // 鎖定來源帳戶
        accountLockPort.lockAccount(sourceAccountId);
        // 將來源帳戶物件進行扣款並增加一筆扣款至目的帳戶的紀錄
        if (!sourceAccountDo.withdraw(sendMoneyCommand.moneyVo(), targetAccountId)) {
            // 釋放來源帳戶鎖
            accountLockPort.releaseAccount(sourceAccountId);
            // 回傳轉帳失敗
            return false;
        }
        // 鎖定目的帳戶
        accountLockPort.lockAccount(targetAccountId);
        // 目的帳戶增加款項並增加一筆從來源帳戶增款的紀錄
        if (!targetAccountDo.deposit(sendMoneyCommand.moneyVo(), sourceAccountId)) {
            // 釋放來源帳戶鎖
            accountLockPort.releaseAccount(sourceAccountId);
            // 釋放目的帳戶鎖
            accountLockPort.releaseAccount(targetAccountId);
            return false;
        }
        // 將來源帳戶轉帳資訊進行更新
        updateAccountStatePort.updateActivities(sourceAccountDo);
        // 將目的帳戶轉帳資訊進行更新
        updateAccountStatePort.updateActivities(targetAccountDo);
        // 釋放來源帳戶鎖
        accountLockPort.releaseAccount(sourceAccountId);
        // 釋放目的帳戶鎖
        accountLockPort.releaseAccount(targetAccountId);
        // 回傳轉帳成功
        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        if (command.moneyVo().isGreaterThan(maximumTransferThreshold)) {
            throw new ThresholdExceededException(maximumTransferThreshold, command.moneyVo());
        }
    }

}




