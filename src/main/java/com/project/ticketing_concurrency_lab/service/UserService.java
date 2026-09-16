package com.project.ticketing_concurrency_lab.service;

import com.project.ticketing_concurrency_lab.domain.User;
import com.project.ticketing_concurrency_lab.repository.UserRepository;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public List<Long> createUser(int count) {

        return IntStream.range(0, count)
                .mapToObj(i -> userRepository.save(User.create()))
                .map(User::getId)
                .toList();
    }

}
