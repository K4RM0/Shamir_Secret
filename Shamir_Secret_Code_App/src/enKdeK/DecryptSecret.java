package enKdeK;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import verif.TestDossier;
import verif.TestFichier;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

public class DecryptSecret {

    final static File stockCrypt = new File ("Shamir_Secret_Code_App/stockageSecretCrypt");
    final static File stockClear = new File ("Shamir_Secret_Code_App/stockageSecretNonCrypt");
    private GenAESkey aesKey = null;
    private TestFichier fileUtil = null ;
    private TestDossier DirUtil = null ;
    private DfileName fileName = null;

    public void decrypt_sym(BigInteger bigInt, String userService ) throws IOException {

        fileUtil = new TestFichier(stockCrypt.getAbsolutePath(), userService);
        File file = fileUtil.takeFile() ;
        int key_length = fileUtil.getAes();
        aesKey = new GenAESkey(userService);

        try {


            BigInteger aesBigI = bigInt;

            SecretKey cle_aes2 = new SecretKeySpec(bigInt.toByteArray(), "AES");


            // Récupération de l'iv
            AlgorithmParameterSpec  salt = new IvParameterSpec("0123456789ABCDEF".getBytes());
//	AlgorithmParameterSpec  salt = new IvParameterSpec(new byte[16]);

            // ajout  du provider BC
            Security.addProvider(new BouncyCastleProvider());

            // aller chercher le code secret crypté
            FileInputStream fis = new FileInputStream(file);
            byte [] buf_crypt2 = new byte [(int) file.length()];
            fis.read(buf_crypt2);

            fis.close();

//Déchiffrement de le chaine
            System.out.println("dechiffrement");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS7PADDING");

            c.init(Cipher.DECRYPT_MODE, cle_aes2, salt);

//	byte[] buf_decrypt = c.doFinal( buf_crypt1.getBytes());
//	byte[] buf_decrypt = c.doFinal( buf_crypt1);
            byte [] buf_crypt3 = new byte[c.getOutputSize(buf_crypt2.length)];
            byte[] buf_decrypt = c.doFinal(buf_crypt2,0, buf_crypt2.length);
            String decrypted = new String (c.doFinal(buf_crypt2), "UTF-8");

            ///////////////////////
/*	// sauvegarde ds un fichier uniquement pour vérifier mais sans intérêt (il me semble)   >>>>>>  pour test ?????
	envfos = new FileOutputStream("D:\\Cours\\3eme semestre\\Math\\Projet\\TEst\\fichier_dechiffre");
	envfos.write(buf_decrypt);
	envfos.close();
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String  byteHexaToString (byte [] hexaToString) {
        String result = "" ;
        for(int i = 0; i < hexaToString.length; i=i+2) {
            String st = ""+hexaToString[i]+""+hexaToString[i+1];
            result = result + st;
        }

        return result;
    }
}
