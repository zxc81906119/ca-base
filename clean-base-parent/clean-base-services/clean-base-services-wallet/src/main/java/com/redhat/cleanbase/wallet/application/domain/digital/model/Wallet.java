package com.redhat.cleanbase.wallet.application.domain.digital.model;

import com.redhat.cleanbase.ddd.entity.DomainEntity;
import com.redhat.cleanbase.ddd.share.digital_voucher_wallet.entity.Voucher;
import com.redhat.cleanbase.ddd.vo.LongIdValueObject;

import java.util.Set;

public class Wallet extends DomainEntity<LongIdValueObject> {
    private Set<Voucher> vouchers;
}
