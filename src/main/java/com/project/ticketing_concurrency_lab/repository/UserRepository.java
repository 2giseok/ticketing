package com.project.ticketing_concurrency_lab.repository;

import com.project.ticketing_concurrency_lab.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
