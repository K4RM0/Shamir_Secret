package enKdeK;

import java.util.Random;

public class NbrAleaHexadecimForJson {

    public String creaHexaAlea() {
    Random random = new Random();
    int nextInt = random.nextInt(512 * 512 * 512);
    return (String.format("#%06x",nextInt));
    }
}
