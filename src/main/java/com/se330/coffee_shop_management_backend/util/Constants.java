package com.se330.coffee_shop_management_backend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

public final class Constants {
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_TYPE = "Bearer";

    public static final int EMAIL_VERIFICATION_TOKEN_LENGTH = 64;

    public static final int PASSWORD_RESET_TOKEN_LENGTH = 32;

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Constants() {
    }

    @Getter
    @AllArgsConstructor
    public enum RoleEnum {
        ADMIN("ADMIN"),
        CUSTOMER("CUSTOMER"),
        EMPLOYEE("EMPLOYEE"),
        MANAGER("MANAGER"),
        USER("USER");

        private final String value;

        public static RoleEnum get(final String name) {
            return Stream.of(RoleEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid role name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderStatusEnum {
        PENDING("PENDING"),
        PROCESSING("PROCESSING"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED");

        private final String value;

        public static OrderStatusEnum get(final String name) {
            return Stream.of(OrderStatusEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid order status name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum NotificationTypeEnum {
        /**
         * Thông báo liên quan đến quản lý đơn hàng.
         * Ví dụ:
         * - Tạo đơn hàng thành công (online), đã thanh toán thành công
         * - Tạo đơn hàng không thành công:
         *     - Thanh toán thất bại (không đủ tiền)
         *     - Không đủ nguyên liệu để làm đơn hàng
         *     - Đơn hàng không hợp lệ
         * - Đơn hàng được nhận (online)
         * - Đơn hàng bị hủy
         * - Đơn hàng giao thành công
         * - Mua hàng tại quầy thành công
         */
        ORDER("ORDER"),

        /**
         * Thông báo về các chương trình khuyến mãi và giảm giá.
         * Ví dụ:
         * - Thêm khuyến mãi mới
         * - Xóa khuyến mãi
         * - Cập nhật khuyến mãi
         * - Khuyến mãi sắp hết hạn
         */
        DISCOUNT("DISCOUNT"),

        /**
         * Thông báo hệ thống ảnh hưởng đến tất cả người dùng.
         * Ví dụ:
         * - Thông báo bảo trì
         * - Cập nhật phiên bản
         * - Thay đổi chính sách
         */
        SYSTEM("SYSTEM"),

        /**
         * Thông báo liên quan đến quản lý kho.
         * Ví dụ:
         * - Cảnh báo hàng sắp hết
         * - Cảnh báo hàng sắp hết hạn
         */
        INVENTORY("INVENTORY"),

        /**
         * Thông báo liên quan đến quản lý nhân sự.
         * Ví dụ:
         * - Nhận thông báo chào mừng đến chi nhánh
         * - Phân công ca làm mới
         * - Checkin thành công
         * - Thông báo lương về
         */
        EMPLOYEE("EMPLOYEE"),

        /**
         * Thông báo trực tiếp từ quản lý đến nhân viên.
         * Ví dụ:
         * - Gửi thông báo cho nhân viên thành công
         * - Nhân viên nhận thông báo từ manager
         */
        MANAGER("MANAGER"),

        /**
         * Thông báo liên quan đến hóa đơn nhập kho.
         * Ví dụ:
         * - Nhập nguyên liệu vào nhà kho thành công
         * - Nhập nguyên liệu vào nhà kho thất bại
         * - Cập nhật phiếu nhập kho
         * - Hủy phiếu nhập kho
         */
        INVOICE("INVOICE"),

        /**
         * Thông báo liên quan đến chuyển kho.
         * Ví dụ:
         * - Xuất nguyên liệu cho chi nhánh thành công
         * - Xuất nguyên liệu cho chi nhánh thất bại:
         *     - Nhà kho không đủ nguyên liệu
         * - Cập nhật phiếu nhập xuất
         * - Hủy phiếu nhập xuất
         */
        TRANSFER("TRANSFER"),

        /**
         * Thông báo liên quan đến chi nhánh.
         */
        BRANCH("BRANCH"),

        /**
         * Thông báo liên quan đến nhà cung cấp.
         * Ví dụ:
         * - Thêm thông tin nhà cung cấp
         * - Cập nhật thông tin nhà cung cấp
         * - Xóa thông tin nhà cung cấp
         */
        SUPPLIER("SUPPLIER"),

        /**
         * Thông báo liên quan đến các loại sản phẩm.
         * Ví dụ:
         * - Thêm product mới
         * - Cập nhật thông tin product
         * - Xóa thông tin product
         */
        PRODUCT("PRODUCT"),

        /**
         * Thông báo liên quan đến nhà kho.
         * Ví dụ:
         * - Thêm nhà kho mới
         * - Cập nhật thông tin nhà kho
         * - Xóa thông tin nhà kho
         */
        WAREHOUSE("WAREHOUSE");

        private final String value;

        public static NotificationTypeEnum get(final String name) {
            return Stream.of(NotificationTypeEnum.values())
                    .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid notification type name: %s", name)));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum DiscountTypeEnum {
        PERCENTAGE("PERCENTAGE"),
        AMOUNT("AMOUNT");

        private final String value;

        public static DiscountTypeEnum get(final String name) {
            return Stream.of(DiscountTypeEnum.values())
                .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid discount type name: %s", name)));
        }
    }

    public static String getTokenFromPath(final String path) {
        if (path == null || path.isEmpty())
            return null;

        final String[] fields = path.split("/");

        if (fields.length == 0)
            return null;

        try {
            return fields[2];
        } catch (final IndexOutOfBoundsException e) {
            System.out.println("Cannot find user or channel id from the path!. Ex:"+ e.getMessage());
        }
        return null;
    }
}
