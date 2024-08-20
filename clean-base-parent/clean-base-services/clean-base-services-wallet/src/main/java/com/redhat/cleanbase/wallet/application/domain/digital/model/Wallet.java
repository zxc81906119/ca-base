package com.redhat.cleanbase.wallet.application.domain.digital.model;

import com.redhat.cleanbase.ca.entity.DomainEntity;
import com.redhat.cleanbase.ca.share.digital_voucher_digital_wallet.entity.Voucher;

import java.util.Set;

public class Wallet extends DomainEntity<Long> {
    private Set<Voucher> vouchers;
}
