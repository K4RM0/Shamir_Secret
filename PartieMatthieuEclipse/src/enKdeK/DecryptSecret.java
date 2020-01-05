package enKdeK;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import verif.TestDossier;
import verif.TestFichier;

public class DecryptSecretText {

    private final static File stockCrypt = new File ("D:/Cours/3eme semestre/Math/Projet/Test");
    private final static File stockClear = new File ("D:/Cours/3eme semestre/Math/Projet/TempSecr");
    private GenAESkey aesKey = null;
    private TestFichier fileUtil = null ;
    private TestDossier DirUtil = null ;
    private DfileName fileName = null;

    public void decrypt_sym(BigInteger bigInt )
    {

        fileUtil = new TestFichier(stockCrypt.getPath());
        String userService = 		fileUtil.getUserService();
        int key_length = fileUtil.getAes();
        System.out.println("getAesKey : " + key_length);
        aesKey = new GenAESkey(userService);
        try {
//		   SecretKey cle_aes = aesKey.clef(key_length);

            BigInteger aesBigI = fileUtil.getAesBigI();
            SecretKey cle_aes2 = new SecretKeySpec(aesBigI.toByteArray(), "AES");
//		   KeyGenerator key_gen = KeyGenerator.getInstance(fileUtil.getParamAes());
//		   SecretKey cle_aes = key_gen.generateKey();

            // Récupération de l'iv
            AlgorithmParameterSpec  salt = new IvParameterSpec("0123456789ABCDEF".getBytes());
//	AlgorithmParameterSpec  salt = new IvParameterSpec(new byte[16]);

            // ajout  du provider BC
            Security.addProvider(new BouncyCastleProvider());

            // aller chercher le code secret crypté
            File file = fileUtil.takeFile() ;
            FileInputStream fis = new FileInputStream(file);
            byte [] buf_crypt2 = new byte [(int) file.length()];
            fis.read(buf_crypt2);
            System.out.println(fis.toString());

            System.out.println("YYYYYYYYYYYY buf_crypt2 : " + new String(buf_crypt2));


//	System.out.println(buf_crypt2.toString() );
            fis.close();

//Déchiffrement de le chaine
            System.out.println("dechiffrement");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS7PADDING");
//    c.init(Cipher.DECRYPT_MODE, cle_aes, salt);
            c.init(Cipher.DECRYPT_MODE, cle_aes2, salt);
//	byte[] buf_decrypt = c.doFinal( buf_crypt1.getBytes());
//	byte[] buf_decrypt = c.doFinal( buf_crypt1);
            byte [] buf_crypt3 = new byte[c.getOutputSize(buf_crypt2.length)];
            byte[] buf_decrypt = c.doFinal(buf_crypt2,0, buf_crypt2.length);
            String decrypted = new String (c.doFinal(buf_crypt2), "UTF-8");

            System.out.println( "Decrypté : " + buf_decrypt.toString());
            System.out.println( "Decrypté : " +decrypted);

    /*	    char[] sortie= new char[(int)buf_decrypt.length];
	    DataInputStream instream=null;
	      System.out.println( "Decrypté : " + decrypted);
	      System.out.println(buf_decrypt.toString());
*/
            ///////////////////////
/*	// sauvegarde ds un fichier uniquement pour vérifier mais sans intérêt (il me semble)
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
//	         char ch = (char)Integer.parseInt(st, 16);
            result = result + st;
        }

        return result;
    }
}
