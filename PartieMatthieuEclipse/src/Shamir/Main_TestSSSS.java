package Shamir;

import enKdeK.DecryptSecret;
import enKdeK.EncryptSecretText;
import verif.formatUserService;
import verif.TestFichier;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

class Main_TestSSSS {

    /**
     * @param args
     */
    final static File stockCrypt = new File ("D:/Cours/3eme semestre/Math/Projet/Test");
    final static File stockClear = new File ("D:/Cours/3eme semestre/Math/Projet/TempSecr");
    final static int n = 9; //number of generate shares
    final static int t = 5; //number of shares for solve the secret (t <= n)
    final static int numBits = 256; //number of bits of p  (à coisir entre 128 et 256)
    static TestFichier fileUtile = null ;
    static ShamirSecret shamirSecret = null;
    static ShamirKey [] ShamKey = null;
    static EncryptSecretText creaSecret = null;
    static DecryptSecret decodSecret = null;


    public static void main(String[] args) throws Exception {

        String newDirectory = "";
        // // // demande si création secret ou si création nouvelles parts du secret ou si révélation secret
        do {
            String userService = "";
            int nbrBits = 256; // chiffrement à 256 bits par défaut si aucune saisie n'est faite
            String secret = "";
            String part2Secret = "";

            System.out.println("Bonjour, Que voulez-vous faire ? (Saisr le chiffre correspondant) \t"
                    + "(1) créer un secret \t"
                    + "(1) créer des parts de secret \t"
                    + "(3) révéler un secret \t"
                    + "(4) ne rien faire\t");

            Scanner scan = new Scanner(System.in);
            newDirectory = scan.next();
            scan = null ;

            // // Si création secret
            if(newDirectory == "1")
            {

                /// demande UserService

                System.out.println("Saisir le nom du service de destination du mot de passe \t"
                        + "(Ne pas mettre de caractère spéciaux en fin de ligne,  SVP.\t");

                userService = saisiStringConsole();
                userService = new formatUserService().formatUserService(userService);

                // vérifier si le UserService est déjà utilisé
                fileUtile = new TestFichier(stockCrypt.getPath(), userService);
                if(fileUtile.validFile())
                {
                    /// demande nombre de bits (128 ou 256(defaut))
                    System.out.println("Saisir le nombre de bits d'encodage (128 ou 256) : \t");

                    int nbrBitsTemp = saisiIntConsole();

                    // vérifier si le nombre de bits est compatible, sinon valeur par défaut
                    if(nbrBitsTemp == 128 || nbrBitsTemp== 256)
                        nbrBits=nbrBitsTemp;

                    /// demande de la saisie du secret (mini un caractère)
                    do {
                        System.out.println("Saisir la phase ou le mot secret que vous souhaitez\t");

                        secret = saisiStringConsole();

                    }while(secret.length()>0);
                    // vérifier que l'utilisateur a bien saisi au moins 1 caractère

                    // // // encodage du secret
                    creaSecret = new EncryptSecretText();
                    creaSecret.chiffrement_sym(userService, nbrBits, secret);
                    newDirectory = "4";
                }
                else
                {
                    newDirectory = "4";
                }
            }
            // // création de parts d'un secret
            if (newDirectory == "2")
            {
                int cpt =0;
                BigInteger secretBigI ;
                int nbrSharedKey =0;
                int nbrKeyUtil = 0;
                int numBits = 192; // valeur par défaut

                /// demande du UserService
                System.out.println("Saisir le nom du service de destination \t"
                        + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\t");

                userService = saisiStringConsole();
                userService = new formatUserService().formatUserService(userService);

                fileUtile = new TestFichier(stockCrypt.getPath(), userService);

                while(!fileUtile.validFile() && cpt<=3 )
                {
                    System.out.println("Erreur dans la saisie du nom du service de destination  \t"
                            + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\t");

                    secret = saisiStringConsole();
                    cpt++;
                }
                if(fileUtile.validFile())
                {
                    /// demande du nombre de bits de cryptage (128, 192 ou 256)
                    System.out.println("Saisir le nombre de bits d'encodage (128, 192 ou 256) : \t");

                    int numBitsTemp = saisiIntConsole();

                    // vérifier si le nombre de bits est compatible, sinon valeur par défaut
                    if(numBitsTemp == 128 || numBitsTemp== 256)
                        numBits=numBitsTemp;

                    /// demande du nombre de parts du secret à créer
                    System.out.println("Saisir le nombre de part de secret à créer (minimum 2 parts) : \t");

                    nbrSharedKey = saisiIntConsole();

                    // (vérifier que le nombre de part est supérieur à 2          ??????????????????   )
                    while (nbrSharedKey < 2 && cpt<=5)
                    {
                        System.out.println("Saisir le nombre de part minimu pour reconstruir le secret (minimum 2 parts) : \t");

                        nbrKeyUtil = saisiIntConsole();
                        cpt++;
                    }

                    if(cpt == 5)
                    {
                        fileUtile =null;
                        return;
                    }

                    do
                    {

                        System.out.println("Saisir le nombre de partage du secret à créer (minimum 2 parts) : \t");

                        nbrSharedKey = saisiIntConsole();

                    }while (nbrKeyUtil <= nbrSharedKey);

                    // // // création des parts de secret
                    secretBigI = fileUtile.getAesBigI();
                    shamirSecret = new ShamirSecret();
                    ShamKey = shamirSecret.generateKeys(nbrKeyUtil, nbrSharedKey, numBits, secretBigI);

// l'affichage à la console est fait depuis la méthode appelée

                    newDirectory = "4";
                }
                else
                {
                    newDirectory = "4";
                }
            }
            // // si révélation du secret
            if (newDirectory == "3")
            {
                String userSecret ="";
                ArrayList <BigInteger> keys = null;
                String tempX = "0";
                String tempFX = "0";
                int cptKey = 0;

                /// demande UserService
                System.out.println("Saisir le nom du service à accéder\t"
                        + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\t");

                userSecret = saisiStringConsole();
                userService = new formatUserService().formatUserService(userService);

                /// demande de la saisie des parts (jusqu'à ce que les utilisateurs signalent qu'ils ont saisi toutes les parts)
                do {
                    System.out.println("Saisir le x numéro " + cptKey + " :\t");
                    tempX = saisiStringConsole();
                    keys.add( new BigInteger (tempX ));

                    System.out.println("Saisir le Fx numéro " + cptKey + " :\t");
                    tempX = saisiStringConsole();
                    keys.add( new BigInteger (tempFX ));

                    cptKey++;

                }while(tempX != "" && tempFX !="");

                /// reconstruction shamir
                if(keys.isEmpty())
                    return;

				/*
				 Rechercher :

				 shamirKey [] en rapport avec le userService
				*/
				/*

				récupérer :

				 prime généré dans la méthode
				 ShamirKey[] generateKeys(int n, int t, int numBits, BigInteger secretBI)
				 de la classe shamirSecret

				 Puis :

				 sk2 = ShamirKey [] creaShamirKeys (BigInteger [] entry, BigInteger prime)
				 pour générer le tableau qui servira à tenter la reconstruction du shamir secret

				 Puis :
				 //RESOLUTION SHAMIR, calculate parameter 0 (secret)
		byte[] des = ShamirSecret.calculateLagrange(sk2);

		BigInteger secretFound = new BigInteger(des);
		System.out.println("Secret = " + secretFound);

		!!!!!!!!!!!!!!!!!!! Attention, je n'ai pas implémenter de test boolean
		pour dire si le secret est bien rerconstruit ou non
		Je sais seulement que la reconstruction fonctionne

				 (A vous les gars !!!!!!!!!!!!)
				Et si il faut que je fasse la partie test boolean dont je viens de parler, il n'y a pas de problème.


				 */

                /// reconstruction secret

				/*
				 A partir d'ici,
				 quand on a le BigInteger généré (reconstruit) juste avant, il suffit de  vérifier s'il existe un fichier qui le contient

				 String aesBI = BigIntegerREconstruit.toString().substring(1);

				 fileUtile = new TestFichier (stockCrypt.getPath());

	 			if (!fileUtile.testFile (String aesBI))
	 				return ;



				 l'inclure dans :

				 decodSecret.decrypt_sym(BigIntegerREconstruit)

				 */


                newDirectory = "4";
            }
            if (newDirectory == "4")
                return;
        }while(newDirectory != "1" && newDirectory != "2" && newDirectory != "3" );


    }

    static String saisiStringConsole ()
    {
        Scanner scan = new Scanner(System.in);
        String get  = scan.next();
        scan = null ;

        return get;
    }

    static int saisiIntConsole ()
    {
        Scanner scan = new Scanner(System.in);
        int get = scan.nextInt();
        scan = null ;

        return get;
    }

}


