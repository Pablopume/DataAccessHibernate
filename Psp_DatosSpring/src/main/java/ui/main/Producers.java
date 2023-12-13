package ui.main;

import common.Constants;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;


public class Producers {

    @Produces
    @Named("url")
    public String getUrl() {
        return Constants.JJJ;
    }

    @Produces
    @Named("configDB")
    public String getDB() {
        return Constants.JJJ1;
    }


}
