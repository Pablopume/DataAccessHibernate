package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemXML {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "id_order")
    private int idOrder;
    @XmlElement(name = "menu_item")
    private String menuItem;
    @XmlElement(name = "quantity")
    private int quantity;

}
