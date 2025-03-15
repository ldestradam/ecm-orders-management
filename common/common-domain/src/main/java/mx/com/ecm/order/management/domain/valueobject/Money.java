package mx.com.ecm.order.management.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isGreaterThanZero() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money other) {
        return amount.compareTo(other.getAmount()) > 0;
    }

    public Money add(Money other) {
        return new Money(setScale(amount.add(other.getAmount())));
    }

    public Money subtract(Money other) {
        return new Money(setScale(amount.subtract(other.getAmount())));
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(amount.multiply(new BigDecimal(multiplier))));
    }

    private BigDecimal setScale(BigDecimal amount) {
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
