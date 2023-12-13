package ui.screens.customers.showcustomers;

import common.Constants;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import ui.screens.common.BaseScreenController;

import java.time.LocalDate;

public class ShowCustomersController extends BaseScreenController {
    @FXML
    private TableColumn<Customer, Integer> idCustomerColumn;
    @FXML
    private TableColumn<Customer, String> nameCustomerColumn;
    @FXML
    private TableColumn<Customer, String> surnameCustomerColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, LocalDate> dateOfBirthdayColumn;
    @FXML
    private TableView<Customer> customersTable;
    @Inject
    private ShowCustomersViewModel custViewModel;


    public void initialize() {
        if (!custViewModel.getServices().getAll().isEmpty()) {
            customersTable.getItems().setAll(custViewModel.getServices().getAll().get());
        }
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        nameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.FIRST_NAME));
        surnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.LAST_NAME));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.EMAIL));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.PHONE));
        dateOfBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.DOB));
        custViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListCustomers() != null) {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(newValue.getListCustomers());
                    }

                }

        );
        custViewModel.voidState();

    }

    @Override
    public void principalLoaded() {
        custViewModel.loadState();
    }
}
