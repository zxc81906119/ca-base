package com.redhat.cleanbase.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * <pre>
 * HSM Service
 * </pre>
 *
 * @author Sunkist
 * @version
 *          <ul>
 *          <li>2024年6月14日,Sunkist,new
 *          </ul>
 * @since 2024年6月14日
 */
@Slf4j
public class HsmUtil {

    public static final String DIVDATA_TWMP_PADDING = "00000000000000000";
    public static final String DIVDATA_FISC_PADDING = "00000";
    public static final String DEFAULT_GCM_KEY_WRAP_ALGORITM = "A256GCMKW";
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String DEFAULT_KEYSTORE_TYPE = "JCEKS";
    public static final String DEFAULT_ALGORITHM = "AES";
    public static final String DEFAULT_FULL_ALGORITM = "AES/GCM/NoPadding";
    public static final byte[] DEFAULT_IV_LOWVALUE = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00 };

    public static final String MAC_PROXY_ENCRYPT_MECHANISM = "3DES";
    public static final String MAC_PROXY_WEBATM_AUTH_KEY = "NetAtm3";
    public static final String MAC_PROXY_WEBATM_AUTH_KEY_ENCRYPT_MECHANISM = "3DES_CBC_PAD";
    public static final String MAC_PROXY_FIXED_DATETIME = "00000000000000";
    public static final String MAC_PROXY_IV_VALUE = "0000000000000000";

    /**
     * <ol>
     * <li>TWMP
     * <li>FISC
     * </ol>
     */
    public enum MasterKeyEnum
    {
        TWMP, FISC;
    }

    // keystore path
    @Value("classpath:${adm.aes.key.store.path:}")
    private Resource keyStoreResource;

    // keystore file path
    @Value("${adm.aes.key.store.path:}")
    private String keyStoreFilePath;

    // store pass Specifies the pd required to access the keystore.
    @Value("${adm.aes.key.store.spd:}")
    private String sPd;

    // key pass Specifies the pd required to access the public-private
    // key pair.
    @Value("${adm.aes.key.store.kpd:}")
    private String kPd;

    @Value("${hsm.secretCode.privateTitle}")
    private String privateTitle;

    @Value("${hsm.secretCode.privateEnd}")
    private String privateEnd;

    @Value("${hsm.secretCode.privateTitle2}")
    private String privateTitle2;

    @Value("${hsm.secretCode.privateEnd2}")
    private String privateEnd2;

    @Value("${hsm.encoding.name}")
    private String encodingName;

    // keystore
    private static KeyStore keystore;

    public String getJwsString(String needToHashData,
                                      String txnPrivate) {

        /*
         * 簽章資訊
         */
        final String HEADER = "{\"typ\":\"jwt\",\"alg\":\"RS256\"}";
        final String RSA_PKCS1_PRIVATE_KEY = txnPrivate;

        final String SIGN_ALGORITHM = "SHA256withRSA";
        final String RSA_KEY_ALGROITHM = "RSA";
//        final int KEY_SIZE = 2048;

        // 經 sha256 再轉換成小寫 HexString ，如 ：
        // Base64Url(hexString.toLower(SHA256(data)))
        byte[] hashData = sha256Hash(needToHashData.getBytes(CHARSET));
        String hexString = HexUtils.bytesToHex(hashData).toLowerCase();

        // 移除結尾的 '=' 字符
        String payload = StringUtil
                .base64UrlEncodeString(hexString.getBytes(CHARSET))
                .replaceAll("=+$", "");

        // 進行 RS256(RSA PKCS#1 signature with SHA-256) 運算，產生訊息簽章
        // decode private key

        byte[] privateKeyBytes = java.util.Base64.getDecoder()
                .decode(RSA_PKCS1_PRIVATE_KEY
                        .replace(StringUtil.base64UrlDecodeString(privateTitle.getBytes()).toString(), "")
                        .replace(StringUtil.base64UrlDecodeString(privateEnd.getBytes()).toString(), "")
                        .replace(StringUtil.base64UrlDecodeString(privateTitle2.getBytes()).toString(), "")
                        .replace(StringUtil.base64UrlDecodeString(privateEnd2.getBytes()).toString(), "")
                        .replaceAll("\\s", "").replaceAll("=+$", ""));

        // prepare keySpec and keyFactory
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                    privateKeyBytes);
            KeyFactory keyFactory = KeyFactory
                    .getInstance(RSA_KEY_ALGROITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // init sign

            Signature signatureWithRsa = Signature
                    .getInstance(SIGN_ALGORITHM);
            signatureWithRsa.initSign(privateKey);

            signatureWithRsa.update((StringUtil.base64UrlEncodeString(
                    HEADER.getBytes(CHARSET)) + "." + payload)
                            .getBytes(CHARSET));

            byte[] signedData = signatureWithRsa.sign();
            String signature = StringUtil
                    .base64UrlEncodeString(signedData)
                    .replaceAll("=+$", "");

            log.debug(
                    "hexString.toLower(SHA256(data)): " + hexString);

            log.debug("Base64Url(hexString.toLower(SHA256(data))): "
                    + payload);

            log.debug("JWS: "
                    + StringUtil.base64UrlEncodeString(
                            HEADER.getBytes(CHARSET))
                    + "." + payload + "." + signature);

            return StringUtil
                    .base64UrlEncodeString(HEADER.getBytes(CHARSET))
                    + "." + payload + "." + signature;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException
                | SignatureException | InvalidKeyException e) {
            // todo
            return null;
//            throw new CustApException(
//                    CustApException.MyErrorCode.ERROR_SIGN_SIGNATURE,
//                    e);
        }
    }

    public byte[] sha256Hash(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest
                    .getInstance("SHA-256");
            return messageDigest.digest(bytes);
        } catch (NoSuchAlgorithmException a) {
            // todo 要拋出完整錯誤資訊
            return null;
//            throw new CustApException(
//                    CustApException.MyErrorCode.ERROR_SHA256, a);
        }
    }

    public KeyPair genTxnPublicAndPrivate() {
        final String RSA_KEY_ALGROITHM = "RSA";
        final int KEY_SIZE = 2048;
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance(RSA_KEY_ALGROITHM);
            keyGen.initialize(KEY_SIZE);
            KeyPair pair = keyGen.generateKeyPair();
            return pair;
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
