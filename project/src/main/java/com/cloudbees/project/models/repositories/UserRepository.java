package com.cloudbees.project.models.repositories;

import com.cloudbees.project.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Boolean existsByEmail(String email);
}
