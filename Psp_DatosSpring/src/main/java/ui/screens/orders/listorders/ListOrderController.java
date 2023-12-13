package ui.screens.orders.listorders;

import common.Constants;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import ui.screens.common.BaseScreenController;
import ui.screens.principal.PrincipalController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


public class ListOrderController extends BaseScreenController {


    public TableColumn<OrderItem, Double> priceT;
    ListOrderViewModel listOrderViewModel;
    @Inject
    public ListOrderController(ListOrderViewModel listOrderViewModel,PrincipalController principalController) {
        this.listOrderViewModel = listOrderViewModel;
    }

    @FXML
    public TableView<Order> customersTable;
    @FXML
    public TableColumn<Order, Integer> idOrder;
    @FXML
    public TableColumn<Order, LocalDateTime> orderDate;

    @FXML
    public TableColumn<Order, Integer> customerId;
    @FXML
    public TableColumn<Order, String> tableId;
    @FXML
    public TextField customerTextField;
    @FXML
    public DatePicker datePicker;
    @FXML
    public ComboBox<String> comboBox;
    public TableView<OrderItem> ordersTable;
    public TableColumn<OrderItem, String> menuItem;
    public TableColumn<OrderItem, Integer> quantity;
    public Label nameCustomer;
    public Label price;




    public void initialize() {

        datePicker.setVisible(false);
        customerTextField.setVisible(false);
        idOrder.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        orderDate.setCellValueFactory(new PropertyValueFactory<>(Constants.DATE));
        customerId.setCellValueFactory(new PropertyValueFactory<>(Constants.CUSTOMER_ID));
        tableId.setCellValueFactory(new PropertyValueFactory<>(Constants.TABLE_ID));
        menuItem.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        filterOptions();

        customerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null || newValue.trim().isEmpty()) {
                        customersTable.getItems().setAll(listOrderViewModel.getServices().getAll().get());
                    } else {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(listOrderViewModel.getServices().getOrdersByCustomerId(Integer.parseInt(customerTextField.getText())));
                    }
                }
        );
        datePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue == null) {
                        customersTable.getItems().setAll(listOrderViewModel.getServices().getAll().get());
                    } else {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(listOrderViewModel.getServices().filteredListDate(datePicker.getValue()).get());
                    }
                }

        );

        listOrderViewModel.getState().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListOrders() != null) {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(newValue.getListOrders());
                    }
                }
        );
        customersTable.setOnMouseClicked(event -> {
            Order selectedOrder = customersTable.getSelectionModel().getSelectedItem();

            price.setText(String.valueOf(listOrderViewModel.getOrderItemService().getTotalPrice(selectedOrder.getId())));
           ordersTable.getItems().setAll(listOrderViewModel.getOrderItemService().getOrdersById(selectedOrder.getId()));

            priceT.setCellValueFactory(cellData -> {
                OrderItem orderItem = cellData.getValue();
                MenuItem menuItem = orderItem.getMenuItem();
                return new SimpleDoubleProperty(menuItem.getPrice()*orderItem.getQuantity()).asObject();
            });

            nameCustomer.setText(listOrderViewModel.getServicesCustomer().getNameById(selectedOrder.getCustomer_id()));

        });
        listOrderViewModel.voidState();
    }

    private void filterOptions() {
        comboBox.setOnAction(event -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
            if ("Customer ID".equals(selectedItem)) {
                customerTextField.setVisible(true);
                datePicker.setVisible(false);
            } else if ("Date".equals(selectedItem)) {
                customerTextField.setVisible(false);
                datePicker.setVisible(true);
            }
        });
    }


    @Override
    public void principalLoaded() {
        listOrderViewModel.loadState();
        if(getPrincipalController().getActualUser().getId()!=-1) {
            if(!listOrderViewModel.getServices().getOrdersByCustomerId(getPrincipalController().getActualUser().getId()).isEmpty()){
                customersTable.getItems().setAll(listOrderViewModel.getServices().getOrdersByCustomerId(getPrincipalController().getActualUser().getId()));
            }

        }else {
            if (!listOrderViewModel.getServices().getAll().isEmpty()) {
                customersTable.getItems().setAll(listOrderViewModel.getServices().getAll().get());
            }
        }
    }


}
