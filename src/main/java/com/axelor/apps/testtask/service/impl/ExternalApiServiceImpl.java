package com.axelor.apps.testtask.service.impl;

import com.axelor.apps.testtask.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiServiceImpl implements ExternalApiService {

    private final RestTemplate restTemplate;

    @Value("${url.objects}")
    private String EXTERNAL_OBJECTS_URL;

    @Async
    @Override
    public void fetchToExternalApi() {
        ResponseEntity<List<Object>> response = restTemplate.exchange(
                EXTERNAL_OBJECTS_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Object>>() {}
        );

        List<Object> objects = response.getBody();
        if (objects != null) {
            objects.forEach(obj -> log.info("Полученный обьект: {}", obj));
        } else {
            log.warn("Ничего не получено из внешнего API");
        }
    }
}
