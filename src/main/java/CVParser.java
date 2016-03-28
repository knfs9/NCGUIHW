

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
//todo createJsonFile
public class CVParser {
    private JSONParser jsonParser;
    JSONObject jsonObject;
    public void parse(String file){
        try {
            FileReader reader = new FileReader(file);
            jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFirstName(){
        return (String) jsonObject.get("firstname");
    }
    public String getMiddleName(){
        return (String) jsonObject.get("middlename");
    }
    public String getLastName(){
        return (String) jsonObject.get("lastname");
    }
    public String getDateOfBirth(){
        return (String) jsonObject.get("dateOfBirth");
    }
    public String getPhone(){
        return (String) jsonObject.get("phone");
    }
    public String getLanguages(){
        JSONArray languages = (JSONArray) jsonObject.get("languages");
        StringBuilder sb = new StringBuilder();

        Iterator it = languages.iterator();
        while (it.hasNext()){
            JSONObject innerObj = (JSONObject) it.next();
            sb.append((String)innerObj.get("lang") + ": ");
            sb.append((String)innerObj.get("knowledge"));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getSkills(){
        JSONArray skills = (JSONArray) jsonObject.get("skills");
        StringBuilder sb = new StringBuilder();
        for(Object obj : skills){
            sb.append(obj + ", ");
        }
        return sb.toString();
    }

}
