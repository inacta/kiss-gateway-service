package com.proofx.gateway.tezosj.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings({"java:S5542", "java:S3776"})
public class Helpers {

    private Helpers() {}

    public static byte[] zeros(int n) {
        return new byte[n];
    }

    // Encryption routine
    // Uses AES encryption
    public static byte[] encryptBytes(byte[] original, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    // Decryption routine
    public static byte[] decryptBytes(byte[] encrypted, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static boolean isJSONObject(String s) {
        try {
            new JSONObject(s);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static boolean isJSONArray(String s) {
        try {
            new JSONArray(s);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static String extractBacktrackedResult(JSONObject jsonObject) {
        String result = "";

        if (jsonObject.has("contents")) {
            JSONArray contentsArray = ((JSONArray) jsonObject.get("contents"));

            for (int w = 0; w < contentsArray.length(); w++) {
                JSONObject opRes = (JSONObject) ((JSONObject) contentsArray.get(w)).get("metadata");

                if (opRes.has("internal_operation_results")) {
                    JSONObject backtrack = (JSONObject) ((JSONObject) ((JSONObject) ((JSONArray) opRes.get("internal_operation_results")).get(0)).get("parameters")).get("value");

                    if (backtrack.has("int")) {
                        result = backtrack.getString("int");
                    }

                    break;

                }

                if (opRes.has("operation_result")) {
                    JSONObject opResult = (JSONObject) opRes.get("operation_result");

                    if (opResult.has("errors")) {
                        JSONObject errors;
                        errors = (JSONObject) ((JSONArray) opResult.get("errors")).get(1);

                        String kind = errors.getString("kind");
                        String id = errors.getString("id");
                        String description = "";

                        if (errors.has("with")) {
                            description = ((JSONObject) errors.get("with")).toString();
                        }

                        if (errors.has("wrongExpression")) {
                            description = ((JSONObject) errors.get("wrongExpression")).toString();
                        }

                        result = "Error: " + kind + " " + id + " " + description;

                    }

                    if (opResult.has("status")) {
                        result = result + " - Status : " + opResult.get("status").toString();
                    }

                    break;

                }
            }

        } else if (jsonObject.has("result")) {
            result = jsonObject.get("result").toString();
        }

        return result;
    }

}
