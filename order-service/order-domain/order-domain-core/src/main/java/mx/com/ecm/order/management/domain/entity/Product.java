package mx.com.ecm.order.management.domain.entity;

import mx.com.ecm.order.management.domain.entity.BaseEntity;
import mx.com.ecm.order.management.domain.valueobject.Money;
import mx.com.ecm.order.management.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        this.setId(productId);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
