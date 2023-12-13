package configuration;


import common.Constants;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Singleton
public class Configuration {

    private Properties p;

    private Configuration() {
        Path p1 = Paths.get(Constants.SRC_MAIN_RESOURCES_MYSQL_PROPERTIES_XML);
        p = new Properties();
        InputStream propertiesStream;
        try {
            propertiesStream = Files.newInputStream(p1);
            p.loadFromXML(propertiesStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String clave) {
        return p.getProperty(clave);
    }


}
