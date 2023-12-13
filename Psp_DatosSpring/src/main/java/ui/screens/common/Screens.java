package ui.screens.common;

public enum Screens {

    SCREENCUSTOMERS(common.Constants.FXML_CUSTOMERS_LIST_CUSTOMERS_FXML),
    SCREENADD(common.Constants.FXML_CUSTOMERS_ADD_CUSTOMERS_FXML),
    LOGIN(common.Constants.FXML_LOGIN_FXML),
    SCREENREMOVE(common.Constants.FXML_CUSTOMERS_DELETE_CUSTOMERS_FXML),
    SCREENEDIT(common.Constants.FXML_CUSTOMERS_EDIT_CUSTOMERS_FXML),
    PANTALLAORDERS(common.Constants.FXML_ORDERS_LIST_ORDERS_FXML),
    ADDORDERS(common.Constants.FXML_ORDERS_ADD_ORDERS_FXML),

    REMOVEORDERS(common.Constants.FXML_ORDERS_DELETE_ORDERS_FXML),
    EDITORDERS(common.Constants.FXML_ORDERS_EDIT_ORDERS_FXML),

    NEWSCREEN(ScreenConstants.FXML_PANTALLA_NUEVA_FXML);

    private String route;

    Screens(String ruta) {
        this.route = ruta;
    }

    public String getRoute() {
        return route;
    }


    private static class Constants {
    }
}
