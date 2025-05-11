package com.se330.coffee_shop_management_backend.service.ingredientservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.ingredient.IngredientCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.ingredient.IngredientUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.service.ingredientservices.IIngredientService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpIngredientService implements IIngredientService {

    private final IngredientRepository ingredientRepository;

    public ImpIngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient findByIdIngredient(UUID id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Ingredient> findAllIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable);
    }

    @Override
    public Ingredient createIngredient(IngredientCreateRequestDTO ingredientCreateRequestDTO) {
        return ingredientRepository.save(
                Ingredient.builder()
                        .ingredientName(ingredientCreateRequestDTO.getIngredientName())
                        .ingredientDescription(ingredientCreateRequestDTO.getIngredientDescription())
                        .ingredientPrice(ingredientCreateRequestDTO.getIngredientPrice())
                        .ingredientType(ingredientCreateRequestDTO.getIngredientType())
                        .build()
        );
    }

    @Override
    public Ingredient updateIngredient(IngredientUpdateRequestDTO ingredientUpdateRequestDTO) {
        Ingredient existingIngredient = ingredientRepository.findById(ingredientUpdateRequestDTO.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        existingIngredient.setIngredientName(ingredientUpdateRequestDTO.getIngredientName());
        existingIngredient.setIngredientDescription(ingredientUpdateRequestDTO.getIngredientDescription());
        existingIngredient.setIngredientPrice(ingredientUpdateRequestDTO.getIngredientPrice());
        existingIngredient.setIngredientType(ingredientUpdateRequestDTO.getIngredientType());

        return ingredientRepository.save(existingIngredient);
    }

    @Transactional
    @Override
    public void deleteIngredient(UUID id) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        ingredientRepository.delete(existingIngredient);
    }
}
