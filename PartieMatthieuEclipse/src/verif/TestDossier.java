package verif;

import java.io.File;
import java.util.Scanner;

public class TestDossier extends TestChemin {


    /**
     * Test si dossier existe
     *
     * Entrée : nom dossier
     *
     * Apelle : décryptage nom du dossier
     *
     */

    public TestDossier (String path)
    {
        super(path);
        creaDir();

    }

    public String getPath ()
    {
        return super.getPath();
    }

    public boolean testDir ()
    {
        if (!file.isDirectory())
        {
            return false ;
        }

        return true;

    }

    private void creaDir ()
    {
        String newDirectory = null ;

        if(!testDir())
        {
            while(!testDir()) {
                System.out.println("Le dossier référent n'exitse pas. \t"
                        + "Voulez-vous en créer un ? (Oui, Non)");
                Scanner scan = new Scanner(System.in);
                newDirectory = scan.next();
                if(newDirectory.toLowerCase() == "oui")
                {
                    file.mkdirs();
                }
                else
                    return;
            }
        }
    }

    public boolean emptyDir ()
    {
        File[] fList = file.listFiles();

        if(testDir())
        {
            if (fList.length == 0){
                System.out.println("Liste vide");
                return false ;
            }
        }
        return true;
    }
}
