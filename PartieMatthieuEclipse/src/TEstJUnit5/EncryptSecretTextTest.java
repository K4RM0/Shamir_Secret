package TEstJUnit5;

import enKdeK.GenAESkey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptSecretTextTest {

    @org.junit.jupiter.api.Test
    void chiffrement_symDifferentSecrets() {
        String service = "user";
        int key = 128 ;
        String secret1 = "a";
        String secret2 = "b";

        GenAESkey aesKey = new GenAESkey(service, key);
        try {
            SecretKey cle_aes = aesKey.clef(key);

            byte[] bufferA = secret1.getBytes();
            byte[] bufferB = secret2.getBytes();

            // Choix de l'iv
            AlgorithmParameterSpec salt = new IvParameterSpec("0123456789ABCDEF".getBytes());

            // ajout  du provider BC
            Security.addProvider(new BouncyCastleProvider());

            // Chiffrement de la chaine
            Cipher c = Cipher.getInstance("AES/CBC/PKCS7PADDING","BC");
            c.init(Cipher.ENCRYPT_MODE, cle_aes, salt);
            byte[] clearData = "1234567890".getBytes();
            byte[] buf_cryptA = c.doFinal(secret1.getBytes("UTF-8"));
            byte[] buf_cryptB = c.doFinal(secret2.getBytes("UTF-8"));
            byte[] buf_cryptClear = c.doFinal(clearData);

            BigInteger bIA = new BigInteger(buf_cryptA);
            BigInteger bIB = new BigInteger(buf_cryptB);

            assertEquals(bIA, bIB); // doit dire que ce n'est pas equivalent

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void chiffrement_symSameSecrets() {
        String service = "user";
        int key = 128;
        String secret1 = "a";
        String secret2 = "a";

        GenAESkey aesKey = new GenAESkey(service, key);
        try {
            SecretKey cle_aes = aesKey.clef(key);

            byte[] bufferA = secret1.getBytes();
            byte[] bufferB = secret2.getBytes();

            // Choix de l'iv
            AlgorithmParameterSpec salt = new IvParameterSpec("0123456789ABCDEF".getBytes());

            // ajout  du provider BC
            Security.addProvider(new BouncyCastleProvider());

            // Chiffrement de la chaine
            Cipher c = Cipher.getInstance("AES/CBC/PKCS7PADDING", "BC");
            c.init(Cipher.ENCRYPT_MODE, cle_aes, salt);
            byte[] clearData = "1234567890".getBytes();
            byte[] buf_cryptA = c.doFinal(secret1.getBytes("UTF-8"));
            byte[] buf_cryptB = c.doFinal(secret2.getBytes("UTF-8"));
            byte[] buf_cryptClear = c.doFinal(clearData);

            BigInteger bIA = new BigInteger(buf_cryptA);
            BigInteger bIB = new BigInteger(buf_cryptB);

            assertEquals(bIA, bIB); // doit dire que c'est equivalent


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}