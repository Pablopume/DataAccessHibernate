package ui.screens.customers.deletecustomer;

import common.Constants;
import jakarta.inject.Inject;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.errors.CustomerError;
import ui.screens.common.BaseScreenController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DeleteCustomersController extends BaseScreenController {
    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Customer, Integer> idCustomerColumn;
    @FXML
    public TableColumn<Customer, String> nameCustomerColumn;
    @FXML
    public TableColumn<Customer, String> surnameCustomerColumn;
    @FXML
    public TableColumn<Customer, String> emailColumn;
    @FXML
    public TableColumn<Customer, String> phoneColumn;
    @FXML
    public TableColumn<Customer, LocalDate> dateOfBirthdayColumn;
    @FXML
    public Button buttonCostumer;
    public TableView ordersCustomer;
    public TableColumn product;
    public TableColumn idProduct;
    public TableColumn price;
    public TableView<Order> orderssTable;
    public TableColumn<Order, Integer> idOrder;
    public TableColumn<Order, LocalDate> orderDate;
    public TableColumn<Order, Integer> customerId;
    public TableColumn<Order, Integer> tableId;

    @Inject
    private DeleteCustomerViewModel deleteCustomerViewModel;

    public void initialize() {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.ID2));
        nameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.FIRST_NAME2));
        surnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.LAST_NAME2));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.EMAIL2));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.PHONE2));
        dateOfBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.DOB2));
        idOrder.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        orderDate.setCellValueFactory(new PropertyValueFactory<>(Constants.DATE));
        customerId.setCellValueFactory(new PropertyValueFactory<>(Constants.CUSTOMER_ID));
        tableId.setCellValueFactory(new PropertyValueFactory<>(Constants.TABLE_ID));
        deleteCustomerViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListCustomers() != null) {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(newValue.getListCustomers());
                    }

                }

        );
        customersTable.setOnMouseClicked(event -> {
            SelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
            Customer selectedCustomer = selectionModel.getSelectedItem();
            if (selectedCustomer != null) {
                orderssTable.getItems().clear();
                orderssTable.getItems().addAll(deleteCustomerViewModel.getServicesOrder().getOrdersByCustomerId(selectedCustomer.getId()));
            }
        });
        deleteCustomerViewModel.voidState();

    }

    @Override
    public void principalLoaded() {
        deleteCustomerViewModel.loadState();
    }

    public void deleteCustomer(ActionEvent actionEvent) {
        SelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        Customer selectedCustomer = selectionModel.getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Delete Customer");
            alert.setContentText("Do you want to delete this customer?");
            Optional<ButtonType> res = alert.showAndWait();
            res.ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    deleteCustomerViewModel.getServices().delete(selectedCustomer, false).peek(customerInt -> {
                                if (customerInt == 0) {
                                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                                    a.setTitle("Customer deleted");
                                    a.setHeaderText(null);
                                    a.setContentText("The customer has been deleted successfully");
                                    a.show();
                                    customersTable.getItems().remove(selectedCustomer);
                                }
                            })
                            .peekLeft(customerError -> {
                                if (customerError.getNumError() == 2) {
                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                    alert2.getButtonTypes().remove(ButtonType.OK);
                                    alert2.getButtonTypes().add(ButtonType.CANCEL);
                                    alert2.getButtonTypes().add(ButtonType.YES);
                                    alert2.setTitle("Delete Customer with orders");
                                    alert2.setContentText("Do you want to delete this customer and its orders?");
                                    Optional<ButtonType> res2 = alert2.showAndWait();
                                    res2.ifPresent(buttonType2 -> {
                                        if (buttonType2 == ButtonType.YES) {
                                            deleteCustomerViewModel.getServicesOrder().getOrdersByCustomerId(selectedCustomer.getId()).forEach(order -> {
                                                List<OrderItem> orderItems = deleteCustomerViewModel.getOrderItemService().getOrdersById(order.getId());
                                                order.setOrderItemList(orderItems);
                                                deleteCustomerViewModel.getServices().save(order);
                                            });

                                            deleteCustomerViewModel.getServices().delete(selectedCustomer, true).peek(result -> {
                                                        if (result == 0) {
                                                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                                                            a.setTitle("Customer deleted");
                                                            a.setHeaderText(null);
                                                            a.setContentText("The customer has been deleted");
                                                            a.show();
                                                            customersTable.getItems().remove(selectedCustomer);
                                                        }
                                                    })
                                                    .peekLeft(customerError2 -> getPrincipalController().sacarAlertError(customerError2.getMessage()));
                                        }
                                    });
                                } else {
                                    getPrincipalController().sacarAlertError(customerError.getMessage());
                                }
                            });
                }
            });
        }
    }
}



