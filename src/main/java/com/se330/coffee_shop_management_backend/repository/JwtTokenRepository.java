package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.JwtToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface JwtTokenRepository extends CrudRepository<JwtToken, UUID> {
    Optional<JwtToken> findByTokenOrRefreshToken(String token, String refreshToken);

    Optional<JwtToken> findByUserIdAndRefreshToken(UUID id, String refreshToken);
}
