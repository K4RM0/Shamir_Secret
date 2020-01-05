package enKdeK;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DfileName implements TextEncryptor{

    private final static File stockCrypt = new File ("D:/Cours/3eme semestre/Math/Projet/Test");
    private final static File stockClear = new File ("D:/Cours/3eme semestre/Math/Projet/TempSecr");
    private static final String complement = "/ka" ;


    /*	public TransfStringEtInv(byte[] key) {
        //	this.enc = key;
        //	this.dec = key;
        }

        public TransfStringEtInv(String utf8Key) {
            this(utf8Key.getBytes(StandardCharsets.UTF_8));
        }
    */
    public DfileName(String text) {

    }

    @Override
    public String encrypt(String text) {

        String [] tabInit = new String [3];
        String [] dispatch = new String [3] ;
        String newString = null;

        text = text + complement.substring(0,text.length()%3);

        tabInit [0] = text.substring (0,text.length()/3);
        tabInit [1] = text.substring((text.length()/3), (text.length()*2/3));
        tabInit [2] = text.substring (text.length()*2/3,text.length());

        dispatch [0] = tabInit [1];
        dispatch [1] = tabInit [0];
        dispatch [2] = tabInit [2];

        newString = dispatch[0] + dispatch[1] + dispatch[2];

        byte[] step1 = newString.getBytes(StandardCharsets.UTF_8);
        byte[] step2 = Base64.encode( step1 );
        String pwd = new String(  step2, StandardCharsets.UTF_8 );
        pwd = pwd.substring(0, pwd.length()-1);
        return pwd ;
    }

    @Override
    public String decrypt(String encryptedText) {
        String [] tabInit = new String [3];
        String [] inv = new String [3] ;
        String newString = null;

        byte[] step3 = ( encryptedText+"=").getBytes(StandardCharsets.UTF_8);
        byte[] step2 = Base64.decode( step3 );
//		byte[] step1 = dec.apply( step2 );

        newString = new String( step2, StandardCharsets.UTF_8 );
        System.out.println(newString);
        inv [0] = newString.substring (0,(newString.length()/3)+1);
        inv [1] = newString.substring((newString.length()/3)+1, (newString.length()*2/3));
        inv [2] = newString.substring ((newString.length()*2/3),newString.length());

        tabInit [0] = inv [1];
        tabInit [1] = inv [0];
        tabInit [2] = inv [2];

        newString = tabInit[0] + tabInit[1] + tabInit[2];

        newString = newString.substring(0, newString.indexOf("/"));

        return newString ;
    }

}
