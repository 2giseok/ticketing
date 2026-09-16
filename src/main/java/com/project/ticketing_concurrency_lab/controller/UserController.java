package com.project.ticketing_concurrency_lab.controller;

import com.project.ticketing_concurrency_lab.controller.dto.CreateUserRequest;
import com.project.ticketing_concurrency_lab.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public List<Long> createUsers(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.count());
    }

}
