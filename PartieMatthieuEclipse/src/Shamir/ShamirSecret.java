package Shamir;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * <p> Calculate Shamir scheme. You need t shares of n for resolve the secret </p>
 */
public class ShamirSecret {

    final static BigInteger minValue = new BigInteger ("1000000000000");

    private BigInteger upperValueBI;
    private BigInteger xValue ;
    private BigInteger fxValue ;
    private ShamirKey [] shamKey ;


    /**
     * Créer le partage du secret --- Create a share using Shamir's scheme
     * @param x Morceau partagé  ---  Shamir's shares
     * @param k tableau des clefs du secret  ---  array of shamirKeys
     * @return vrai si  x est une clef shamir    ---   true is x is in k
     *
     */

    private BigInteger primeNbr (BigInteger valInit)	{
        // recherche du probable nbre premier plus grand que valInit

        BigInteger borne = valInit;
        BigInteger prime;
        BigInteger test = borne;
//		BigInteger step = 10;

        // recherche d'un premier plus petit que borne

/*		do {
//			valInit = valInit - step;
///		    step *= 2;
		    prime = valInit.nextProbablePrime();
		} while(prime < valInit);

*/
        return borne;
    }

    protected boolean isRepeat(BigInteger x, ShamirKey[] k){
        if(k.length == 0)
            return false;

        for(int i = 0; i < k.length; i++){
            if(k[i] == null)
                break;
            if(k[i].getX() == x)
                return true;
        }

        return false;
    }

    /**
     * Calculate polynomial
     * @param s parameters of polynomial
     * @param x variable of polynomial
     * @param p prime number just au-dessus de notre limite (for calulate mod (modulo))
     * @return solution of polynomial
     */
    private BigInteger calculatePolynomial(BigInteger s[], BigInteger x, BigInteger p){
        BigInteger result = BigInteger.ZERO;

        for(int i = 0; i < s.length; i++)
            result = result.add(s[i].multiply(x.pow(i)));

        result = result.mod(p);
        return result;
    }

    /**
     * Generate parameters of polynomial
     * @param t number of shares for solve Shamir scheme
     * @param numBits number of bits of parameters
     * @param SecretBytes secret
     * @return parameters of polynomial
     * @throw shamirException
     */
 /*   public BigInteger[] generateParameters(int t, int numBits, byte[] SecretBytes)throws ExceptionShamirSecret{
        BigInteger secret = new BigInteger(SecretBytes);

        if(secret.bitLength() >= numBits)
            throw new ExceptionShamirSecret("Nombre de bits de protection trop petit");

        BigInteger s[] = new BigInteger[t];
        s[0] = secret;
        //System.out.println("s(0) = " + secret + " (secret)" );

        for(int i = 1; i < t; i++){
            s[i] = new BigInteger(numBits, new Random());
            //System.out.println("s("+i+") = " +s[i]);
        }

        return s;
    }
    */

    /**
     *
     * @param t  ==  nombre de part pour résoudre le secret
     * @param numBits  == Key_Length (128, 198, 256)
     * @param SecretBI
     * @return
     * @throws ExceptionShamirSecret
     */
    private BigInteger[] generateParameters(int t, int numBits, BigInteger secretBI)throws ExceptionShamirSecret{

        if(secretBI.bitLength() >= numBits)
            throw new ExceptionShamirSecret("Nombre de bits de protection est trop petit");

        // Nombre premier juste au-dessus BI secret >> limite Max
        BigInteger sPremBI = secretBI.nextProbablePrime();

        // Détermination de la valeur moyenne entre limite max(nombre premier juste au-dessus secretBI) et minValue
        BigInteger tempVal = sPremBI.subtract(minValue);
        Random rnd = new Random();
        int len = secretBI.bitLength();
        BigInteger keyBI = null ;

        //

        BigInteger s[] = new BigInteger[t];
        s[0] = secretBI;
        //System.out.println("s(0) = " + secret + " (secret)" );

        for(int i = 1; i < t; i++){
            keyBI = new BigInteger(len, new SecureRandom()).mod(secretBI);
            if (keyBI.compareTo(minValue) < 0)
                keyBI = keyBI.add(minValue);
            if (keyBI.compareTo(tempVal) >= 0)
                keyBI = keyBI.mod(tempVal).add(minValue);

            s[i] = keyBI;

            keyBI = null;
            //System.out.println("s("+i+") = " +s[i]);
        }

        return s;
    }

    /**
     * Résoudre le secret Shamir  >> Polynôme LAGRANGE
     * Il faut rentrer le tableau en paramètre
     * @param sk
     * @return
     */

    public byte[] calculateLagrange(ShamirKey[] sk){
        BigInteger p = sk[0].getP();
        BigInteger d;
        BigInteger D;
        BigInteger c;
        BigInteger S = BigInteger.ZERO;
        for(int i = 0; i < sk.length; i++){
            d = BigInteger.ONE;
            D = BigInteger.ONE;
            for(int j = 0; j < sk.length; j++){
                if(j==i)
                    continue;
                d = d.multiply(sk[j].getX());
                D = D.multiply(sk[j].getX().subtract(sk[i].getX()));
            }
            c = d.multiply(D.modInverse(p)).mod(p);
            S = S.add(c.multiply(sk[i].getF())).mod(p);
        }
        return S.toByteArray();
    }

    /**
     * n = nombre de parts créés >> combien en créer et comment les gérer ???
     * Générer les clefs/parts pour Shamir
     */
    public ShamirKey[] generateKeys(int n, int t, int numBits, BigInteger secretBI)throws ExceptionShamirSecret{
        BigInteger[] s = generateParameters( t, numBits, secretBI);
        ShamirKey[] keys = new ShamirKey[n];
        if(s[0].bitLength() >= numBits)
            throw new ExceptionShamirSecret("Nombre de bits trop petit");
        if(n > t)
            throw new ExceptionShamirSecret("Il faut plus de parts à partager, SVP.");

        BigInteger prime = BigInteger.probablePrime(numBits, new Random());

        BigInteger fx, x;
        for(int i = 1; i <= n; i++){
            do{
                x = new BigInteger(numBits, new Random());
            }while(isRepeat(x, keys));
            fx = calculatePolynomial(s, x, prime);
            keys[i-1] = new ShamirKey();
            keys[i-1].setP(prime);
            keys[i-1].setX(x);        /// part à donner
            keys[i-1].setF(fx);		  /// part à donner
            System.out.println(i+"-> f("+x+") = " +keys[i-1].getF());
        }
        this.shamKey = keys ;

        return keys;
    }



    public  ShamirKey[] creaShamirKeys (BigInteger [] entry, BigInteger prime)
    {
        ShamirKey [] creaSham = new ShamirKey[entry.length/2];

        for(int i = 0; i <= (entry.length/2)-1; i++){
            creaSham[i].setP(prime);
            creaSham[i].setX(entry[i]);          /// parts données
            creaSham[i].setF(entry[i+1]);		  /// parts données
        }

        return creaSham;
    }


    public  ShamirKey [] getShamKey ()
    {
        return shamKey;
    }

}

