package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
public class OrdersXML {
    @XmlElement(name = "order")
    private List<OrderXML> orderList;

    public OrdersXML() {
        this.orderList = new ArrayList<>();
    }
}
