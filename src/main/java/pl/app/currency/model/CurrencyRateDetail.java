package pl.app.currency.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyRateDetail {
    private String no;
    private String effectiveDate;
    private double bid;
    private double ask;
}
