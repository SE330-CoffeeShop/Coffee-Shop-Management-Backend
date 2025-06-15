package com.se330.coffee_shop_management_backend.service.favoritedrinkservices.imp;

import com.se330.coffee_shop_management_backend.entity.FavoriteDrink;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.repository.FavoriteDrinkRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.service.favoritedrinkservices.IFavoriteDrinkService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImpFavoriteDrinkService implements IFavoriteDrinkService {

    private final FavoriteDrinkRepository favoriteDrinkRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ImpFavoriteDrinkService(
            FavoriteDrinkRepository favoriteDrinkRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {
        this.favoriteDrinkRepository = favoriteDrinkRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public UUID addFavoriteDrink(UUID userId, UUID drinkId) {
        Optional<FavoriteDrink> existingFavorite = favoriteDrinkRepository.findByUser_IdAndProduct_Id(userId, drinkId);
        if (existingFavorite.isPresent()) {
            return existingFavorite.get().getId();
        }

        // Find user and product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(drinkId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + drinkId));

        // Create and save new favorite drink
        FavoriteDrink favoriteDrink = FavoriteDrink.builder()
                .user(user)
                .product(product)
                .build();

        FavoriteDrink savedFavoriteDrink = favoriteDrinkRepository.save(favoriteDrink);
        return savedFavoriteDrink.getId();
    }

    @Override
    @Transactional
    public void removeFavoriteDrink(UUID userId, UUID drinkId) {
        FavoriteDrink favoriteDrink = favoriteDrinkRepository.findByUser_IdAndProduct_Id(userId, drinkId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Favorite drink not found for user id: " + userId + " and product id: " + drinkId));

        favoriteDrinkRepository.delete(favoriteDrink);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllFavoriteDrinksByUserId(UUID userId, Pageable pageable) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        List<UUID> productIds = favoriteDrinkRepository.findProductIdsByUser_Id(userId, pageable).getContent();
        if (productIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return productRepository.findAllById(productIds, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findTheMostFavoritedDrink(Pageable pageable) {
        List<UUID> productIds = favoriteDrinkRepository.findMostFavoritedProductIds(pageable).getContent();
        if (productIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return productRepository.findAllById(productIds, pageable);
    }

    @Override
    public boolean isDrinkFavoritedByUser(UUID userId, UUID drinkId) {
        return favoriteDrinkRepository.existsFavoriteDrinkByUser_IdAndProduct_Id(userId, drinkId);
    }
}