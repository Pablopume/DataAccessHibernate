package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.JPAUtil;
import dao.OrdersDAO;
import dao.imp.maps.MapOrder;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;
import model.model2.CustomersEntity;
import model.model2.OrdersEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.List;

@Log4j2
@Named("OrderDB")
public class OrderDB implements OrdersDAO {
    private DBConnectionPool db;
    private final JPAUtil jpautil;
    private EntityManager em;

    @Inject
    public OrderDB(DBConnectionPool db, JPAUtil jpautil) {
        this.db = db;
        this.jpautil = jpautil;

    }

    @Override
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> result;
        JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());
        List<Order> l = jtm.query(SqlQueries.SELECT_FROM_ORDERS, new MapOrder());
        if (l.isEmpty()) {
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        } else {
            result = Either.right(l);
        }

        return result;
    }

    @Override
    public Either<OrderError, List<Order>> getAll(int id) {
        return null;
    }

    public Either<OrderError, Integer> delete(Order order) {
        Either<OrderError, Integer> result;

        try {
            em = jpautil.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                OrdersEntity ordersEntity = em.find(OrdersEntity.class, order.getId());
                em.remove(em.merge(ordersEntity));
                tx.commit();
                result = Either.right(1);
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();

                result = Either.left(new OrderError("Error deleting order"));
            } finally {
                em.close();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }

        return result;
    }


    public Either<OrderError, Integer> update(Order c) {
        Either<OrderError, Integer> result;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.UPDATE_ORDERS_SET_ORDER_DATE_CUSTOMER_ID_TABLE_ID_WHERE_ORDER_ID + "  ")) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(2, c.getCustomer_id());
            preparedStatement.setInt(3, c.getTable_id());
            preparedStatement.setInt(4, c.getId());
            int rs = preparedStatement.executeUpdate();
            if (rs == 0) {
                result = Either.left(new OrderError(Constants.ERROR_UPDATING_ORDER));
            } else {
                result = Either.right(0);
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }


    @Override
    public Either<OrderError, Order> save(Order order) {
        Either<OrderError, Order> result;
        EntityManager entityManager = jpautil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            OrdersEntity order2 = order.toOrdersEntity();
            order2.setOrderItemsByOrderId(order.getOrderItemList().stream().map(OrderItem::toOrderItemsEntity).toList());
            order2.getOrderItemsByOrderId().forEach(orderItemsEntity -> orderItemsEntity.setOrdersByOrderId(order2));
            entityManager.persist(order2);

            tx.commit();
            result = Either.right(order);
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            entityManager.getTransaction().rollback();
            result = Either.left(new OrderError(Constants.ERROR_SAVING_ORDERS));
        } finally {
            entityManager.close();
        }

        return result;
    }

}
