package com.redhat.cleanbase.convert.parser;

import com.redhat.cleanbase.common.func.Operator;
import lombok.val;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class JsonParser<D, SELF extends JsonParser<D, SELF>> {

    protected final D delegate;

    public JsonParser(D delegate) {
        this.delegate = Objects.requireNonNull(
                Optional.ofNullable(delegate)
                        .orElseGet(this::createJsonParserDelegate),
                "需要提供 json delegate 供 parse");
    }

    public <O, E extends Exception> O operate(Operator<D, O, E> operator) throws E {
        return operator.operate(delegate);
    }

    public abstract <T> T parse(String json, Class<T> clazz) throws Exception;

    public abstract String toJson(Object o) throws Exception;

    protected abstract D createJsonParserDelegate();

    public SELF copy() {
        return this.copyWithCustomizer(null);
    }

    public SELF copyWithCustomizer(Consumer<D> consumer) {
        val copyDelegate = Objects.requireNonNull(copyDelegate(delegate));
        if (copyDelegate == delegate) {
            throw new IllegalStateException("不接受複製為相同物件");
        }

        if (consumer != null) {
            consumer.accept(copyDelegate);
        }

        val self = Objects.requireNonNull(createSelf(copyDelegate));
        if (self == this) {
            throw new IllegalStateException("不接受複製為相同物件");
        }

        return self;
    }

    protected abstract D copyDelegate(D originDelegate);

    protected abstract SELF createSelf(D delegate);

}
