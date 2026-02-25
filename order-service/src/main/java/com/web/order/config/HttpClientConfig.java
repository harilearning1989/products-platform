package com.web.order.config;

import com.web.order.client.InventoryClient;
import com.web.order.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

    /*

    @Bean
    public PaymentClient paymentClient() {
        return createClient(PaymentClient.class, serviceUrls.getPayment());
    }*/

    private final WebClient.Builder webClientBuilder;
    private final ServiceUrlConfig serviceUrls;

    @Bean
    public ProductClient productClient() {
        return createClient(ProductClient.class, serviceUrls.getProduct());
    }

    @Bean
    public InventoryClient inventoryClient() {
        return createClient(InventoryClient.class, serviceUrls.getInventory());
    }

    private <T> T createClient(Class<T> clazz, String serviceName) {
        WebClient webClient = webClientBuilder
                .baseUrl("http://" + serviceName)
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(adapter)
                .build();

        return factory.createClient(clazz);
    }
}
