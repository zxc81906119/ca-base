package com.redhat.cleanbase.common.constants;

/**
 * <pre>
 * Header 常數
 * </pre>
 * 
 * @since 2022年12月27日
 * @author 1104309
 * @version
 *          <ul>
 *          <li>2022年12月27日,1104309,new
 *          </ul>
 */
public interface HeaderConstants {

    /** 交易代碼 */
    public static final String HEADER_FUNCTION_ID = "X-functonId";

    /** 交易細項代碼 */
    public static final String HEADER_TRANSACTION_ID = "X-transactionId";

    /** client app 執行/送出交易的流水號 */
    public static final String HEADER_REQUEST_TIME = "X-requestTime";

    /** client app 執行/送出交易的交易時間 */
    public static final String HEADER_TX_SEQ_NO = "X-txSeqNo";

}
