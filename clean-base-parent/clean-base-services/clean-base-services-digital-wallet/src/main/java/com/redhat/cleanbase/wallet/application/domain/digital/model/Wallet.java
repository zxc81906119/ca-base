package com.redhat.cleanbase.wallet.application.domain.digital.model;

import com.redhat.cleanbase.ca.entity.AbstractDomainEntity;
import com.redhat.cleanbase.ca.entity.Voucher;

import java.util.Set;

public class Wallet extends AbstractDomainEntity<Long> {
    private Set<Voucher> vouchers;
}
