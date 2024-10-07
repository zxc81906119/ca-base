package com.redhat.cleanbase.common.constants;

public interface ProjectConstants {

    public static final String TWMP_APP_ID = "27188ca2ab83443b911ff54d1e57f6f3";
    public static final String LDAP_DEFAULT_ROLE = "ROLE01";
    public static final int SESSION_TIME_OUT = 600;


    public static final String REGISTRY_SOURCE = "SOURCE";
    
    public static final String GATEWAY = "Gateway";
    
    // TWMP KID
    public static final String TWMP_KID = "B004DC00YDKACDO";//"ee1";

    // FISC KID
    public static final String FISC_KID = "B004DC00YDKACDO";//"B006DC01YTKACDO";

    // SYSTYPE: ADM
    public static final String ADM_SYS_TYPE = "ADM";

    public static final String SYS_TYPE = ADM_SYS_TYPE;

    // APID
    // ADM
    public static final String ADM = "ADM";

    // 數位券 Digital Voucher
    public static final String DIGITAL_VOUCHER = "EPAY";

    // 交通碼
    public static final String TRAFFIC = "TRAFFIC";

    // 金融卡
    public static final String CARD = "CARD";

    // 紅利點數
    public static final String BONUX_POINT = "BP";

    // receive MQ timeout message
    public static final String RECEIVE_MQ_TIMEOUT_MSG = "EAI TIMEOUT";

    // gateway timeout message and code
    public static final String GATEWAY_TIMEOUT_CODE = "N1030";
    public static final String GATEWAY_TIMEOUT_MESSAGE = "系統忙線中，請稍候重新嘗試";
    
    // MQ timeout second
    public static final Integer DEFAULT_MQ_TIMEOUT = 30;
    // wait MQ response time million second
    public static final Integer DEFAULT_WAIT_MILLI_SECONDS = 30000;

    // EAI for GateWay Send
    public static final String CLIENTID = "NBP";
    public static final Short FROMCCSID = 1208;
    public static final Short TOCCSID = 1208;

    // FISC
    public static final String MTI_FISC_DEFAULT = "0000";
    public static final String MTI_FISC_SEND = "0900";
    public static final String MTI_FISC_RECEIVE = "0910";
    public static final String MTI_FISC_SEND_WALLET_COMBINE_TXN = "0200";
    public static final String MTI_FISC_RECEIVE_WALLET_COMBINE_TXN = "0210";
    public static final String MTI_FISC_RECEIVE_MESSAGE_NOTICES = "0800";
    public static final String MTI_FISC_SEND_MESSAGE_NOTICES = "0810";
    public static final String PRODESSING_CODE_FISC_DEFAULT = "000000";
    public static final String PRODESSING_CODE_FISC_WALLET_REGISTER_APPLY = "090101";
    public static final String PRODESSING_CODE_FISC_WALLET_DEVICE_BINDING = "090203";
    public static final String PRODESSING_CODE_FISC_WALLET_COMBINE_TXN = "020101";
    public static final String PRODESSING_CODE_FISC_WALLET_COMBINE_TXN_QUERY = "090106";
    public static final String PRODESSING_CODE_FISC_MESSAGE_NOTICES = "080201";
    public static final String PRODESSING_CODE_FISC_WALLET_BLACKLIST_ADD = "090104";
    public static final String PRODESSING_CODE_FISC_WALLET_BLACKLIST_MOVE = "090105";
    public static final String PRODESSING_CODE_FISC_VOUCHER_APPLY = "090301";
    public static final String PRODESSING_CODE_FISC_WALLET_PUBLIC_KEY_CHANGE = "090202";
    public static final String AGENCY_ID = "9501006";
    public static final String ISS_BANK = "0060000";
    public static final String TRACE_NUMBER_SEQ_NO_NAME = "FISC_TRACE_NUMBER";

    public static final String FISC_SRRN_TXN_CODE = "FISC_SRRN_TXN_CODE";

    public static final String WALLET_ID_SEQ_NO_NAME = "TWMP_WALLET_ID";
    public static final String EAI_TXN_ID_IMSCUQ_02 = "IMSCUQ02";
    public static final String EAI_TXN_ID_IMSACQ_20 = "IMSACQ20";
    public static final String EAI_TXN_ID_ECSCDQ_16 = "ECSCDQ16";
    public static final String EAI_TXN_ID_ECSCDQ_24 = "ECSCDQ24";
    public static final String EAI_TXN_ID_ATMAPT_50 = "ATMAPT50";
    public static final String EAI_TXN_ID_ATMAPT_51 = "ATMAPT51";
    public static final String WALLET_REFUND_URL = "/api/wallet/WalletRefund";
    public static final String REGISTER_WALLET_URL = "/api/wallet/WalletRegisterApply";
    public static final String TRADE_URL = "/api/wallet/WalletCombineTxn";
    public static final String WALLET_BLACK_LIST_ADD_URL = "/api/wallet/WalletBlackListAdd";
    public static final String WALLET_BLACK_LIST_MOVE_URL = "/api/wallet/WalletBlackListMove";
    public static final String VOUCHER_BINDING_URL = "/api/voucher/VoucherApply";

    // 錢包主檔為啟用狀態
    public static final String VOUCHER_WALLET_STATUS_ENABLE = "00";

    // 數位券交易成功
    public static final String VOUCHER_WALLET_TXN_SUCCESS = "01";
    // 數位券交易失敗
    public static final String VOUCHER_WALLET_TXN_FAIL = "00";

    // 交易記錄儲存發生例外
    public static final String VOUCHER_WALLET_TXN_SAVE_ERROR = "交易成功，但資料庫發生錯誤";
    
    // 查詢資料庫時發生例外
    public static final String DB_SEARCH_ERROR = "系統錯誤";

    // EAI電文失敗發生例外
    public static final String EAI_ERROR = "系統錯誤";
    
    // FISC電文失敗發生例外
    public static final String FISC_ERROR = "系統錯誤";

    /**
     * <ol>
     * <li>0:否
     * <li>1:是
     * </ol>
     */
    public enum EnableEnum {

        NO("0"),
        YES("1");

        private String code;

        EnableEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    // 電子軌跡記錄 begin
    public enum LOG_POINT {
        EPAY_TWMP("EPAY->TWMP"),
        TWMP_EPAY("TWMP->EPAY"),
        EPAY_FISC("EPAY->FISC"),
        FISC_EPAY("FISC->EPAY"),
        EPAY_EAI("EPAY->EAI"),
        EAI_EPAY("EAI->EPAY");

        private String code;

        LOG_POINT(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public enum SYS_RESULT {
        SUCCESS("Success"),
        FAIL("Fail");

        private String code;

        SYS_RESULT(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public enum LOG_TYPE_SYS {
        REQUEST("Request"),
        RESPONSE("Response");

        private String code;

        LOG_TYPE_SYS(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    // 電子軌跡記錄 end
}
