package verif;

import java.io.File;
import java.util.Scanner;

/**
 * Classe héritant de TestChemin
 * Teste si dossier existe et en crée un si besoin
 * et teste si dossier vide
 */
public class TestDossier extends TestChemin {
    public TestDossier (String path)
    {
        super(path);
        testDir();

    }

    public String getPath ()
    {
        return super.getPath();
    }

    public boolean testDir ()
    {
        if (!file.isDirectory())
        {
            creaDir() ;
        }

        return true;
    }

    private boolean creaDir ()
    {
        String newDirectory = null ;
        System.out.println("Le dossier référent n'exitse pas. \t"
                + "Voulez-vous en créer un ? (Oui, Non)");
        Scanner scan = new Scanner(System.in);
        newDirectory = scan.nextLine();
        if(newDirectory.toLowerCase() == "oui") {
            file.mkdirs();
            return true;
        }
        else
            return false;

    }

    public boolean emptyDir ()
    {
        File[] fList = file.listFiles();

        if(testDir())
        {
            if (fList.length == 0){
                return true ;
            }
        }
        return false;
    }
}
