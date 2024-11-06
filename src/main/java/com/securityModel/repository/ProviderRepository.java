package com.securityModel.repository;

import com.securityModel.models.Provider;
import com.securityModel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Long> {
    Optional<Provider> findByUsername(String username);
}
