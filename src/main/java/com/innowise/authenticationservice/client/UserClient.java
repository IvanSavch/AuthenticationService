package com.innowise.authenticationservice.client;

import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.exception.ServiceUnavailableException;
import com.innowise.authenticationservice.model.dto.user.CreateUserServiceDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
public class UserClient {
    private final RestTemplate restTemplate;
    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "createFallback")
    public void create(CreateUserServiceDto userServiceDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("UserId", "0");
        headers.set("UserRoles", "ROLE_ADMIN");
        HttpEntity<CreateUserServiceDto> httpEntity = new HttpEntity<>(userServiceDto, headers);
        restTemplate.postForEntity(userServiceUrl + "/users/", httpEntity, Void.class);
    }

    public void createFallback(CreateUserServiceDto userServiceDto, Throwable throwable) throws ServiceUnavailableException {
        if (throwable instanceof HttpClientErrorException.BadRequest){
            throw  new InvalidCredentialsException();
        }
        throw new ServiceUnavailableException();
    }
}
