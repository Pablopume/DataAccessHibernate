package ui.screens.login;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Credentials;
import services.LoginServices;
import ui.screens.common.BaseScreenController;

public class LoginController extends BaseScreenController {
    public PasswordField txtPassword;
    public TextField txtUserName;
    private final LoginServices services;
    public AnchorPane pantallaLogin;

    @Inject
    public LoginController(LoginServices services) {

        this.services = services;
    }
    public void doLogin(ActionEvent actionEvent) {
      Credentials credentials=  services.getByNameAndPassword(txtUserName.getText(), txtPassword.getText());

        if (credentials!= null) {
            getPrincipalController().onLoginDone(credentials);
            pantallaLogin.setVisible(false);

        }
    }
}
