import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class CreateJSONFile {

    public static void main(String[] args) {

        JSONObject obj = new JSONObject();
        obj.put("name", "Kevin Arguello");
        obj.put("location", "Switzerland");
        // tableau

        JSONArray list = new JSONArray();
        list.add("Java");
        list.add("JSP");
        list.add("Exemple");

        obj.put("courses", list);

        try(FileWriter file = new FileWriter("/jsonExample.json"))
        {
            file.write(obj.toString());
            file.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(obj);
    }
}
