package com.se330.coffee_shop_management_backend.controller.admin;

import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.service.adminservices.IAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminn")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/update-database")
    public ResponseEntity<SingleResponse<Void>> updateDatabase() {
        adminService.updateDatabase();
        return ResponseEntity.ok(new SingleResponse<>(
                200,
                "Cập nhật cơ sở dữ liệu thành công",
                null
        ));
    }
}
