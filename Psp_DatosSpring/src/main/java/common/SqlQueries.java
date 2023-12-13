package common;

public class SqlQueries {

    public static final String INSERT_INTO_CREDENTIALS_ID_USER_NAME_PASSWORD_VALUES = "insert into credentials (user_name, password) values (?,?)";
    public static final String INSERT_INTO_CUSTOMERS_ID_FIRST_NAME_LAST_NAME_EMAIL_PHONE_DATE_OF_BIRTH_VALUES = "insert into customers (id, first_name, last_name, email, phone, date_of_birth) values (?,?,?,?,?,?)";
    public static final String DELETE_FROM_CUSTOMERS_WHERE_ID = "DELETE FROM customers WHERE id = ?";
    public static final String DELETE_FROM_CREDENTIALS_WHERE_ID = "DELETE FROM credentials WHERE id = ?";
    public static final String UPDATE_CUSTOMERS_SET_FIRST_NAME_LAST_NAME_EMAIL_PHONE_DATE_OF_BIRTH_WHERE_ID = "update customers set first_name=?, last_name=?, email=?, phone=?, date_of_birth=? where id=?";
    public static final String SELECT_FROM_CUSTOMERS_WHERE_ID = "select * from customers where id=?";
    public static final String DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID_SELECT_ORDER_ID_FROM_ORDERS_WHERE_CUSTOMER_ID = "DELETE FROM order_items WHERE order_id = (SELECT order_id FROM orders WHERE customer_id = ?)";
    public static final String DELETE_FROM_ORDERS_WHERE_CUSTOMER_ID = "DELETE FROM orders WHERE customer_id = ?";
    public static final String DELETE_FROM_CREDENTIALS_WHERE_CUSTOMER_ID = "DELETE FROM credentials WHERE id = ?";
    public static final String DELETE_FROM_CUSTOMERS_WHERE_ID1 = "DELETE FROM customers WHERE id = ?";
    public static final String SELECT_FROM_CREDENTIALS = "SELECT * FROM credentials";
    public static final String SELECT_FROM_MENU_ITEMS = "SELECT * FROM menu_items";
    public static final String SELECT_FROM_ORDERS = "select * from orders";
    public static final String DELETE_FROM_ORDERS_WHERE_ORDER_ID = "DELETE FROM orders WHERE order_id = ?";
    public static final String DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID = "DELETE FROM order_items WHERE order_id = ?";
    public static final String UPDATE_ORDERS_SET_ORDER_DATE_CUSTOMER_ID_TABLE_ID_WHERE_ORDER_ID = "update orders set order_date=?, customer_id=?, table_id=? where order_id=?";
    public static final String INSERT_INTO_ORDERS_ORDER_DATE_CUSTOMER_ID_TABLE_ID_VALUES = "insert into orders (order_date,customer_id,table_id) values (?, ?, ?)";
    public static final String SELECT_FROM_ORDER_ITEMS = "SELECT * FROM order_items";
    public static final String INSERT_INTO_ORDER_ITEMS_ORDER_ID_MENU_ITEM_ID_QUANTITY_VALUES = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
    public static final String INSERT_INTO_ORDER_ITEMS_ORDER_ID_MENU_ITEM_ID_QUANTITY_VALUES1 = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
    public static final String SELECT_FROM_MENU_ITEMS_WHERE_MENU_ITEM_ID = "SELECT * FROM menu_items WHERE menu_item_id = ?";
    public static final String DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID2 = "DELETE FROM order_items WHERE order_id = ?";
    public static final String DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID1 = "DELETE FROM order_items WHERE order_id = ?";
    public static final String DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID_IN_SELECT_ORDER_ID_FROM_ORDERS_WHERE_CUSTOMER_ID = "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)";
    public static final String DELETE_FROM_CREDENTIALS_WHERE_ID4 = "Delete from credentials where id = ?";
    public static final String QUERY = "SELECT oi.order_item_id, oi.order_id, oi.menu_item_id, oi.quantity, mi.name, mi.description, mi.price FROM order_items oi INNER JOIN menu_items mi ON oi.menu_item_id = mi.menu_item_id";
}
