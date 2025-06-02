package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CatalogRepository extends JpaRepository<Catalog, Integer>, JpaSpecificationExecutor<Catalog> {
    Page<Catalog> findAllByParentCatalog(Catalog parentCatalog, Pageable pageable);
}
