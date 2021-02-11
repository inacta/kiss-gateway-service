package org.tezosj.core;

import org.apache.commons.lang3.ArrayUtils;
import org.bitcoinj.core.Base58;
import org.bitcoinj.crypto.MnemonicCode;
import org.json.JSONObject;
import org.tezosj.legacy.CustomSodium;
import org.tezosj.util.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.tezosj.util.Global.TZJ_KEY_ALIAS;
import static org.tezosj.util.Helpers.*;

public class Accounts {

    private EncryptedKeys encKeys = null;

    public EncryptedKeys getEncKeys() throws Exception {
        if (encKeys == null) {
            throw new Exception("No wallet set");
        }
        return encKeys;
    }

    public void importWallet(String mnemonicWords, String passPhrase) {
        EncryptedKeys encryptedKeys = new EncryptedKeys();
        // Creates a unique copy and initializes libsodium native library.
        SecureRandom rand = new SecureRandom();
        int randId = rand.nextInt(1000000) + 1;
        CustomSodium sodium = new CustomSodium(String.valueOf(randId));
        encryptedKeys.setRandSeed(randId);
        encryptedKeys.setSodium(sodium);
        initStore(encryptedKeys, passPhrase.getBytes());
        // Cleans undesired characters from mnemonic words.
        String cleanMnemonic = mnemonicWords.replace("[", "").replace("]", "").replace(",", " ").replace("  ", " ");
        // Stores encypted mnemonic words into wallet's field.
        encryptedKeys.setMnemonicWords(encryptBytes(cleanMnemonic.getBytes(), encryptionKey(encryptedKeys)));
//        initDomainClasses();
        generateKeys(encryptedKeys, passPhrase);
        this.encKeys = encryptedKeys;
    }

    public JSONObject sign(byte[] bytes, String watermark) throws Exception {
        EncryptedKeys keys = this.getEncKeys();
        JSONObject response = new JSONObject();

        // Access wallet keys to have authorization to perform the operation.
        byte[] byteSk = keys.getEncPrivateKey();
        byte[] decSkBytes = decryptBytes(byteSk, this.encryptionKey(keys));

        // First, we remove the edsk prefix from the decoded private key bytes.
        byte[] edskPrefix =
                {(byte) 43, (byte) 246, (byte) 78, (byte) 7};
        byte[] decodedSk = Base58Check.decode(new String(decSkBytes));
        byte[] privateKeyBytes = Arrays.copyOfRange(decodedSk, edskPrefix.length, decodedSk.length);

        // Then we create a work array and check if the watermark parameter has been
        // passed.
        byte[] workBytes = ArrayUtils.addAll(bytes);

        if (watermark != null) {
            byte[] wmBytes = Encoder.HEX.decode(watermark);
            workBytes = ArrayUtils.addAll(wmBytes, workBytes);
        }

        // Now we hash the combination of: watermark (if exists) + the bytes passed in
        // parameters.
        // The result will end up in the sig variable.
        byte[] hashedWorkBytes = new byte[32];
        keys.getSodium().crypto_generichash(hashedWorkBytes, hashedWorkBytes.length, workBytes, workBytes.length, workBytes, 0);

        byte[] sig = new byte[64];
        keys.getSodium().crypto_sign_detached(sig, null, hashedWorkBytes, hashedWorkBytes.length, privateKeyBytes);

        // To create the edsig, we need to concatenate the edsig prefix with the sig and
        // then encode it.
        // The sbytes will be the concatenation of bytes (in hex) + sig (in hex).
        byte[] edsigPrefix =
                {9, (byte) 245, (byte) 205, (byte) 134, 18};
        byte[] edsigPrefixedSig = ArrayUtils.addAll(edsigPrefix, sig);
        ;
        String edsig = Base58Check.encode(edsigPrefixedSig);
        String sbytes = Encoder.HEX.encode(bytes) + Encoder.HEX.encode(sig);

        // Now, with all needed values ready, we create and deliver the response.
        response.put("bytes", Encoder.HEX.encode(bytes));
        response.put("sig", Encoder.HEX.encode(sig));
        response.put("edsig", edsig);
        response.put("sbytes", sbytes);

        return response;

    }

    public JSONObject signWithLedger(byte[] bytes, String watermark) {
        JSONObject response = new JSONObject();
        String watermarkedForgedOperationBytesHex = "";

        byte[] workBytes = ArrayUtils.addAll(bytes);

        if (watermark != null) {
            byte[] wmBytes = Encoder.HEX.decode(watermark);
            workBytes = ArrayUtils.addAll(wmBytes, workBytes);
        }

        watermarkedForgedOperationBytesHex = Encoder.HEX.encode(workBytes);

        // There is a Ledger hardware wallet configured. Signing will be done with it.
        Runtime rt = Runtime.getRuntime();
        String[] commands = {Global.ledgerTezosFolderPath + Global.ledgerTezosFilePath, Global.ledgerDerivationPath, watermarkedForgedOperationBytesHex};

        try {
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = "", signature = "", error = "";
            while ((s = stdInput.readLine()) != null) {
                signature = signature + s;
            }

            JSONObject jsonObject = new JSONObject(signature);
            String ledgerSig = jsonObject.getString("signature");

            String r = "";
            while ((r = stdError.readLine()) != null) {
                error = error + r;
            }

            byte[] sig = Encoder.HEX.decode(ledgerSig);

            // To create the edsig, we need to concatenate the edsig prefix with the sig and
            // then encode it.
            byte[] edsigPrefix =
                    {9, (byte) 245, (byte) 205, (byte) 134, 18};
            byte[] edsigPrefixedSig = ArrayUtils.addAll(edsigPrefix, sig);
            String edsig = Base58Check.encode(edsigPrefixedSig);

            // The sbytes will be the concatenation of bytes (in hex) + sig (in hex).
            String sbytes = Encoder.HEX.encode(bytes) + Encoder.HEX.encode(sig);

            // Now, with all needed values ready, we create and deliver the response.
            response.put("bytes", Encoder.HEX.encode(bytes));
            response.put("sig", Encoder.HEX.encode(sig));
            response.put("edsig", edsig);
            response.put("sbytes", sbytes);
        } catch (Exception e) {
            response = null;
        }

        return response;
    }

    // Retrieves the Public Key Hash (Tezos user address) upon user request.
    public String getPublicKeyHash() {
        if (this.encKeys == null) {
            throw new RuntimeException("Wallet not set");
        }
        byte[] decrypted = decryptBytes(this.encKeys.getEncPublicKeyHash(), encryptionKey(this.encKeys));
        return new String(decrypted);
    }

    private void initStore(EncryptedKeys encryptedKeys, byte[] toHash) {
        try {
            String pString = new String(toHash, "UTF-8");
            int hashedP = pString.hashCode();
            StringBuilder strHash = new StringBuilder(String.valueOf(hashedP));
            while (strHash.length() < 16) {
                strHash.append(strHash);
            }
            strHash = new StringBuilder(strHash.substring(0, 16)); // 16 bytes needed.
            pString = strHash.toString();

            SecretKey secretKey = createKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptionIv = cipher.getIV();
            byte[] pBytes = pString.getBytes("UTF-8");
            byte[] encPBytes = cipher.doFinal(pBytes);
            String encP = Base64.getEncoder().encodeToString(encPBytes);
            String encryptedIv = Base64.getEncoder().encodeToString(encryptionIv);

            Global.initKeyStore();
            Global.myKeyStore.load(null, encP.toCharArray());
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);

            KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(encP.toCharArray());
            Global.myKeyStore.setEntry(TZJ_KEY_ALIAS + encryptedKeys.getRandSeed(), secretKeyEntry, entryPassword);
            encryptedKeys.setEncPass(encP);
            encryptedKeys.setEncIv(encryptedIv);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize Android KeyStore.", e);
        }
    }

    private SecretKey createKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    public byte[] encryptionKey(EncryptedKeys encryptedKeys) {
        try {
            String base64EncryptedPassword = encryptedKeys.getEncPass();
            String base64EncryptionIv = encryptedKeys.getEncIv();

            byte[] encryptionIv = Base64.getDecoder().decode(base64EncryptionIv);
            byte[] encryptionPassword = Base64.getDecoder().decode(base64EncryptedPassword);

            KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(
                    base64EncryptedPassword.toCharArray());
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) Global.myKeyStore.getEntry(TZJ_KEY_ALIAS + encryptedKeys.getRandSeed(),
                    entryPassword);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, entry.getSecretKey(), new IvParameterSpec(encryptionIv));
            return cipher.doFinal(encryptionPassword);
        } catch (Exception e) {
            return new byte[0];
        }

    }

    private void initDomainClasses() {
//        this.rpc = new Rpc();
//        this.crypto = new Crypto();
    }

    // This method generates the Private Key, Public Key and Public Key hash (Tezos address).
    private EncryptedKeys generateKeys(EncryptedKeys encryptedKeys, String passphrase) {

        // Decrypts the mnemonic words stored in class properties.
        byte[] input = decryptBytes(encryptedKeys.getMnemonicWords(), encryptionKey(encryptedKeys));

        // Converts mnemonics back into String.
        StringBuilder builder = new StringBuilder();
        for (byte anInput : input) {
            builder.append((char) (anInput));
        }

        List<String> items = Arrays.asList((builder.toString()).split(" "));
        byte[] srcSeed = MnemonicCode.toSeed(items, passphrase);
        byte[] seed = Arrays.copyOfRange(srcSeed, 0, 32);

        byte[] sodiumPrivateKey = zeros(32 * 2);
        byte[] sodiumPublicKey = zeros(32);
        encryptedKeys.getSodium().crypto_sign_seed_keypair(sodiumPublicKey, sodiumPrivateKey, seed);

        // These are our prefixes.
        byte[] edpkPrefix =
                {(byte) 13, (byte) 15, (byte) 37, (byte) 217};
        byte[] edskPrefix =
                {(byte) 43, (byte) 246, (byte) 78, (byte) 7};
        byte[] tz1Prefix =
                {(byte) 6, (byte) 161, (byte) 159};

        // Creates Tezos Public Key.
        byte[] prefixedPubKey = new byte[36];
        System.arraycopy(edpkPrefix, 0, prefixedPubKey, 0, 4);
        System.arraycopy(sodiumPublicKey, 0, prefixedPubKey, 4, 32);

        byte[] firstFourOfDoubleChecksum = Sha256.hashTwiceThenFirstFourOnly(prefixedPubKey);
        byte[] prefixedPubKeyWithChecksum = new byte[40];
        System.arraycopy(prefixedPubKey, 0, prefixedPubKeyWithChecksum, 0, 36);
        System.arraycopy(firstFourOfDoubleChecksum, 0, prefixedPubKeyWithChecksum, 36, 4);

        // Encrypts and stores Public Key into wallet's class property.
        encryptedKeys.setEncPublicKey(encryptBytes(Base58.encode(prefixedPubKeyWithChecksum).getBytes(), encryptionKey(encryptedKeys)));

        // Creates Tezos Private (secret) Key.
        byte[] prefixedSecKey = new byte[68];
        System.arraycopy(edskPrefix, 0, prefixedSecKey, 0, 4);
        System.arraycopy(sodiumPrivateKey, 0, prefixedSecKey, 4, 64);

        firstFourOfDoubleChecksum = Sha256.hashTwiceThenFirstFourOnly(prefixedSecKey);
        byte[] prefixedSecKeyWithChecksum = new byte[72];
        System.arraycopy(prefixedSecKey, 0, prefixedSecKeyWithChecksum, 0, 68);
        System.arraycopy(firstFourOfDoubleChecksum, 0, prefixedSecKeyWithChecksum, 68, 4);

        // Encrypts and stores Private Key into wallet's class property.
        encryptedKeys.setEncPrivateKey(encryptBytes(Base58.encode(prefixedSecKeyWithChecksum).getBytes(), encryptionKey(encryptedKeys)));

        // Creates Tezos Public Key Hash (Tezos address).
        byte[] genericHash = new byte[20];
        encryptedKeys.getSodium().crypto_generichash(genericHash, genericHash.length, sodiumPublicKey, sodiumPublicKey.length, sodiumPublicKey, 0);

        byte[] prefixedGenericHash = new byte[23];
        System.arraycopy(tz1Prefix, 0, prefixedGenericHash, 0, 3);
        System.arraycopy(genericHash, 0, prefixedGenericHash, 3, 20);

        firstFourOfDoubleChecksum = Sha256.hashTwiceThenFirstFourOnly(prefixedGenericHash);
        byte[] prefixedPKhashWithChecksum = new byte[27];
        System.arraycopy(prefixedGenericHash, 0, prefixedPKhashWithChecksum, 0, 23);
        System.arraycopy(firstFourOfDoubleChecksum, 0, prefixedPKhashWithChecksum, 23, 4);

        // Encrypts and stores Public Key Hash into wallet's class property.
        encryptedKeys.setEncPublicKeyHash(encryptBytes(Base58.encode(prefixedPKhashWithChecksum).getBytes(), encryptionKey(encryptedKeys)));
        return encryptedKeys;
    }

}
