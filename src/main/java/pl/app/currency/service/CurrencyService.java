package pl.app.currency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.app.currency.model.CurrencyRateDetail;
import pl.app.currency.model.CurrencyRateResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyRateResponse getRate(String currencyCode) {
        //tutaj sprawdzić czy pln jesli tak to zwrócić 1
        if ("PLN".equalsIgnoreCase(currencyCode)) {
            CurrencyRateDetail currencyRateDetail = new CurrencyRateDetail("1", LocalDate.now().toString(), 1.0, 1.0);
            return new CurrencyRateResponse("C", "złoty polski", "PLN", List.of(currencyRateDetail));
        }

        //try catch
        String url = "http://api.nbp.pl/api/exchangerates/rates/c/" + currencyCode;
        return restTemplate.getForObject(url, CurrencyRateResponse.class);
    }
}
