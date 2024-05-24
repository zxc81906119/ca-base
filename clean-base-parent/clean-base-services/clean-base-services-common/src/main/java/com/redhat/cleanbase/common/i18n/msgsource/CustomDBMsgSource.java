package com.redhat.cleanbase.common.i18n.msgsource;

import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Locale;

public class CustomDBMsgSource extends AbstractMessageSource {
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        // code 是 key , 可以搭配階層(比如 type.i18nKey)
        // 將 type 和 i18n key 抓出當欄位查詢
        // locale 當作 table 加工名稱 比如 i18n_${locale},或當作 i18n table 欄位
        // 抓出來的 i18n 值
        // 未啥還要放 locale ?
        // 因為他 format 的 格式會因為 locale 而有不同
        // 如果沒有 arg 那就沒差
        return new MessageFormat("CustomDBMsgSource", locale);
    }
}
