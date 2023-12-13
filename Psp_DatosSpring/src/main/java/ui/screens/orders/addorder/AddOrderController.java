package ui.screens.orders.addorder;

import common.Constants;
import jakarta.inject.Inject;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Credentials;
import model.Order;
import model.OrderItem;

import ui.screens.common.BaseScreenController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddOrderController extends BaseScreenController {
    @FXML
    public TableView<Order> customersTable;
    @FXML
    public TableColumn<Order, Integer> idOrder;
    @FXML
    public TableColumn<Order, LocalDateTime> orderDate;

    @FXML
    public TableColumn<Order, Integer> customerId;
    @FXML
    public TableColumn<Order, Integer> tableId;
    Credentials actualuser;
    @FXML
    public ComboBox<String> idCustomer;
    @FXML
    public ComboBox<String> table_id;
    public TableColumn<OrderItem, String> menuItem;
    public TableView<OrderItem> ordersXMLTable;
    public TableColumn<OrderItem, Integer> quantity;
    public ComboBox<String> menuItems;
    public TextField quantityItems;


    @Inject
    AddOrderViewModel addOrderViewModel;

    public void initialize() {
        menuItem.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        addOrderViewModel.voidState();

    }



    public void addOrder() {

            int selectedCustomerId = actualuser.getId();
            int selectedTableId = Integer.parseInt(table_id.getValue());
     Order order=  addOrderViewModel.getServices().createOrder(new Order(LocalDateTime.now(),selectedCustomerId,selectedTableId)).get();
            List<Order> orders = addOrderViewModel.getServices().getAll().get();
            if((orders.get(orders.size()-1).getDate().getSecond()==order.getDate().getSecond()||orders.get(orders.size()-1).getDate().getSecond()==order.getDate().getSecond()+1 ) && orders.get(orders.size()-1).getDate().getMinute()==order.getDate().getMinute() && orders.get(orders.size()-1).getDate().getHour()==order.getDate().getHour() && orders.get(orders.size()-1).getDate().getDayOfMonth()==order.getDate().getDayOfMonth() && orders.get(orders.size()-1).getDate().getMonth()==order.getDate().getMonth() && orders.get(orders.size()-1).getDate().getYear()==order.getDate().getYear()) {
            for (OrderItem orderItem : ordersXMLTable.getItems()) {
                orderItem.setIdOrder(orders.get(orders.size() - 1).getId());
            }
        }
            addOrderViewModel.getOrderItemService().save(ordersXMLTable.getItems());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(Constants.ORDER_ADDED);
            alert.setHeaderText(null);
            alert.setContentText(Constants.THE_ORDER_HAS_BEEN_ADDED);
            alert.showAndWait();

    }

    public void addItem() {
         ObservableList<OrderItem> orderItem= ordersXMLTable.getItems();
          orderItem.add(new OrderItem(addOrderViewModel.getOrderItemService().getAutoId(),addOrderViewModel.getServices().getLastId()+1,addOrderViewModel.getMenuItemService().getByName(menuItems.getValue()),Integer.parseInt(quantityItems.getText())));
          ordersXMLTable.setItems(orderItem);
    }

    public void removeOrder() {

        ObservableList<OrderItem> orderItemXMLS= ordersXMLTable.getItems();
        OrderItem selectedOrder = ordersXMLTable.getSelectionModel().getSelectedItem();
          orderItemXMLS.remove(selectedOrder);
          ordersXMLTable.setItems(orderItemXMLS);
    }

    @Override
    public void principalLoaded() {
        addOrderViewModel.loadState();
        actualuser=getPrincipalController().getActualUser();
    }
}
