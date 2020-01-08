package enKdeK;

import java.io.File;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import verif.TestDossier;
import verif.TestFichier;


public class GenAESkey {

    private String pwd = null ;
    private SecretKey aesKey =  null ;
    private TestDossier dossier = null;
    private TestFichier workFile = null ;
    private int key_length = 0;

    /**
     * Objet pour créer le secret
     * @param password
     * @param key_len
     */
    public GenAESkey (String password, int key_len) {
        this.pwd = password;
        this.key_length = key_len;
    }

    /**
     * Objet pour récupérer le secret
     * @param password
     */
    public GenAESkey (String password) {
        this.pwd = password;
    }

    /**
     * Obtention de la clef AES générée en privée
     * @return
     */
    public SecretKey clef(int key_len)
    {
        return genererCleAES(key_len);
    }

    /**
     * Récupération de la clef AES pour le décryptage
     * @return
     */
/*	public SecretKey getKey(String filePath)
	{
		workFile = new TestFichier(filePath, pwd);
*/
    /**
     * Fichiers dans le dossier de stockage des clefs cryptées
     *
     */
/*		File file = workFile.takeFile();
		String getName = file.getPath();
		String forKey = getName.substring(getName.indexOf(pwd+"_")) ;
		this.key_length =Integer.parseInt(forKey);

		return clef();
	}
*/

    /**
     * Génération de la clef AES PRIVATE
     * @param key_len
     * @return
     */
    private SecretKey genererCleAES( int key_len)
    {
        try
        {
            //CHoix du   vecteur initial (mode CBC)
//	       byte[] iv = tobyteHexa (password);
//	IvParameterSpec salt = new IvParameterSpec(iv);
            // ajout  du provider BC
            Security.addProvider(new BouncyCastleProvider());

            //generation de la clé
            KeyGenerator key_gen = KeyGenerator.getInstance("AES","BC"); //  instance du generateur AES.

	    /*
	     SecureRandom sec_rand = SecureRandom.getInstance("SHA1PRNG","SUN");
	     key_gen.init(key_len,sec_rand); // Configurer avec les bits de "key_size" en utilisant "sec-rand" salted PRNG.
	     * Impossible de décrypter si on conserve le SecurRandom
	     */

            key_gen.init(key_len); // Configurer avec les bits de "key_size"
            SecretKey cle_aes = key_gen.generateKey(); //Generer la clef

            return(cle_aes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Permettra d'afficher en String l'élément crypté
     * (hexadécimal en String)
     * @param hexaToString
     * @return
     */
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
