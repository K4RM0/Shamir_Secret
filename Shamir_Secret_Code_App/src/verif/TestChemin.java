package verif;

import java.io.File;

public class TestChemin {

    private String chemin ;
    protected File file = null;

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
