package com.example.darren.viewpagertest.teetest;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyPairGenerator;

public class AndroidProtectedConfirmation {

    private byte[] bytes = {};

    public void test(){
        //生成密钥
        if (Build.VERSION.SDK_INT == 28){
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("defaultKeyName",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserConfirmationRequired(true)
                    .setUserAuthenticationRequired(true)
                    .setAttestationChallenge(bytes)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }

    }
}
