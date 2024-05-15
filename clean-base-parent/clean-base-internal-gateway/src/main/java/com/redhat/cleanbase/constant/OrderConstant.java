package com.redhat.cleanbase.constant;

import org.springframework.core.Ordered;

public final class OrderConstant {
    private OrderConstant() {
        throw new UnsupportedOperationException();
    }

    public static final int VERIFY_DEVICE_ID_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;
    // api 是否可以使用, 查詢 db or redis
    // 目前照舊 spring data jpa 搭配連線池
    // 未來效能可以考慮用 spring data r2dbc
    public static final int API_SWITCH_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;
    // pilot 環境忽略 db flag 開關,用 spring profile 判定
    public static final int PILOT_IGNORE_SWITCH_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;
    // 檢查 tid 是否重複, 使用 redis ,成功後將 tid 放入 span 中 供 tracing
    public static final int BLOCK_REPEAT_REQUEST_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    // 檢查 user 是否登入,從 redis 抓出 user 資訊, 或是打後端 api 知道 user 資訊
    public static final int CHECK_LOGIN_STATUS_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    // 由於 app 自行串 oauth2 拿 token , 所以這邊只要確定傳入 token 是否合法
    public static final int VERIFY_OAUTH2_ACCESS_TOKEN_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    // (作法1) 改變傳入 bff 之 url
    // (作法2) 將 request header 帶入 api 版本
    public static final int MAPPING_APP_TO_BFF_API_VERSION_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    // 將 request body 換成解密後,所以需要去做解密動作
    public static final int DECRYPT_REQUEST_BODY_DATA_FILTER_ORDER_ORDER = Ordered.HIGHEST_PRECEDENCE;

}
