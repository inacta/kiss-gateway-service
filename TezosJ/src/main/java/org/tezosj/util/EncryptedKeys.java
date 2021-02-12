package org.tezosj.util;

import com.goterl.lazycode.lazysodium.LazySodiumJava;

/**
 * Created by Milfont on 31/07/2018.
 */

// Encypted keys
public class EncryptedKeys {

    private byte[] encPublicKey;
    private byte[] encPrivateKey;
    private byte[] encPublicKeyHash;
    private byte[] mnemonicWords;
    private String encPass;
    private String encIv;

    /**
     * Gets the value of the encPublicKey property.
     *
     * @return possible object is {@link byte[]}
     */
    public byte[] getEncPublicKey() {
        return encPublicKey;
    }

    /**
     * Sets the value of the encPublicKey property
     *
     * @param encPublicKey allowed object is {@link byte[] }
     * @return the {@link EncryptedKeys}
     */
    public EncryptedKeys setEncPublicKey(byte[] encPublicKey) {
        this.encPublicKey = encPublicKey;
        return this;
    }

    /**
     * Gets the value of the encPrivateKey property.
     *
     * @return possible object is {@link byte[]}
     */
    public byte[] getEncPrivateKey() {
        return encPrivateKey;
    }

    /**
     * Sets the value of the encPrivateKey property
     *
     * @param encPrivateKey allowed object is {@link byte[] }
     * @return the {@link EncryptedKeys}
     */
    public EncryptedKeys setEncPrivateKey(byte[] encPrivateKey) {
        this.encPrivateKey = encPrivateKey;
        return this;
    }

    /**
     * Gets the value of the encPublicKeyHash property.
     *
     * @return possible object is {@link byte[]}
     */
    public byte[] getEncPublicKeyHash() {
        return encPublicKeyHash;
    }

    /**
     * Sets the value of the encPublicKeyHash property
     *
     * @param encPublicKeyHash allowed object is {@link byte[] }
     * @return the {@link EncryptedKeys}
     */
    public EncryptedKeys setEncPublicKeyHash(byte[] encPublicKeyHash) {
        this.encPublicKeyHash = encPublicKeyHash;
        return this;
    }

    /**
     * Gets the value of the mnemonicWords property.
     *
     * @return possible object is {@link byte[]}
     */
    public byte[] getMnemonicWords() {
        return mnemonicWords;
    }

    /**
     * Sets the value of the mnemonicWords property
     *
     * @param mnemonicWords allowed object is {@link byte[] }
     * @return the {@link EncryptedKeys}
     */
    public EncryptedKeys setMnemonicWords(byte[] mnemonicWords) {
        this.mnemonicWords = mnemonicWords;
        return this;
    }

    /**
     * Gets the value of the encPass property.
     *
     * @return possible object is {@link String}
     */
    public String getEncPass() {
        return encPass;
    }

    /**
     * Sets the value of the encPass property
     *
     * @param encPass allowed object is {@link String }
     * @return the {@link EncryptedKeys}
     */
    public EncryptedKeys setEncPass(String encPass) {
        this.encPass = encPass;
        return this;
    }

    /**
     * Gets the value of the encIv property.
     *
     * @return possible object is {@link String}
     */
    public String getEncIv() {
        return encIv;
    }

    /**
     * Sets the value of the encIv property
     *
     * @param encIv allowed object is {@link String }
     * @return the {@link EncryptedKeys}
     */
    public EncryptedKeys setEncIv(String encIv) {
        this.encIv = encIv;
        return this;
    }
}
