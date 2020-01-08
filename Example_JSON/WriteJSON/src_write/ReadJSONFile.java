import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class ReadJSONFile {

    public static void main(String[] args) {

        JSONParser parser = new JSONParser();

        try
        {
         Object obj = parser.parse(new FileReader("/jsonExample.json"))   ;
         JSONObject jsonObject = (JSONObject) obj ;
         String name = (String) jsonObject.get("name");
         System.out.println("Name is: " + name);

         String location = (String) jsonObject.get("location");
         System.out.println("Location is: " + location);

         //loop Array

            JSONArray coursesArray = (JSONArray) jsonObject.get("courses");
            Iterator<String> iterator = coursesArray.iterator();

            while(iterator.hasNext()) {

                System.out.println("Course : "+ iterator.next());
            }
        }
        catch(FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        catch (ParseException e) {e.printStackTrace();}
        catch (Exception e){e.printStackTrace();}



    }

}
