package com.proofx.gateway.core;

import com.google.common.primitives.Bytes;
import com.proofx.gateway.api.v1.model.StatusResponse;
import com.proofx.gateway.core.model.TagEntity;
import com.proofx.gateway.core.util.TagUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import java.security.GeneralSecurityException;
import java.util.Arrays;


/**
 * Tag verification
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
public class DefaultTagService {
    private static final int CMAC_SIZE = 16;

    protected byte[] generateSubKey(byte[] key) {

        byte[] subKey = TagUtils.shiftLeft(key);
        int msbL = (key[0] & 0xff) >> 7;
        if (msbL == 1) {
            subKey[15] = (byte) (subKey[15] ^ (byte) 0x87);
        }

        return subKey;
    }

    protected byte[] diversifyCMAC(byte[] mac) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(mac, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[CMAC_SIZE]));

        // Generate SubKeys
        // L = CIPHK(0b)
        byte[] l = cipher.doFinal(new byte[CMAC_SIZE]);
        // If MSB1(L) = 0, then K1 = L << 1 Else K1 = (L << 1) XOR Rb
        byte[] k1 = generateSubKey(l);
        // If MSB1(K1) = 0, then K2 = K1 << 1 Else K2 = (K1 << 1) XOR Rb
        byte[] k2 = generateSubKey(k1);

        byte[] div = mac;
        byte[] finalDiv = div;
        boolean padded = false;
        if (finalDiv.length != 32) {
            finalDiv = TagUtils.pad(Bytes.concat(div, new byte[]{(byte) 0x80}), 32);
            padded = true;
        }

        /**
         * Last 16-byte is XORed with K2 if padding is added, otherwise XORed with K1
         */
        byte[] inputLSB = Arrays.copyOfRange(finalDiv, 16, 32);

        if (padded) {
            inputLSB = TagUtils.xor(inputLSB, k2);
        } else {
            inputLSB = TagUtils.xor(inputLSB, k1);
        }
        return cipher.doFinal(inputLSB);
    }


    protected boolean verify(byte[] uuid, byte[] counter, byte[] cmacMirror) {
        TagEntity tagEntity = PanacheEntityBase.find("chipUUID", TagUtils.bytesToString(uuid)).firstResult();
        byte[] key = TagUtils.stringToBytes(tagEntity.getSecretKey());
        byte[] prefix = {0x3C, (byte)0xC3, 0x00, 0x01, 0x00, (byte)0x80};
        byte[] valueToMAC = Bytes.concat(prefix, Bytes.concat(uuid, counter));
        try {
            BlockCipher blockCipher = new AESEngine();
            Mac cmac = new CMac(blockCipher, CMAC_SIZE * 8);
            KeyParameter keyParameter = new KeyParameter(key);
            cmac.init(keyParameter);
            cmac.update(valueToMAC, 0, valueToMAC.length);
            byte[] cmacSecret = new byte[CMAC_SIZE];
            cmac.doFinal(cmacSecret, 0);

            // diversify CMAC
            cmacSecret = diversifyCMAC(cmacSecret);

            // truncated CMAC
            byte[] sdmmac = new byte[CMAC_SIZE / 2];
            int j = 0;
            for (int i = 0; i < cmacSecret.length; i++) {
                if (i % 2 != 0) {
                    sdmmac[j] = cmacSecret[i];
                    j += 1;
                }
            }
            return Arrays.equals(cmacMirror, sdmmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Verify nfc tag
     *
     * @param uuid id of nfc tag
     * @param counter  nfc tag counter
     * @param mac verification code
     * @return status of nfc tag
     */
    public StatusResponse read(String uuid, String counter, String mac) {
        // counter is MSB in ASCII but LSB is required for calculation
        byte[] sdmReadCtr = TagUtils.msbToLsb(TagUtils.stringToBytes(counter));
        if (!this.verify(TagUtils.stringToBytes(uuid), sdmReadCtr, TagUtils.stringToBytes(mac))) {
            return new StatusResponse("invalid");
        }
        TagEntity tagEntity = PanacheEntityBase.find("chipUUID", uuid).firstResult();
        int lastCounter = tagEntity.getCounter();
        int currentCounter = Integer.decode("0x" + counter);
        if (lastCounter >= currentCounter) {
            return new StatusResponse("expired");
        }
        PanacheEntityBase.update("counter = ?1 where CHIP_UUID = ?2", currentCounter, uuid);
        return new StatusResponse("valid");
    }
}
