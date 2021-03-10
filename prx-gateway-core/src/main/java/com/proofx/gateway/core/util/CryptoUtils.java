package com.proofx.gateway.core.util;

import com.proofx.gateway.api.v1.model.ServiceRuntimeException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Crypto utility class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class CryptoUtils {
    private CryptoUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * calculate hmac sha256 hash
     *
     * @param secretKey byte array
     * @param message to hash byte array
     * @return hash as byte array
     */
    public static byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new ServiceRuntimeException(INTERNAL_SERVER_ERROR);
        }
        return hmacSha256;
    }
}
