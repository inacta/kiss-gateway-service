package org.tezosj.core;

import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;
import org.tezosj.TezosJ;
import org.tezosj.exceptions.NoWalletSetException;
import org.tezosj.util.Base58Check;
import org.tezosj.util.EncryptedKeys;

import java.util.Arrays;

import static org.tezosj.util.Helpers.decryptBytes;

public class Crypto {

    private final TezosJ tezosJ;
    private final LazySodiumJava sodium = new LazySodiumJava(new SodiumJava());

    public Crypto (TezosJ tezosJ) {
        this.tezosJ = tezosJ;
    }

    public boolean genericHash(byte[] out, int outLen, byte[] in, long inLen, byte[] key, int keyLen) {
        return this.sodium.cryptoGenericHash(out, outLen, in, inLen, key, keyLen);
    }

    public boolean sign(byte[] signature, long[] sigLength, byte[] message, long messageLen) throws NoWalletSetException {
//        byte[] secretKey
        EncryptedKeys keys = this.tezosJ.accounts.getEncKeys();
        // Access wallet keys to have authorization to perform the operation.
        byte[] byteSk = keys.getEncPrivateKey();
        byte[] decSkBytes = decryptBytes(byteSk, this.tezosJ.accounts.encryptionKey(keys));

        // First, we remove the edsk prefix from the decoded private key bytes.
        byte[] edskPrefix = {(byte) 43, (byte) 246, (byte) 78, (byte) 7};
        byte[] decodedSk = Base58Check.decode(new String(decSkBytes));
        byte[] privateKeyBytes = Arrays.copyOfRange(decodedSk, edskPrefix.length, decodedSk.length);

        return this.sodium.cryptoSignDetached(signature, sigLength, message, messageLen, privateKeyBytes);
    }

    public boolean sign(byte[] signature, long[] sigLength, byte[] message, long messageLen, byte[] secretKey) {
        return this.sodium.cryptoSignDetached(signature, sigLength, message, messageLen, secretKey);
    }

    public boolean signSeedKeypair(byte[] publicKey, byte[] secretKey, byte[] seed) {
        return this.sodium.cryptoSignSeedKeypair(publicKey, secretKey, seed);
    }
}
