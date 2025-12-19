package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public static String propertyReader(String filePath, String key) {
        String value = null;

        //inputStream is required when loading into properties
        try (InputStream input = new FileInputStream(filePath)) {

            //Object creation for property class
            Properties prop = new Properties();

            //load a properties file i.e. read the property file
            prop.load(input);

            // getProperty will fetch the value acc to the key
            value = prop.getProperty(key);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
