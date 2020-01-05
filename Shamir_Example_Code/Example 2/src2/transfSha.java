import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.NoSuchAlgorithmException;


public class transfSha {

    public String hashPass(String pass,String sel) {
        String hash;
        MessageDigest md = null;
        byte[]hashInBytes= null ;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        hash = md.digest((pass + sel).getBytes()).toString();
        hashInBytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        System.out.println(hash);
        System.out.println(sb.toString());

        return hash;
// NORMALEMENT : return sb;
    }
//////////////////////   Vecteur initiale donne un aléas sur le cipher text
// ///////////////////   et est public (voir ce qui est appellé sell dans les codes que j'ai trouvé)

/*    public String decrypt1(String password, String salt, String encString) throws Exception {

        byte[] ivData = toByte(encString.substring(0, 32));
        byte[] encData = toByte(encString.substring(32));

        PKCS12ParametersGenerator gen = new PKCS12ParametersGenerator(new SHA256Digest());
        gen.init(password.getBytes(), toByte(salt), 50);
        CBCBlockCipher cbcBlockcipher = new CBCBlockCipher(new RijndaelEngine(256));
        CipherParameters params = gen.generateDerivedParameters(256, 256);

        cbcBlockcipher.init(false, params);

        PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(cbcBlockcipher, new PKCS7Padding());
        byte[] plainTemp = new byte[aesCipher.getOutputSize(encData.length)];
        int offset = aesCipher.processBytes(encData, 0, encData.length, plainTemp, 0);
        int last = aesCipher.doFinal(plainTemp, offset);
        byte[] plain = new byte[offset + last];
        System.arraycopy(plainTemp, 0, plain, 0, plain.length);
        return new String(plain);
    }
*/
    public String dec(String password, String salt, String encString)
            throws Exception {

        byte[] ivData = (encString.substring(0, 32)).getBytes();
        byte[] encData = toByte(encString.substring(32));

        // get raw key from password and salt
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(),
                toByte(salt), 50, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBEWithSHA256And256BitAES-CBC-BC");
        SecretKeySpec secretKey = new SecretKeySpec(keyFactory.generateSecret(
                pbeKeySpec).getEncoded(), "AES");
        byte[] key = secretKey.getEncoded();

        // setup cipher parameters with key and IV
        KeyParameter keyParam = new KeyParameter(key);
        CipherParameters params = new ParametersWithIV(keyParam, ivData);

        // setup AES cipher in CBC mode with PKCS7 padding
        BlockCipherPadding padding = new PKCS7Padding();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()), padding);
        cipher.reset();
        cipher.init(false, params);

        // create a temporary buffer to decode into (it'll include padding)
        byte[] buf = new byte[cipher.getOutputSize(encData.length)];
        int len = cipher.processBytes(encData, 0, encData.length, buf, 0);
        len += cipher.doFinal(buf, len);

        // remove padding
        byte[] out = new byte[len];
        System.arraycopy(buf, 0, out, 0, len);

        // return string representation of decoded bytes
        return new String(out, "UTF-8");
    }
}
