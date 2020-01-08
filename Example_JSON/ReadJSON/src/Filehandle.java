import java.io.InputStream;

public class Filehandle {

    public static InputStream inputStreamFromFile(String path){
        try{
            InputStream inputStream = Filehandle.class.getResourceAsStream(path);
            return inputStream;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

}
