package enKdeK;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import verif.TestDossier;
import verif.TestFichier;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;


public class EncryptSecretText {

    final static File stockCrypt = new File ("PartieMatthieuEclipse/stockageSecretCrypt");
    final static File stockClear = new File ("PartieMatthieuEclipse/stockageSecretNonCrypt");
    private GenAESkey aesKey = null;
    private TestFichier fileUtil = null ;
    private TestDossier dirUtil = null ;
    private DfileName fileName = null;

    /**
     * Méthode permettant de crypté le secret en enregistrant dans un fichier le bigInteger (en byte)
     * Il y a aussi, pour l'exercice, enregistrement du secret non crypté dans un fichier.txt dont le nom est crypté
     * @param userService
     * @param key_length
     * @param entree
     */
    public void chiffrement_sym(String userService,int key_length, String entree )
    {
        aesKey = new GenAESkey(userService, key_length);
        try {
            SecretKey cle_aes = aesKey.clef(key_length);

            byte[] buffer = entree.getBytes();
            System.out.println("chiffrement de :"+ entree );

            // Choix de l'iv
            AlgorithmParameterSpec  salt = new IvParameterSpec("0123456789ABCDEF".getBytes());
	/*
	  AlgorithmParameterSpec  salt = new IvParameterSpec(new byte[16]);
	 * Impossible de décrypter si est activé
	 */

            // ajout  du provider BC
            Security.addProvider(new BouncyCastleProvider());

            // Chiffrement de la chaine
            Cipher c = Cipher.getInstance("AES/CBC/PKCS7PADDING","BC");
            c.init(Cipher.ENCRYPT_MODE, cle_aes, salt);
            byte[] clearData = "1234567890".getBytes();
            byte[] buf_crypt = c.doFinal(entree.getBytes("UTF-8"));
            byte[] buf_cryptClear = c.doFinal(clearData);

// permet d'afficher l'élément crypté en string pour être sur que le cryptage est réussi
//	      System.out.println( byteHexaToString (buf_crypt) );

            // sauvegarde secret text non crypté dans un fichier
            /**
             * sauvegarde secret text non crypté dans un fichier dont le nom est crypté
             * Nom du fichier == chemin + userService
             *
             */
            dirUtil = new TestDossier(stockClear.getAbsolutePath());
            fileName = new DfileName(userService) ;
            String path = fileName.encrypt(userService);
            File file = new File (stockClear.getAbsoluteFile()+ "/" +path + ".txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();


            // sauvevfgarde secret text crypté dans un fichier
            /**
             * sauvegarde secret text non crypté dans un fichier dont le nom est crypté
             * Nom du fichier == chemin + userService
             *
             */
            BigInteger bI = new BigInteger(buf_crypt);
            byte [] tab = bI.toByteArray();
            byte [] paramAES = cle_aes.getEncoded();
            BigInteger aesBigI = new BigInteger (paramAES);
            String aesBI = aesBigI.toString().substring(1);
            System.out.println("YYYYYYYYYYYY bu : " + aesBigI.toString());

            FileOutputStream envfos = new FileOutputStream(stockCrypt.getAbsolutePath() + "/" + aesBI + "_" + userService + "-" + key_length);
            envfos.write(tab);
            envfos.close();

            System.out.println("YYYY : " +stockCrypt.getAbsolutePath() + "/" + aesBI + "_" + userService + "-" + key_length);


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
