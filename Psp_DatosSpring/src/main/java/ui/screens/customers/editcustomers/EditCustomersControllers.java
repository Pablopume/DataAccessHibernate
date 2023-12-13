package ui.screens.customers.editcustomers;

import common.Constants;
import jakarta.inject.Inject;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import ui.screens.common.BaseScreenController;

public class EditCustomersControllers extends BaseScreenController {
    @FXML
    public TextField idTxtField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;

    @FXML
    public TextField mailField;
    @FXML
    public TextField phoneField;
    @FXML
    public DatePicker dobField;
    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Customer, String> idCustomerColumn;
    @FXML
    public TableColumn nameCustomerColumn;
    @FXML
    public TableColumn surnameCustomerColumn;
    @FXML
    public TableColumn emailColumn;
    @FXML
    public TableColumn phoneColumn;
    @FXML
    public TableColumn dateOfBirthdayColumn;

    @Inject
    private EditCustomerViewModel editCustomerViewModel;

    public void handleCustomerSelection() {
        Customer selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            nameField.setText(selectedCustomer.getFirst_name());
            surnameField.setText(selectedCustomer.getLast_name());
            mailField.setText(selectedCustomer.getEmail());
            phoneField.setText(selectedCustomer.getPhone());
            dobField.setValue(selectedCustomer.getDob());
        }
    }


    public void initialize() {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        nameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.FIRST_NAME));
        surnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.LAST_NAME));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.EMAIL));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.PHONE));
        dateOfBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.DOB));
        customersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleCustomerSelection();
        });
        editCustomerViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListCustomers() != null) {

                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(newValue.getListCustomers());


                    }

                }

        );
        editCustomerViewModel.voidState();

    }

    @Override
    public void principalLoaded() {
        editCustomerViewModel.loadState();
    }

    public void editClient(ActionEvent actionEvent) {
        ObservableList<Customer> customers = customersTable.getItems();
        SelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        Customer selectedCustomer = selectionModel.getSelectedItem();
        editCustomerViewModel.getServices().update(new Customer(selectedCustomer.getId(),nameField.getText(),surnameField.getText(),mailField.getText(),phoneField.getText(),dobField.getValue())).get();
        customers.clear();
        customers.addAll(editCustomerViewModel.getServices().getAll().get());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Constants.USER_UPDATED);
        alert.setHeaderText(null);
        alert.setContentText(Constants.THE_USER_HAS_BEEN_UPDATED_CORRECTLY);
        alert.showAndWait();
    }
}
