import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;


public class Main_TestSSSS {

    /**
     * @param args
     */
    final static int n = 9; //number of generate shares
    final static int t = 5; //number of shares for solve the secret (t <= n)
    final static int numBits = 256; //number of bits of p
    static ShamirSecret ShamirSecret = new ShamirSecret();
    final static BigInteger maxValue = new BigInteger("5000000000000000000000000");
    final static BigInteger minValue = new BigInteger ("1000000000000");

    public static void main(String[] args) {
        // TODO Auto-generated method stub

/*		String pass = "phrase à écrire";
		String sel = "ajout pour compliquer les choses (et, peut-être signer l'origine)";
		String hash;
        MessageDigest md = null;
        byte[]hashInBytes;

        try {
            md = MessageDigest.getInstance("SHA-256");
            hashInBytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        hash = md.digest((pass + sel).getBytes()).toString();
        StringBuilder sb = new StringBuilder();
//        for (byte b : hashInBytes) {
//            sb.append(String.format("%02x", b));
//        }

        System.out.println(hash);
//        System.out.println(sb.toString());

 */

        String secret = "Secret Sharing";


        System.out.println("Secret = " + secret);
//			BigInteger sPr = new  BigInteger(secret.getBytes());
        BigInteger sPremBI = maxValue.nextProbablePrime();
        System.out.println("nb Prem's : " + sPremBI);
//			System.out.println("nb BI : " + sPr);

        // choose randomized secret
        BigInteger tempVal = maxValue.subtract(minValue);
        Random rnd = new Random();
        int len = sPremBI.bitLength();
        BigInteger secretBI = new BigInteger(len, new SecureRandom()).mod(sPremBI);
/*		      if (secretBI.compareTo(minValue) < 0)
		    	  secretBI = secretBI.add(minValue);
		      if (secretBI.compareTo(tempVal) >= 0)
		    	  secretBI = secretBI.mod(tempVal).add(minValue);
	*/
        System.out.println("secretBI : " + secretBI);


        //Create key
        ShamirKey[] sk;
        BigInteger[] s;

        try{
//				s = createShamirSecret.generateParameters(t, numBits, secret.getBytes());
            s = ShamirSecret.generateParameters(t, numBits, secretBI);

            sk = ShamirSecret.generateKeys(n, t, numBits, s);
            for (int i = 0; i < sk.length; i++) {
                System.out.println("val " + i + " : "+ sk[i]);
                System.out.println("val F " + i + " : "+ sk[i].getF());
                System.out.println("val P " + i + " : "+ sk[i].getP());
                System.out.println("val X " + i + " : "+ sk[i].getX());
            }
            /*
             *  création mots de passe et enregistrement dans fichier où :
             * - nom utilisateur
             * - valeur x ( getX() ) et voir si possibilité d'associer à une autre valeur plus simple (string ou int)
             * >>>  ex : 333e7df
             * - valeur f(x) en 16 bits  ( getF().16bits )
             * <<<< Vérif que utilisateur + clef + solution f(x) associé  LinkedList
             * >>>>>>>>>>>>>> voir pour gérer que les parts (clefs) ne soient pas données en double


             * conservation du secret ( sk2.getP() ) dans un fichier crypté (?)
             * avec  indication du nombre de parts/clefs
             * et l'association des parts >> LinkedList
             *
             */
        }catch(ExceptionShamirSecret sE){
            System.out.println("Error while generate shamir keys");
            return;
        }

        // Voir si utile lors de l'attribution des clefs
        ShamirKey[] sk2 = new ShamirKey[t];
        //select t keys

        sk2[0] = sk[1];
        sk2[1] = sk[3];
        sk2[2] = sk[4];
        sk2[3] = sk[5];
        sk2[4] = sk[2];

        /*
         *  création mots de passe et enregistrement dans fichier où :
         * - nom utilisateur
         * - valeur x encodé en 16 bits   ( getX().16bits )
         * >>>  ex : 333e7df
         * - valeur f(x) en 16 bits  ( getF().16bits )
         * >>>>>>>>>>>>>> voir pour gérer que les parts ne soient pas donné en double


         * conservation du secret ( sk2.getP() ) dans un fichier crypté (?)
         * avec  indication du nombre de parts
         * et l'association des parts
         *
         */


        /*
         * Remplacer la fonction existante par
         * une fonction permettant de calculer chaque secret
         * en vérifiant si le pwd correspond au user
         * Puis en construisant un tableau
         */
        //solve scheme, calculate parameter 0 (secret)
        byte[] des = ShamirSecret.calculateLagrange(sk2);

        BigInteger secretFound = new BigInteger(des);
        System.out.println("Secret = " + secretFound);

    }


}

