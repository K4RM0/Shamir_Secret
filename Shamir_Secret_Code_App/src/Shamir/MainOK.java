package Shamir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enKdeK.DecryptSecret;
import enKdeK.EncryptSecretText;
import verif.TestFichier;
import verif.formatUserService;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MainOK {

    final static File stockCrypt = new File ("Shamir_Secret_Code_App/stockageSecretCrypt");
    final static File stockClear = new File ("Shamir_Secret_Code_App/stockageSecretNonCrypt");
    final static File stockJson = new File ("Shamir_Secret_Code_App/JsonFile");
    //    final static int n = 9; //number of generate shares
//    final static int t = 5; //number of shares for solve the secret (t <= n)
    final static int numBits = 256; //number of bits of p  (à coisir entre 128 et 256)
    static TestFichier fileUtile = null ;
    static ShamirSecret shamirSecret = null;
    static ShamirKey [] ShamKey = null;
    static EncryptSecretText creaSecret = null;
    static DecryptSecret decodSecret = null;


    public static void main(String[] args) throws Exception {

        //       System.out.print(stockCrypt.getAbsolutePath());

        System.out.print(stockCrypt.getAbsolutePath());

        int newDirectory = 0;
        // // // demande si création secret ou si création nouvelles parts du secret ou si révélation secret
        do {
            String userService = "";
            int nbrBits = 256; // chiffrement à 256 bits par défaut si aucune saisie n'est faite
            String secret = "";
            String part2Secret = "";

            System.out.println("Bonjour, Que voulez-vous faire ? (Saisir le chiffre correspondant) \n"
                    + "(1) créer un secret \n"
                    + "(2) créer des parts de secret \n"
                    + "(3) révéler un secret \n"
                    + "(4) ne rien faire\n");

            newDirectory = saisiIntConsole ();

            // // Si création secret
            if(newDirectory == 1)
            {

                /// demande UserService

                System.out.println("Saisir le nom du service de destination du mot de passe \n"
                        + "(Ne pas mettre de caractère spéciaux en fin de ligne,  SVP.\n");

                userService = saisiStringConsole();

                userService = new formatUserService().formatUserService(userService);
                // vérifier si le UserService est déjà utilisé
                fileUtile = new TestFichier(stockCrypt.getAbsolutePath(), userService);

                if(!fileUtile.validFile())
                {
                    /// demande nombre de bits (128 ou 256(defaut))
                    System.out.println("Saisir le nombre de bits d'encodage (128 ou 256) : \n");

                    int nbrBitsTemp = saisiIntConsole();

                    // vérifier si le nombre de bits est compatible, sinon valeur par défaut
                    if(nbrBitsTemp == 128 || nbrBitsTemp== 256)
                        nbrBits=nbrBitsTemp;

                    /// demande de la saisie du secret (mini un caractère)
                    do {
                        System.out.println("Saisir la phase ou le mot secret que vous souhaitez\n");

                        secret = saisiStringConsole();

                    }while(secret.length()<0);
                    // vérifier que l'utilisateur a bien saisi au moins 1 caractère

                    // // // encodage du secret
                    creaSecret = new EncryptSecretText();
                    creaSecret.chiffrement_sym(userService, nbrBits, secret);
                    newDirectory = 4;
                }
                else
                {
                    System.out.println("Ce service a déjà un secret. Veuillez contacter votre responsable informatique.");
                    newDirectory = 4;
                }
            }
            // // création de parts d'un secret
            if (newDirectory == 2) {
                int cpt = 0;
                BigInteger secretBigI;
                int nbrSharedKey = 0;
                int nbrKeyUtil = 0;
                int numBits = 192; // valeur par défaut

                /// demande du UserService
                System.out.println("Saisir le nom du service de destination \n"
                        + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\n");

                userService = saisiStringConsole();
                userService = new formatUserService().formatUserService(userService);

                fileUtile = new TestFichier(stockCrypt.getPath(), userService);
                while (!fileUtile.validFile() && cpt <= 3) {
                    System.out.println("Erreur dans la saisie du nom du service de destination  \n" +
                            "Saisissez à nouveau le nom du service souhaité.\n"
                            + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\n");

                    userService = saisiStringConsole();
                    fileUtile = new TestFichier(stockCrypt.getPath(), userService);
                    cpt++;

                    System.out.println(fileUtile.takeFile().getAbsolutePath());

                }
                cpt = 0;

                if (fileUtile.validFile()) {
                    /// demande du nombre de bits de cryptage (128, 192 ou 256)
                    System.out.println("Saisir le nombre de bits d'encodage (128, 192 ou 256) : \n");

                    int numBitsTemp = saisiIntConsole();

                    // vérifier si le nombre de bits est compatible, sinon valeur par défaut
                    if (numBitsTemp == 128 || numBitsTemp == 256)
                        numBits = numBitsTemp;


                    // (vérifier que le nombre de part est supérieur à 2          ??????????????????   )
                    while (nbrKeyUtil < 2) {
                        System.out.println("Saisir le nombre de part minimum pour reconstruire le secret (minimum 2 parts) : \n");

                        nbrKeyUtil = saisiIntConsole();
                        cpt++;
                        if (cpt > 3) {
                            fileUtile = null;
                            return;
                        }
                    }

                    do {

                        System.out.println("Saisir le nombre de partage du secret à créer (au minimum égal nombre de part pour reconstruire le secret) : \n");

                        nbrSharedKey = saisiIntConsole();

                    } while (nbrKeyUtil >= nbrSharedKey);

                    // // // création des parts de secret
                    secretBigI = fileUtile.getAesBigI();

                    shamirSecret = new ShamirSecret();

                    ShamKey = shamirSecret.generateKeys(nbrSharedKey, nbrKeyUtil, numBits, secretBigI,userService);
                }
            }
                if(newDirectory == 3)
                {
                // // //////////////////////////////révélation du secret //////////////////////////////////

                int cpt =0;
                ShamirSecret shamirSecret = new ShamirSecret();

                /// demande du UserService
                System.out.println("Saisir le nom du service de destination \n"
                        + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\n");

                userService = saisiStringConsole();
                userService = new formatUserService().formatUserService(userService);

                fileUtile = new TestFichier(stockJson.getPath(), userService);
                TestFichier fileUtileTestBigInt = new TestFichier(stockCrypt.getPath(), userService);

                while(!fileUtile.JsonFile() && cpt<=3 )
                {
                    System.out.println("Erreur dans la saisie du nom du service de destination  \n" +
                            "Saisissez à nouveau le nom du service souhaité.\n"
                            + "(Ne pas mettre de caractère spéciaux en fin de ligne, SVP.\n");

                    userService = saisiStringConsole();
                    fileUtile = new TestFichier(stockJson.getPath(), userService);
                    fileUtileTestBigInt = new TestFichier(stockCrypt.getPath(), userService);
                    cpt++;
                }
                cpt = 0;

                // restitution des donnéees stockées dans Json
                    File fileJ = fileUtile.takeJson();

                    // Extraction des données sauvegardées dans le fichier Json
                    Gson decryptGson = new GsonBuilder().create ();
                    ArrayList<ShamirKey> ShamKey  = null ;
                    Type ListType = new TypeToken<ArrayList<ShamirKey>>(){}.getType();

                    InputStream iStream = new FileInputStream(fileJ.getPath());
                    InputStreamReader irJ= new InputStreamReader(iStream);
                    BufferedReader buff=new BufferedReader(irJ);
                    ShamKey  = decryptGson.fromJson(buff, ListType /* ShamirKey [].class*/  );
                    buff.close();

                    // But didactique pour être sûr
                    // que les données récupérées soient les mêmes que celles sauvegardées
/*                    for(int i=1; i< ShamKey.size(); i++)
                    {
                        System.out.println(i+"-> x" + i + " = " +ShamKey.get(i).getF());
                        System.out.println(i+"-> f(x" + i + ") = " +ShamKey.get(i).getF());
                        System.out.println("Prime "+ ShamKey.get(i).getP());
                    }
*/
                    // Conversion ArrayList en ShamirKey []
                    ShamirKey [] keys = new ShamirKey[ShamKey.size()];
                    for (int i=0; i< ShamKey.size(); i++)
                    {
                        keys [i] = ShamKey.get(i);
                    }

				 //RESOLUTION SHAMIR, calculate parameter 0 (secret)
                byte[] des = shamirSecret.calculateLagrange(keys);
                BigInteger secretFound = new BigInteger(des);
                String aesBI = secretFound.toString(32);

                if(fileUtileTestBigInt.validFile()) {
                    if (fileUtileTestBigInt.getAesBigI().compareTo(BigInteger.ONE) < 0) {
                        secretFound = secretFound.negate();
                        aesBI = "0" + aesBI;
                    }
                }
                /// reconstruction secret
                decodSecret = new DecryptSecret();

                // vérification du BigInteger trouvé
                if (!fileUtileTestBigInt.testFile (aesBI))
                    return ;

                decodSecret.decrypt_sym(secretFound, userService);

                newDirectory = 4;
            }
            newDirectory = 4;
            return;
        }while(newDirectory != 1 && newDirectory != 2 && newDirectory != 3 );


    }

    static String saisiStringConsole ()
    {
        Scanner scan = new Scanner(System.in);
        String get  = scan.nextLine();
        //   scan = null ;

        return get;
    }

    static int saisiIntConsole ()
    {
        Scanner scan = new Scanner(System.in);
        int get = scan.nextInt();
        //   scan = null ;

        return get;
    }


}
