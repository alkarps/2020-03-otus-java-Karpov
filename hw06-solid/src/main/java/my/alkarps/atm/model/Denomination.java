package my.alkarps.atm.model;

import java.math.BigInteger;

/**
 * @author alkarps
 * create date 22.07.2020 13:09
 */
public enum Denomination {
    b1(1),
    b2(2),
    b5(5),
    b10(10),
    b50(50),
    b100(100),
    b500(500),
    b1000(1000),
    b5000(5000);

    private final int amount;

    Denomination(int amount) {
        this.amount = amount;
    }

    public BigInteger getAmount() {
        return BigInteger.valueOf(amount);
    }
}
