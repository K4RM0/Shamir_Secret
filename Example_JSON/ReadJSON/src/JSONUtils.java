import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

public class JSONUtils {

    public static String getJSONStringFromFile(String path) {
        Scanner scanner;
        InputStream in = Filehandle.inputStreamFromFile(path);
        scanner = new Scanner(in);
        String json = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return json;
    }

    public static JSONObject getJSONObjectFromFile(String path) {
        return new JSONObject(getJSONStringFromFile(path));
    }

    public static boolean objectExists(JSONObject jsonObject, String key) {
        Object o;
        try {
            o = jsonObject.get(key);
        } catch(Exception e) {
            return false;
        }
        return o != null;
    }

}