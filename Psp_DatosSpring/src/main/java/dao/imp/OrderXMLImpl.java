package dao.imp;

import common.Constants;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Named;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import model.Order;
import model.errors.OrderError;
import model.xml.OrderItemXML;
import model.xml.OrderXML;
import model.xml.OrdersXML;

import javax.security.auth.login.Configuration;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Named("OrderXMLImpl")
public class OrderXMLImpl implements OrdersDAO {
    @Override
    public Either<OrderError, List<Order>> getAll() {
        return null;
    }

    @Override
    public Either<OrderError, List<Order>> getAll(int id) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Order> save(Order order) {
        List<OrderItemXML> list = new ArrayList<>();
        order.getOrderItemList().forEach(orderItem -> list.add(new OrderItemXML(orderItem.getId(), orderItem.getIdOrder(), orderItem.getMenuItem().toString(), orderItem.getQuantity())));
        OrderXML orderXML = new OrderXML(order.getId(), order.getDate(), order.getCustomer_id(), order.getTable_id(), list);
        try {
            Path xmlFile = Paths.get("data/" + order.getCustomer_id() + ".xml");
            File file = xmlFile.toFile();
            if (!file.exists()) {
                file.createNewFile();
            }

            OrdersXML ordersXML = new OrdersXML();
            if (file.exists() && file.length() != 0) {
                JAXBContext jaxbContext = JAXBContext.newInstance(OrdersXML.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ordersXML = (OrdersXML) unmarshaller.unmarshal(file);
            }
            ordersXML.getOrderList().add(orderXML);
            JAXBContext jaxbContext = JAXBContext.newInstance(OrdersXML.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            java.io.FileWriter fileWriter = new java.io.FileWriter(file);
            jaxbMarshaller.marshal(ordersXML, fileWriter);
            fileWriter.close();
        } catch (JAXBException | java.io.IOException e) {
            e.printStackTrace();
        }
        return Either.right(order);
    }

    @Override
    public Either<OrderError, Integer> update(Order c) {
        return null;
    }


}
