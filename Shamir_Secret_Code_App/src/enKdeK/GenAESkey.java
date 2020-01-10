package enKdeK;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import verif.TestDossier;
import verif.TestFichier;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Security;


public class GenAESkey {

    private String pwd = null ;
    private SecretKey aesKey =  null ;
    private TestDossier dossier = null;
    private TestFichier workFile = null ;
    private int key_length = 0;

    /**
     * Création d'une clef AES spécifique au UserService pour créer le secret
     * @param password
     * @param key_len
     */
    public GenAESkey (String password, int key_len) {
        this.pwd = password;
        this.key_length = key_len;
    }

    public GenAESkey (String password) {
        this.pwd = password;
    }

    /**
     * Obtention de la clef AES générée dans cette classe en privée
     * @return
     */
    public SecretKey clef(int key_len)
    {
        return genererCleAES(key_len);
    }


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

}
