package enKdeK;

import java.util.Random;

/**
 * Classe apportant une méthode pour générer une clef
 * qui sera mise en référence à la part x stocké dans le tableau de SHamirKey
 * et qui sera donné au utilisateur
 * (en cas de départ d'un utilisateur, seule cette valeur pourra être échangée et on pourra conserver les parts de Shamir gérérées initialement)
 */
public class NbrAleaHexadecimForJson {

    public String creaHexaAlea() {
    Random random = new Random();
    int nextInt = random.nextInt(512 * 512 * 512);
    return (String.format("#%06x",nextInt));
    }
}
