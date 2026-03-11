package com.innowise.authenticationservice.client;

import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.exception.ServiceUnavailableException;
import com.innowise.authenticationservice.model.dto.user.CreateUserServiceDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
public class UserClient {
    private static final String USER_ID_HEADER = "UserId";
    private static final String USER_ROLES_HEADER = "UserRoles";
    private static final String USER_ID = "0";
    private static final String ROLE = "ROLE_ADMIN";
    private final RestTemplate restTemplate;
    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "createFallback")
    public void create(CreateUserServiceDto userServiceDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_ID_HEADER, USER_ID);
        headers.set(USER_ROLES_HEADER, ROLE);
        HttpEntity<CreateUserServiceDto> httpEntity = new HttpEntity<>(userServiceDto, headers);
        restTemplate.postForEntity(userServiceUrl + "/users/", httpEntity, Void.class);
    }

    public void createFallback(CreateUserServiceDto userServiceDto, Throwable throwable) {
        if (throwable instanceof HttpClientErrorException httpClientErrorException) {
            if (httpClientErrorException.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidCredentialsException("Email already exist");
            }
        }
        throw new ServiceUnavailableException("User service unavailable");
    }

}
