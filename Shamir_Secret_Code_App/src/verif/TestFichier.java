package verif;

//public class TestFichier {
//}

import java.io.File;
import java.math.BigInteger ;

public class TestFichier extends TestChemin {

    /**
     * Test si fichier existe
     *
     * Entrée : nom fichier
     *
     */
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
//				testName = item.getPath().substring(item.getPath().lastIndexOf("\\")+1,item.getPath().indexOf("_") );
                testName = item.getPath().substring(item.getPath().indexOf("_")+1,item.getPath().lastIndexOf("-") );

                if (testName.equals(fileName)) {
                    this.prospectFile = item ;
                    this.userService = testName;
                    this.aesBigI = new BigInteger(/*"-"+*/item.getPath().substring(item.getPath().lastIndexOf("\\")+1, item.getPath().indexOf("_") ));
                    this.aesKey = Integer.parseInt(item.getPath().substring(item.getPath().lastIndexOf("-")+1, item.getPath().length() ));
                    System.out.println(aesBigI.toString());

                }
                else
                {
                    System.out.println("Le fichier n'existe pas.");
                }
            }
        }
        return true ;
    }

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
//				testName = item.getPath().substring(item.getPath().lastIndexOf("\\")+1,item.getPath().indexOf("_") );
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
     * Affiche le nom du fichier recherché (utile seuleement pour vérifier si le fichier existe et savoir l'encodage AES)
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

    public void listUserService()
    {
        if(!testFile())
        {
            System.out.println("Aucun fichier créé !");
            return ;
        }
        File[] fList = file.listFiles();


        for (File item : fList)
        {
            System.out.println(item.getPath());
//			System.out.println(item.getPath().substring(0,item.getPath().indexOf("_") );
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
