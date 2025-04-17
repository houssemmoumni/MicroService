package com.megaminds.finance.Service;

import com.megaminds.finance.Controller.FeignClientInterceptor;
import com.megaminds.finance.Entity.ProjectDTO;
import com.megaminds.finance.Entity.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user-service", configuration = FeignClientInterceptor.class)
public interface Userclient {
    @GetMapping("/api/services/user/{id}")
    UserDto getUserById(@PathVariable String id, @RequestHeader("Authorization") String token);
}
