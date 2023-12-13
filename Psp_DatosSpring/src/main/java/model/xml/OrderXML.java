package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.LocalDateTimeAdapter;
import model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderXML {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime date;
    @XmlElement(name = "customer_id")
    private int customer_id;
    @XmlElement(name = "table_id")
    private int table_id;
    @XmlElement(name = "order_item")
    private List<OrderItemXML> orderItemList;
}
