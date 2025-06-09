package com.se330.coffee_shop_management_backend.service.favoritedrinkservices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IFavoriteDrinkService {
    UUID addFavoriteDrink(UUID userId, UUID drinkId);
    void removeFavoriteDrink(UUID userId, UUID drinkId);
    Page<UUID> findAllFavoriteDrinksByUserId(UUID userId, Pageable pageable);
    Page<UUID> findTheMostFavoritedDrink(Pageable pageable);
    boolean isDrinkFavoritedByUser(UUID userId, UUID drinkId);
}
