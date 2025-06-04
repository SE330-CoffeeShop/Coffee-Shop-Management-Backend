package com.se330.coffee_shop_management_backend.dto.response.catalog;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Catalog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class CatalogResponseDTO {

    @Schema(
            name = "id",
            description = "Catalog ID",
            type = "Integer",
            example = "1"
    )
    private Integer id;

    @Schema(
            name = "createdAt",
            description = "Date time field of catalog creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of catalog update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String name;
    private String description;
    private Integer parentCatalogId;
    private List<Integer> childCatalogIds;
    private List<String> categoryIds;

    public static CatalogResponseDTO convert(Catalog catalog) {
        List<Integer> childIds;
        List<String> catIds;

        if (catalog.getChildCatalogs() == null || catalog.getChildCatalogs().isEmpty()) {
            childIds = Collections.emptyList();
        } else {
            childIds = catalog.getChildCatalogs().stream()
                    .map(Catalog::getId)
                    .collect(Collectors.toList());
        }

        if (catalog.getCategories() == null || catalog.getCategories().isEmpty()) {
            catIds = Collections.emptyList();
        } else {
            catIds = catalog.getCategories().stream()
                    .map(category -> category.getId().toString())
                    .collect(Collectors.toList());
        }

        return CatalogResponseDTO.builder()
                .id(catalog.getId())
                .createdAt(catalog.getCreatedAt())
                .updatedAt(catalog.getUpdatedAt())
                .name(catalog.getName())
                .description(catalog.getDescription())
                .parentCatalogId(catalog.getParentCatalog() != null ? catalog.getParentCatalog().getId() : null)
                .childCatalogIds(childIds)
                .categoryIds(catIds)
                .build();
    }

    public static List<CatalogResponseDTO> convert(List<Catalog> catalogs) {
        if (catalogs == null || catalogs.isEmpty()) {
            return Collections.emptyList();
        }

        return catalogs.stream()
                .map(CatalogResponseDTO::convert)
                .collect(Collectors.toList());
    }
}