package com.sesac.orderservice.facade;

import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceFacade {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "user-service", fallbackMethod = "getUserFallback")
    @Retry(name = "user-service")
    public UserDto getUserByIdWithFallback(Long id) {
        log.info("* try call to getUserByIdWithFallback with id: {}", id);

        return userServiceClient.getUserById(id);
    }

    protected UserDto getUserFallback(Long id, Throwable ex) {
        log.warn("**** Fallback called ! userId: {}, error: {} ", id, ex.getMessage());
        return getDefaultUser(id);
    }

    private UserDto getDefaultUser(Long id) {

        UserDto defaultUser = new UserDto();
        defaultUser.setId(id);
        defaultUser.setName("tempUserName");
        defaultUser.setEmail("temp@example.com");

        return defaultUser;
    }
}
