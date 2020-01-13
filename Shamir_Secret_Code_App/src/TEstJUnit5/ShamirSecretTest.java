package TEstJUnit5;

class ShamirSecretTest {


    /*@Test
    void calculateLagrange(ShamirKey[] sk ) {
        //générer 4 parts (a1, a2, a3, a4)
        //sortir ces valeurs de la fonction qui genère les parts
        //employer la méthode de la grange
        //voir si les deux résultats sont les mêmes
        //
        //

        BigInteger valeur1 = sk[1].getX();
        BigInteger bigInt = valeur1;
        SecretKeySpec a1 = new SecretKeySpec(bigInt.toByteArray(), "AES");
        BigInteger valeur2 = sk[2].getX();
        BigInteger bigInt2 = valeur2;
        SecretKeySpec a2 = new SecretKeySpec(bigInt2.toByteArray(), "AES");
        BigInteger valeur3 = sk[3].getX();
        BigInteger bigInt3 = valeur3;
        SecretKeySpec a3 = new SecretKeySpec(bigInt3.toByteArray(), "AES");
        BigInteger valeur4 = sk[4].getX();
        BigInteger bigInt4 = valeur4;
        SecretKeySpec a4 = new SecretKeySpec(bigInt4.toByteArray(), "AES");

        BigInteger val1 = sk[1].getF();
        BigInteger big1 = val1;
        SecretKeySpec b1 = new SecretKeySpec(big1.toByteArray(), "AES");
        BigInteger val2 = sk[2].getF();
        BigInteger big2 = val2;
        SecretKeySpec b2 = new SecretKeySpec(big2.toByteArray(), "AES");
        BigInteger val3 = sk[3].getF();
        BigInteger big3 = val3;
        SecretKeySpec b3 = new SecretKeySpec(big3.toByteArray(), "AES");
        BigInteger val4 = sk[4].getF();
        BigInteger big4 = val4;
        SecretKeySpec b4 = new SecretKeySpec(big4.toByteArray(), "AES");

        // recupération des points a1, a2, a3, a4
        SecretKeySpec X[] = {a1, a2, a3, a4};
        SecretKeySpec F[] = {b1, b2, b3, b4};


        //vérification avec la méthode lagrange

        assertEquals(a1, );
        assertEquals(a2, );
        assertEquals(a3, );
        assertEquals(a4, );








    }

    @Test
    public static boolean nbreAleaHexaShouldReturnAnHexaNumber(){
        NbrAleaHexadecimForJson hexaAlea = new NbrAleaHexadecimForJson();

        String value;
        if (value.startsWith("-"))
        {
            value = value.substring(1);
        }

        value = value.toLowerCase();

        if (value.length() <= 2 || !value.startsWith("0x"))
        {
            return false;
        }

        for (int i = 2; i < value.length(); i++)
        {
            char c = value.charAt(i);

            if (!(c >= '0' && c <= '9' || c >= 'a' && c <= 'f'))
            {
                return false;
            }
        }

        return true;


    }

*/
}

