package com.lzb;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import static com.lzb.WorldFirstApi.*;

/**
 * 签名<br/>
 * Created on : 2023-09-24 14:43
 * @author lizebin
 */
@Getter
@Builder(toBuilder = true)
public class Signature {

    /**
     * name and value separator
     */
    private static final String NAME_VALUE_SEPARATOR = "=";

    /**
     * comma
     */
    private static final String COMMA = ",";

    /**
     * algorithm
     */
    private static final String ALGORITHM = "algorithm";

    /**
     * signature
     */
    private static final String SIGNATURE = "signature";

    /**
     * keyVersion
     */
    private static final String KEY_VERSION = "keyVersion";

    /**
     * RSA256
     */
    private static final String RSA_256 = "RSA256";

    /**
     * 验签公钥
     */
    @Builder.Default
    private String verifyPublicKey = WorldFirstApi.WORLD_FIRST_PUBLIC_KEY;

    @NonNull
    private String httpMethod;

    @NonNull
    private String uriQueryString;

    @NonNull
    private String requestBody;


    String sign(String requestTime) throws Exception {
        String reqContent = httpMethod + " " + uriQueryString + "\n" + CLIENT_ID + "." + requestTime + "." + requestBody;
        System.out.println("reqContent is " + "\n" + reqContent);

        String originalString = signWithSHA256RSA(reqContent, CIDER_PRIVATE_KEY);

        return URLEncoder.encode(originalString, StandardCharsets.UTF_8);
    }

    public String signatureHeaderPayload(String requestTime) throws Exception {
        return ALGORITHM +
                NAME_VALUE_SEPARATOR +
                RSA_256 +
                COMMA +
                KEY_VERSION +
                NAME_VALUE_SEPARATOR +
                WorldFirstApi.KEY_VERSION +
                COMMA +
                SIGNATURE +
                NAME_VALUE_SEPARATOR +
                sign(requestTime);
    }

    /**
     * Generate base64 encoded signature using the sender's private key
     *
     * @param reqContent:    the original content to be signed by the sender
     * @param strPrivateKey: the private key which should be base64 encoded
     */
    private static String signWithSHA256RSA(String reqContent, String strPrivateKey) throws Exception {
        java.security.Signature privateSignature = java.security.Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(getPrivateKeyFromBase64String(strPrivateKey));
        privateSignature.update(reqContent.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = privateSignature.sign();

        return Base64.getEncoder().encodeToString(bytes);
    }

    private static PrivateKey getPrivateKeyFromBase64String(String privateKeyString) throws Exception {
        byte[] b1 = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private static boolean verifySignatureWithSHA256RSA(String rspContent, String signature, String strPk) throws Exception {
        PublicKey publicKey = getPublicKeyFromBase64String(strPk);

        java.security.Signature publicSignature = java.security.Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(rspContent.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }


    private static PublicKey getPublicKeyFromBase64String(String publicKeyString) throws Exception {
        byte[] b1 = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(X509publicKey);
    }

    /**
     * Check the response of WorldFirst
     *
     * @param httpMethod         http method                  e.g., POST, GET
     * @param uriWithQueryString query string in url          e.g., if your request url is https://{domain_name}.com/ams/api/pay/query uriWithQueryString should be /ams/api/pay/query not https://{domain_name}.com/ams/api/pay/query
     * @param clientId           clientId issued by WorldFirst    e.g., *****
     * @param timeString         "response-time" in response  e.g., 2020-01-02T22:36:32-08:00
     * @param rspBody            json format response         e.g., "{"acquirerId":"xxx","refundAmount":{"currency":"CNY","value":"123"},"refundFromAmount":{"currency":"JPY","value":"234"},"refundId":"xxx","refundTime":"2020-01-03T14:36:32+08:00","result":{"resultCode":"SUCCESS","resultMessage":"success","resultStatus":"S"}}"
     * @param worldfirstPublicKey    public key from WorldFirst
     */
    public static boolean verify(
            String httpMethod,
            String uriWithQueryString,
            String timeString,
            String rspBody,
            String signature,
            String verifyPublicKey) throws Exception {
        // 1. construct the response content
        String responseContent = httpMethod + " " + uriWithQueryString + "\n" + CLIENT_ID + "." + timeString + "." + rspBody;

        // 2. decode the signature string
        String decodedString = URLDecoder.decode(signature, StandardCharsets.UTF_8);

        // 3. verify the response with WorldFirst's public key
        return verifySignatureWithSHA256RSA(responseContent, decodedString, verifyPublicKey);

    }

}