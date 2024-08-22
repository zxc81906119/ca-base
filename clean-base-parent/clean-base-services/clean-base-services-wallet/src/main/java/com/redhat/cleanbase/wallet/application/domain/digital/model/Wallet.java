package com.redhat.cleanbase.wallet.application.domain.digital.model;

import com.redhat.cleanbase.ca.entity.DomainEntity;
import com.redhat.cleanbase.ca.share.digital_voucher_wallet.entity.Voucher;
import com.redhat.cleanbase.ca.vo.LongIdValueObject;

import java.util.Set;

public class Wallet extends DomainEntity<LongIdValueObject> {
    private Set<Voucher> vouchers;
}
