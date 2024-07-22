package com.redhat.cleanbase.account.adapter.out.persistence.repository;

import com.redhat.cleanbase.account.adapter.out.persistence.repository.model.ActivityPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityPo, Long> {

    @Query("""
            select a from ActivityPo a
            where a.ownerAccountId = :ownerAccountId\s
            and a.timestamp >= :since
            """)
    List<ActivityPo> findByOwnerSince(
            @Param("ownerAccountId") long ownerAccountId,
            @Param("since") LocalDateTime since);

    @Query("""
            select sum(a.amount) from ActivityPo a
            where a.targetAccountId = :accountId
            and a.ownerAccountId = :accountId
            and a.timestamp < :until
            """)
    Optional<Long> getDepositBalanceUntil(
            @Param("accountId") long accountId,
            @Param("until") LocalDateTime until);

    @Query("""
            select sum(a.amount) from ActivityPo a
            where a.sourceAccountId = :accountId
            and a.ownerAccountId = :accountId
            and a.timestamp < :until
            """)
    Optional<Long> getWithdrawalBalanceUntil(
            @Param("accountId") long accountId,
            @Param("until") LocalDateTime until);

}
