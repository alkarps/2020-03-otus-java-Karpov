package my.alkarps.atm.model;

import java.util.Optional;

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

    public static int compare(Denomination d1, Denomination d2) {
        return Integer.compare(getAmount(d1), getAmount(d2));
    }

    private static int getAmount(Denomination d) {
        return d == null ? -1 : d.amount;
    }

    public Long getAmount() {
        return (long) amount;
    }

    /**
     * Поиск номинала по названию для восстановления.
     * Основан на enum.valueOf с перехватом ошибки IllegalArgumentException
     *
     * @param name - название номинала
     * @return Возвращает номинал в обертке Optional
     */
    public static Optional<Denomination> fromName(String name) {
        try {
            return Optional.of(valueOf(name));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
