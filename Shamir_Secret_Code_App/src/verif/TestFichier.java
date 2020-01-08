package verif;

//public class TestFichier {
//}

import java.io.File;
import java.math.BigInteger ;

/**
 * Classe héritant de TestChemin
 * Teste le fichier considéré (userService ou BigInteger)
 * et récupére certaine données au passage
 */
public class TestFichier extends TestChemin {

    private TestDossier dossier = null ;
    private String fileName ;
    private String dirPath  ;
    private String userService ;
    private int aesKey ;
    private File prospectFile;
    private BigInteger aesBigI ;

    public TestFichier(String dirPath, String fileName, int key)
    {
        super (dirPath);

        this.dirPath = dirPath ;
        this.dossier = new TestDossier(dirPath);
        this.fileName = fileName ;
    }

    public TestFichier(String dirPath, String fileName)
    {
        super (dirPath);

        this.dirPath = dirPath ;
        this.dossier = new TestDossier(dirPath);
        this.fileName = fileName ;

    }

    public TestFichier(String dirPath)
    {
        super (dirPath);

        this.dirPath = dirPath ;
        this.dossier = new TestDossier(dirPath);

    }

    /**
     * Test si le dossier contient des fichiers
     * Et sélectionne le fichier recherhé
     * En même temps, il y a récupération de données : le fichier, le userService lié, le BigInteger lié, la clef AES liée
     * @return
     */
    private boolean testFile ()
    {
        File[] fList = file.listFiles();
        String testName = null ;

        if (dossier.emptyDir()){
            System.out.println("Error systemFile");
            return false ;
        }
        else
        {
            for (File item : fList)
            {
                testName = item.getPath().substring(item.getPath().indexOf("_")+1,item.getPath().lastIndexOf("-") );

                if (testName.equals(fileName)) {
                    this.prospectFile = item ;
                    this.userService = testName;
                    this.aesBigI = new BigInteger(/*"-"+*/item.getPath().substring(item.getPath().lastIndexOf("\\")+1, item.getPath().indexOf("_") ));
                    this.aesKey = Integer.parseInt(item.getPath().substring(item.getPath().lastIndexOf("-")+1, item.getPath().length() ));
                }
                else
                {
                    System.out.println("Le fichier n'existe pas.");
                }
            }
        }
        return true ;
    }

    /**
     * Teste si le fichier contient dans son nom, le BigInteger recherché
     * En même temps, il y a récupération de données : le fichier, le userService lié, clef AES liée
     * @param bigInt
     * @return
     */
    public boolean testFile (String bigInt)
    {
        File[] fList = file.listFiles();
        String testBigI = null ;

        if (dossier.emptyDir()){
            System.out.println("Error systemFile");
            return false ;
        }
        else
        {
            for (File item : fList)
            {
                testBigI = item.getPath().substring(item.getPath().lastIndexOf("\\")+1, item.getPath().indexOf("_") );
                if (testBigI.equals(bigInt)) {
                    this.prospectFile = item ;
                    this.aesBigI = new BigInteger(/*"-"+*/item.getPath().substring(item.getPath().lastIndexOf("\\")+1, item.getPath().indexOf("_") ));
                    this.userService = item.getPath().substring(item.getPath().indexOf("_")+1,item.getPath().lastIndexOf("-") );
                }
                else
                {
                    System.out.println("Vous n'avez pas fourni les bonnes informations");
                    return false;
                }
            }
        }
        return true ;
    }

    /**
     * Test si le dossier contient un fichier avec le même UserService
     *
     * @return
     */
    public boolean validFile ()
    {
        File[] fList = file.listFiles();
        String testName = null ;

        if (dossier.emptyDir()){
            return false ;
        }
        else
        {
            for (File item : fList)
            {
                testName = item.getPath().substring(item.getPath().lastIndexOf("_")+1,item.getPath().lastIndexOf("-") );
                if (testName.equals(fileName)) {
                    testFile();
                    return true;
                }
            }
        }

        return false ;
    }

    /**
     * retourne le fichier à consulter
     *
     */
    public File takeFile()
    {
        if(!testFile())
            System.out.println("Le fichier n'existe pas.");

        return prospectFile;
    }

    /**
     * Affiche l'adresse du fichier recherché (utile seuleement pour vérifier si le fichier existe et savoir l'encodage AES)
     *
     */
    public void viewFile ()
    {
        if(!testFile()) {
            System.out.println("Le fichier n'existe pas.");
            return ;
        }

        System.out.println(prospectFile.getPath());

    }

    /**
     * Lister tous fichiers (adresse) pour les userService déjà existants
     */
    public void listUserService()
    {
        if(!testFile())
        {
            System.out.println("Aucun fichier trouvé !");
            return ;
        }
        File[] fList = file.listFiles();


        for (File item : fList)
        {
            System.out.println(item.getPath());
        }

    }

    /**
     * Création du nom du fichier pour cryptage de ce même nom
     * @return
     */
    public String creaFile ()
    {
        return super.getPath()+"/" + fileName ;
    }

    public String getUserService()
    {
        return userService;
    }

    public int getAes()
    {
        return aesKey;
    }

    public BigInteger getAesBigI()
    {
        return aesBigI;
    }
}
