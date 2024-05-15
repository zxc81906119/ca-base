package com.redhat.cleanbase.adapter.out.persistence.repository.converter;

import com.redhat.cleanbase.adapter.out.persistence.repository.model.AccountPo;
import com.redhat.cleanbase.adapter.out.persistence.repository.model.ActivityPo;
import com.redhat.cleanbase.application.domain.model.AccountDo;
import com.redhat.cleanbase.application.domain.model.AccountDo.AccountId;
import com.redhat.cleanbase.application.domain.model.ActivityDo;
import com.redhat.cleanbase.application.domain.model.ActivityWindowVo;
import com.redhat.cleanbase.application.domain.model.MoneyVo;
import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountConverter {

    public AccountDo mapToDomainEntity(
            AccountPo account,
            List<ActivityPo> activities,
            Long withdrawalBalance,
            Long depositBalance) {

        val baselineBalance = MoneyVo.subtract(
                MoneyVo.of(depositBalance),
                MoneyVo.of(withdrawalBalance));

        return AccountDo.withId(
                new AccountId(account.getId()),
                baselineBalance,
                mapToActivityWindow(activities));
    }

    public ActivityWindowVo mapToActivityWindow(List<ActivityPo> activities) {
        val activityVos = ActivityConverter.INSTANCE.posToVos(activities);
        return new ActivityWindowVo(activityVos);
    }

    public ActivityPo mapToJpaEntity(ActivityDo activityDo) {
        return ActivityConverter.INSTANCE.voToPo(activityDo);
    }

    @Mapper(
            unmappedSourcePolicy = ReportingPolicy.ERROR,
            unmappedTargetPolicy = ReportingPolicy.ERROR
    )
    public interface ActivityConverter {
        ActivityConverter INSTANCE = Mappers.getMapper(ActivityConverter.class);

        @Mapping(source = "id", target = "id.value")
        @Mapping(source = "ownerAccountId", target = "ownerAccountId.value")
        @Mapping(source = "sourceAccountId", target = "sourceAccountId.value")
        @Mapping(source = "targetAccountId", target = "targetAccountId.value")
        @Mapping(source = "amount", target = "moneyVo.amount")
        ActivityDo poToVo(ActivityPo activityPo);

        @Mapping(source = "id.value", target = "id")
        @Mapping(source = "ownerAccountId.value", target = "ownerAccountId")
        @Mapping(source = "sourceAccountId.value", target = "sourceAccountId")
        @Mapping(source = "targetAccountId.value", target = "targetAccountId")
        @Mapping(source = "moneyVo.amount", target = "amount")
        ActivityPo voToPo(ActivityDo activityDo);

        List<ActivityDo> posToVos(List<ActivityPo> activityPos);

    }

}
