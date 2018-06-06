package com.paytm.core.repository;

import com.paytm.core.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findTopByUsername(String username);
}
