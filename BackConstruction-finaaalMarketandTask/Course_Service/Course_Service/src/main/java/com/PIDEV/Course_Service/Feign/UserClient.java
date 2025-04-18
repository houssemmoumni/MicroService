package com.PIDEV.Course_Service.Feign;

import com.PIDEV.Course_Service.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", configuration = FeignClientInterceptor.class)
public interface UserClient {  // Renommé en UserClient (convention Java)
    @GetMapping("/api/services/user/{id}")
    UserDTO getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}