package pl.app.currency.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateResponse {
    private String table;
    private String currency;
    private String code;
    private List<CurrencyRateDetail> rates;
}