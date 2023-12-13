package ui.screens.principal;


import common.Constants;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import model.Credentials;
import ui.screens.common.BaseScreenController;
import ui.screens.common.Screens;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Data
public class PrincipalController {

    public MenuItem addOrders;
    public Menu menuCustomers;
    public MenuItem deleteOrders;

    private Credentials actualUser;
    @FXML
    private Menu menuHelp;

    Instance<Object> instance;

    @FXML
    private MenuBar menuPrincipal;
    private Stage primaryStage;

    @FXML
    public BorderPane root;


    private final Alert alert;




    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
    }

    private void loadScreen(Screens pantalla) {
        cambioPantalla(loadScreen(pantalla.getRoute()));

    }


    public void sacarAlertError(String mensaje) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.getDialogPane().setId(Constants.ALERT);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(Constants.BTN_OK);
        alert.showAndWait();
    }


    private Pane loadScreen(String ruta) {
        Pane panePantalla = null;
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(ruta));
            BaseScreenController pantallaController = fxmlLoader.getController();
            pantallaController.setPrincipalController(this);
            pantallaController.principalLoaded();


        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return panePantalla;
    }


    public void logout() {
        actualUser = null;
        menuPrincipal.setVisible(false);
        loadScreen(Screens.LOGIN);
    }

    private void cambioPantalla(Pane pantallaNueva) {
        root.setCenter(pantallaNueva);
    }


    public void initialize() {
        menuPrincipal.setVisible(false);
        loadScreen(Screens.LOGIN);

    }

    private void closeWindowEvent(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle(Constants.QUIT_APPLICATION);
        alert.setContentText(Constants.CLOSE_WITHOUT_SAVING);
        alert.initOwner(primaryStage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();


        res.ifPresent(buttonType -> {
            if (buttonType == ButtonType.CANCEL) {
                event.consume();
            }
        });
    }


    public void exit() {

        primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    @FXML
    private void cambiarcss() {
        primaryStage.getScene().getRoot().getStylesheets().clear();


        primaryStage.getScene().getRoot().getStylesheets().add(getClass().getResource(Constants.CSS_DARKMODE_CSS).toExternalForm());

    }


    public double getHeight() {
        return root.getScene().getWindow().getHeight();
    }

    public double getWidth() {
//        return 600;
        return root.getScene().getWindow().getWidth();
    }


    @FXML
    private void menuClick(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case Constants.MENU_ITEM_PANTALLA_1:
                loadScreen(Screens.SCREENCUSTOMERS);
                break;

            case Constants.MENU_ITEM_PANTALLA_NUEVA:
                loadScreen(Screens.NEWSCREEN);
                break;
            case Constants.MENU_ITEM_LOGOUT:
                logout();
                break;
        }


    }


    public void onLoginDone(Credentials usuario) {
        actualUser = usuario;
        menuPrincipal.setVisible(true);
        if(usuario.getId()==-1){
            addOrders.setVisible(false);
            menuCustomers.setVisible(true);
            deleteOrders.setVisible(true);
        }else {
            addOrders.setVisible(true);
            menuCustomers.setVisible(false);
            deleteOrders.setVisible(false);

        }

        //menuHelp.setVisible(false);


    }

    public void menuCustomers(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case Constants.SHOW_CUSTOMERS -> loadScreen(Screens.SCREENCUSTOMERS);
            case Constants.ADD_CUSTOMERS -> loadScreen(Screens.SCREENADD);
            case Constants.DELETE_CUSTOMERS -> loadScreen(Screens.SCREENREMOVE);
            case Constants.UPDATE_CUSTOMERS -> loadScreen(Screens.SCREENEDIT);
        }
    }

    public void menuOrders(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case Constants.SHOW_ORDERS -> loadScreen(Screens.PANTALLAORDERS);
            case Constants.ADD_ORDERS -> loadScreen(Screens.ADDORDERS);
            case Constants.DELETE_ORDERS -> loadScreen(Screens.REMOVEORDERS);
            case Constants.UPDATE_ORDERS -> loadScreen(Screens.EDITORDERS);
        }
    }


}
