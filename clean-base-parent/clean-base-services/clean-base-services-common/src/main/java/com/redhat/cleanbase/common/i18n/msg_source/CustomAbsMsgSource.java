package com.redhat.cleanbase.common.i18n.msg_source;

import com.redhat.cleanbase.common.i18n.msg_source.input.I18nInput;
import lombok.val;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.Locale;

public abstract class CustomAbsMsgSource extends AbstractMessageSource implements CustomMsgSource {

    @Override
    public final String getMessage(I18nInput input, boolean forceDefaultNull, Locale locale) {
        val msg = getMessageInternal(input, locale);
        if (msg != null) {
            return msg;
        }
        if (forceDefaultNull) {
            return null;
        }
        val defaultMessage = input.getDefaultMessage();
        if (defaultMessage == null) {
            return getDefaultMessage(input.getCode());
        }
        return renderDefaultMessage(defaultMessage, input.getArguments(), locale);
    }

    @Nullable
    protected String resolveCodeWithoutArguments(@Nullable I18nInput input, Locale locale) {
        val messageFormat = resolveCode(input, locale);
        if (messageFormat != null) {
            synchronized (messageFormat) {
                return messageFormat.format(new Object[0]);
            }
        }
        return null;
    }

    @Nullable
    protected String getMessageFromParent(I18nInput input, @Nullable Object[] args, Locale locale) {
        val parent = getParentMessageSource();
        if (parent != null) {

            if (parent instanceof CustomAbsMsgSource customAbsMsgSource) {
                return customAbsMsgSource.getMessageInternal(input, locale);
            }

            if (parent instanceof AbstractMessageSource) {
                // todo 因為存取修飾符是 protected , 所以調用不到
                // todo 且不想使用反射去拿, 因此拋不支援之例外
                throw new UnsupportedOperationException();
            }

            return parent.getMessage(input.getCode(), args, input.getDefaultMessage(), locale);
        }
        // Not found in parent either.
        return null;
    }

    @Nullable
    public String getMessageInternal(@Nullable I18nInput input, @Nullable Locale locale) {
        if (input == null) {
            return null;
        }

        if (locale == null) {
            locale = Locale.getDefault();
        }

        Object[] argsToUse = input.getArguments();

        if (!isAlwaysUseMessageFormat() && ObjectUtils.isEmpty(argsToUse)) {
            // Optimized resolution: no arguments to apply,
            // therefore no MessageFormat needs to be involved.
            // Note that the default implementation still uses MessageFormat;
            // this can be overridden in specific subclasses.
            val message = resolveCodeWithoutArguments(input, locale);
            if (message != null) {
                return message;
            }
        } else {
            // Resolve arguments eagerly, for the case where the message
            // is defined in a parent MessageSource but resolvable arguments
            // are defined in the child MessageSource.
            argsToUse = resolveArguments(argsToUse, locale);

            val messageFormat = resolveCode(input, locale);
            if (messageFormat != null) {
                synchronized (messageFormat) {
                    return messageFormat.format(argsToUse);
                }
            }
        }

        // Check locale-independent common messages for the given message code.
        val commonMessages = getCommonMessages();
        if (commonMessages != null) {
            val commonMessage = commonMessages.getProperty(input.getCode());
            if (commonMessage != null) {
                return formatMessage(commonMessage, argsToUse, locale);
            }
        }

        // Not found -> check parent, if any.
        return getMessageFromParent(input, argsToUse, locale);
    }

    protected abstract MessageFormat resolveCode(I18nInput input, Locale locale);

    protected MessageFormat resolveCode(String code, Locale locale) {
        // todo 直接拋出例外,如要用,請覆寫
        throw new UnsupportedOperationException();
    }
}
