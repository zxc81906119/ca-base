package com.redhat.cleanbase.account.application.domain.model;

import lombok.NonNull;

import java.math.BigInteger;

public record MoneyVo(@NonNull BigInteger amount) {

    public static MoneyVo ZERO = MoneyVo.of(0L);

    public boolean isPositiveOrZero() {
        return this.amount.compareTo(BigInteger.ZERO) >= 0;
    }

    public boolean isGreaterThan(MoneyVo moneyVo) {
        return this.amount.compareTo(moneyVo.amount) >= 1;
    }

    public static MoneyVo of(long value) {
        return new MoneyVo(BigInteger.valueOf(value));
    }

    public static MoneyVo add(MoneyVo a, MoneyVo b) {
        return new MoneyVo(a.amount.add(b.amount));
    }

    public static MoneyVo subtract(MoneyVo a, MoneyVo b) {
        return new MoneyVo(a.amount.subtract(b.amount));
    }

    public MoneyVo negate() {
        return new MoneyVo(this.amount.negate());
    }

}
