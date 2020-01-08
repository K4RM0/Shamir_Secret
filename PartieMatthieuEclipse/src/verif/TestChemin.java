package verif;

import java.io.File;

public class TestChemin {

    private String chemin ;
    protected File file = null;

    /**
     * Classe mère permettant de créer un fichier de travail
     * et faire héritage d'une méthode récupérant l'adresse de ce fichier
     * @param chemin
     */
    public TestChemin (String chemin)
    {
        this.chemin = chemin ;

        this.file = new File(chemin);

    }

    protected String getPath()
    {
        return chemin ;
    }

}
