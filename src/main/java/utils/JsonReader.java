package utils;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

// https://automationreinvented.blogspot.com/2020/05/how-to-fetch-testdata-from-json-file.html

public class JsonReader {

    public static JSONObject getJsonData() throws IOException, ParseException {

        //pass the path of the testdata.json file
        File fileName = new File("resources//TestData//testdata.json");

        //convert json file to string
        String jsonString = FileUtils.readFileToString(fileName, "UTF-8");

        //parse the string into object
        Object obj = new JSONParser().parse(jsonString);

        //convert it to jsonobject so that I can return it to the function everytime it gets called
        JSONObject jsonObject = (JSONObject) obj;

        return jsonObject;

        //read the file -> convert into string -> convert string into object -> convert object into JsonObject
    }

    public static String getTestData(String key) throws IOException, ParseException {
        String testData;
        return testData = (String) getJsonData().get(key);
    }
}
