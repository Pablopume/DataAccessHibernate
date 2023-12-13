package ui.screens.common;


import jakarta.inject.Inject;
import lombok.Getter;
import ui.screens.principal.PrincipalController;

public class BaseScreenController {
    @Inject
    private PrincipalController principalController;

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public PrincipalController getPrincipalController() {
        return principalController;
    }



    public void principalLoaded() {

    }
}
