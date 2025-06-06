package com.se330.coffee_shop_management_backend.service.dummydataservices;

import com.se330.coffee_shop_management_backend.service.dummydataservices.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainDummyDataService implements CommandLineRunner {

    private final UserBasicInfo userBasicInfo;
    private final ParentProduct parentProduct;
    private final VariantAndIngredient variantAndIngredient;
    private final BranchAndEmployee branchAndEmployee;
    private final WarehouseAndSupplier warehouseAndSupplier;
    private final StockAndInventory stockAndInventory;
    private final Comment comment;
    private final Discount discount;

    @Override
    public void run(String... args) throws Exception {
        log.info("Start creating dummy data...");

        log.info("Creating user basic info...");
        userBasicInfo.create();
        log.info("Dummy data creation completed.");

        log.info("Creating parent product..");
        parentProduct.create();
        log.info("Dummy data creation completed.");

        log.info("Creating variant and ingredient...");
        variantAndIngredient.create();
        log.info("Dummy data creation completed.");

        log.info("Creating branch and employee...");
        branchAndEmployee.create();
        log.info("Dummy data creation completed.");

        log.info("Creating warehouse and supplier...");
        warehouseAndSupplier.create();
        log.info("Dummy data creation completed.");

        log.info("Creating stock and inventory...");
        stockAndInventory.create();
        log.info("Dummy data creation completed.");

        log.info("Creating comments...");
        comment.create();
        log.info("Dummy data creation completed.");

        log.info("Creating discounts...");
        discount.create();
        log.info("Dummy data creation completed.");

        log.info("All dummy data creation completed successfully.");
    }
}
