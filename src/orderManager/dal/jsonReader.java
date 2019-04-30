package orderManager.dal;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsonReader {

  public static void readFile() {
    JSONParser parser = new JSONParser();

    try {
      JSONObject object = (JSONObject) parser.parse(new FileReader("data.json"));

      JSONArray jArray = (JSONArray) object.get("AvailableWorkers");
      for (int i = 0; i < jArray.size(); i++) {
        JSONObject rec = (JSONObject) jArray.get(i);
        System.out.println("Name: "+rec.get("Name"));
        System.out.println("Initials: "+rec.get("Initials"));
        System.out.println("Salary Number: "+rec.get("SalaryNumber"));
        System.out.println("-----------------");
      }

    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
  }

}
