package com.redhat.cleanbase.account.adapter.out.persistence.repository;

import com.redhat.cleanbase.account.adapter.out.persistence.repository.model.AccountPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataAccountRepository extends JpaRepository<AccountPo, Long> {
}
