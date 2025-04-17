package com.megaminds.finance.Service;

import com.megaminds.finance.Entity.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Userclient userClient;

    public UserService(Userclient userClient) {
        this.userClient = userClient;
    }

    public UserDto getUser(String id, String token) {
        return userClient.getUserById(id, "Bearer " + token);
    }
}