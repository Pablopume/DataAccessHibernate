package ui.screens.orders.deleteorders;

import common.Constants;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import model.OrderItem;

import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class DeleteOrderController extends BaseScreenController {
    @FXML
    public TableView<Order> ordersTable;
    @FXML
    public TableColumn<Order, Integer> idOrder;
    @FXML
    public TableColumn<Order, LocalDateTime> orderDate;

    @FXML
    public TableColumn<Order, Integer> customerId;
    @FXML
    public TableColumn<Order, String> tableId;
    public Button buttonDelete;
    public TableView<OrderItem> ordersXMLTable;
    public TableColumn<OrderItem, String> menuItem;
    public TableColumn<OrderItem, Integer> quantity;
    @Inject
    DeleteOrderViewModel deleteOrderViewModel;

    public void initialize() {
        idOrder.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        orderDate.setCellValueFactory(new PropertyValueFactory<>(Constants.DATE));
        customerId.setCellValueFactory(new PropertyValueFactory<>(Constants.CUSTOMER_ID));
        tableId.setCellValueFactory(new PropertyValueFactory<>(Constants.TABLE_ID));
        menuItem.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        deleteOrderViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListOrders() != null) {
                        ordersTable.getItems().clear();
                        ordersTable.getItems().setAll(newValue.getListOrders());
                    }

                }

        );
        ordersTable.setOnMouseClicked(event -> {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            ordersXMLTable.getItems().setAll(deleteOrderViewModel.getOrderItemService().getOrdersById(selectedOrder.getId()));

        });
        deleteOrderViewModel.voidState();

    }

    @Override
    public void principalLoaded() {
        deleteOrderViewModel.loadState();
        if(getPrincipalController().getActualUser().getId()!=-1) {
            ordersTable.getItems().setAll(deleteOrderViewModel.getServices().getOrdersByCustomerId(getPrincipalController().getActualUser().getId()));
        }
    }

    public void deleteOrder(ActionEvent actionEvent) {
        SelectionModel<Order> selectionModel = ordersTable.getSelectionModel();
        Order selectedOrder = selectionModel.getSelectedItem();


        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm delete");
        confirmationAlert.setHeaderText("Delete order");
        confirmationAlert.setContentText("Are you sure you want to delete this order?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteOrderViewModel.getServices().delete(selectedOrder);
            ObservableList<Order> orders = ordersTable.getItems();
            orders.remove(selectedOrder);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Order deleted");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The Order has been deleted");
            successAlert.showAndWait();
        }
    }


}