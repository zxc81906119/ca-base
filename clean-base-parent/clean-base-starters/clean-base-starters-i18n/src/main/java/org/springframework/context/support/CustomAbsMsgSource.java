package org.springframework.context.support;

import com.redhat.cleanbase.i18n.msg_source.base.CustomMsgSource;
import com.redhat.cleanbase.i18n.msg_source.base.I18nMessageSource;
import com.redhat.cleanbase.i18n.msg_source.input.I18nInput;
import lombok.NonNull;
import lombok.val;
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
    protected String resolveCodeWithoutArguments(@NonNull I18nInput input, Locale locale) {
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
            if (parent instanceof I18nMessageSource i18nMessageSource) {
                return i18nMessageSource.getMessage(input, locale);
            }
            val code = input.getCode();
            if (parent instanceof AbstractMessageSource abstractMessageSource) {
                abstractMessageSource.getMessageInternal(code, args, locale);
            }
            return parent.getMessage(code, args, input.getDefaultMessage(), locale);
        }
        // Not found in parent either.
        return null;
    }

    @Nullable
    public String getMessageInternal(@NonNull I18nInput input, @Nullable Locale locale) {

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

    @Nullable
    protected abstract MessageFormat resolveCode(I18nInput input, Locale locale);
}
