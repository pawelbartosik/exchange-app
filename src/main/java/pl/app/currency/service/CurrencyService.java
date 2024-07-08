package pl.app.currency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.app.account.model.enums.CurrencyCode;
import pl.app.currency.model.CurrencyRateDetail;
import pl.app.currency.model.CurrencyRateResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyRateResponse getSingleRate(CurrencyCode currencyCode) {
        if ("PLN".equalsIgnoreCase(String.valueOf(currencyCode))) {
            CurrencyRateDetail currencyRateDetail = new CurrencyRateDetail("1", LocalDate.now().toString(), 1.0, 1.0);
            return new CurrencyRateResponse("C", "złoty polski", "PLN", List.of(currencyRateDetail));
        }

        //try catch
        String url = "http://api.nbp.pl/api/exchangerates/rates/c/" + currencyCode;
        return restTemplate.getForObject(url, CurrencyRateResponse.class);
    }

    public BigDecimal exchangeCurrency(CurrencyCode from, CurrencyCode to, BigDecimal amount) {
        CurrencyRateResponse fromRate = getSingleRate(from);
        CurrencyRateResponse toRate = getSingleRate(to);

        double fromRateValue = fromRate.getRates().get(0).getBid();
        double toRateValue = toRate.getRates().get(0).getAsk();

        return amount.multiply(BigDecimal.valueOf(fromRateValue / toRateValue));
    }
}
