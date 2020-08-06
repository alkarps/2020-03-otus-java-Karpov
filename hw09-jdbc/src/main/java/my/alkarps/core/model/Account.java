package my.alkarps.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.alkarps.jdbc.mapper.annotation.Id;

import java.math.BigDecimal;

/**
 * @author alkarps
 * create date 06.08.2020 22:26
 */
@Getter
@Setter
@ToString
public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;
}
