package Shamir;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
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

        System.out.println("bigIneteger decrypt Shamir " + result);
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
     * @param secretBI
     * @return
     * @throws ExceptionShamirSecret
     */
    private BigInteger[] generateParameters(int t, int numBits, BigInteger secretBI)throws ExceptionShamirSecret{

        if(secretBI.bitLength() >= numBits)
            throw new ExceptionShamirSecret("Nombre de bits de protection est trop petit ! \n" +
                    "Voud devez choisir un nombre au-dessus de " + numBits + " pour ce secret ! \n" +
                    "Veuillez recommencer toute l'opération, SVP.");

        // Nombre premier juste au-dessus BI secret >> limite Max
        BigInteger secretBIAbs = secretBI.abs();
        BigInteger sPremBI = secretBIAbs.nextProbablePrime();

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
            keyBI = new BigInteger(len, new SecureRandom()).mod(secretBIAbs);
            if (keyBI.compareTo(minValue) < 0)
                keyBI = keyBI.add(minValue);
            if (keyBI.compareTo(tempVal) >= 0)
                keyBI = keyBI.mod(tempVal).add(minValue);

            s[i] = keyBI;

            keyBI = null;
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
     * s = nombre premier limite supérieur
     * prime = modulo
     * n = nombre de parts créés >> combien en créer et comment les gérer ???
     * Générer les clefs/parts pour Shamir
     */
    public ShamirKey[] generateKeys(int n, int t, int numBits, BigInteger secretBI, String userService) throws ExceptionShamirSecret, IOException {

        // pour fichier Json
        Gson sauvGson = new GsonBuilder().create();
        JsonArray jzTab = new JsonArray();
        ArrayList<ShamirKey> keyArrayList = new ArrayList<ShamirKey>();

        BigInteger[] s = generateParameters( t, numBits, secretBI.abs());
        ShamirKey[] keys = new ShamirKey[n];

        if(s[0].bitLength() >= numBits)
            throw new ExceptionShamirSecret("Nombre de bits trop petit");
        if(t > n)
            throw new ExceptionShamirSecret("Il faut plus de parts à partager.\n" +
                    "Vous devez tout recommencer.");

        BigInteger prime = BigInteger.probablePrime(numBits, new Random());

        BigInteger fx, x;

        for(int i = 1; i <= n; i++){
            do{
                x = new BigInteger(numBits, new Random());
            }while(isRepeat(x, keys));
            fx = calculatePolynomial(s, x, prime);
            keys[i-1] = new ShamirKey();
            keys[i-1].setP(prime);
            keys[i-1].setX(x);        /// part à "donner"
            keys[i-1].setF(fx);		  /// part à donner (?)

            // Sauvegarde dans une ArryList pour sauvegarde Json
            keyArrayList.add(keys[i-1]);
        }

        String pathBI ="" ;
        if(secretBI.compareTo(BigInteger.ONE)<0) {

            pathBI = "0" + secretBI.toString(32).substring(1);
        }
        jzTab = sauvGson.toJsonTree(keyArrayList).getAsJsonArray();

        OutputStreamWriter wr= new OutputStreamWriter(new FileOutputStream("Shamir_Secret_Code_App/JsonFile/" + pathBI + "_" + userService + ".json"));
        wr.write(String.valueOf(jzTab));
        wr.close();

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

