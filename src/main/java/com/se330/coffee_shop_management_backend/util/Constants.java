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
         * - Đơn hàng mới được tạo
         * - Cập nhật trạng thái đơn hàng
         * - Đơn hàng hoàn thành hoặc bị hủy
         */
        ORDER("ORDER"),

        /**
         * Thông báo liên quan đến quy trình thanh toán.
         * Ví dụ:
         * - Thanh toán thành công
         * - Giao dịch thất bại
         * - Hoàn tiền
         */
        PAYMENT("PAYMENT"),

        /**
         * Thông báo về các chương trình khuyến mãi và giảm giá.
         * Ví dụ:
         * - Khuyến mãi mới
         * - Khuyến mãi sắp hết hạn
         * - Mã giảm giá mới
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
         * - Nhắc nhở đặt hàng
         * - Thông báo hàng mới về
         */
        INVENTORY("INVENTORY"),

        /**
         * Thông báo liên quan đến quản lý nhân sự.
         * Ví dụ:
         * - Phân công ca làm việc
         * - Giao nhiệm vụ
         * - Đánh giá hiệu suất
         */
        EMPLOYEE("EMPLOYEE"),

        /**
         * Thông báo trực tiếp từ quản lý đến nhân viên.
         * Lưu ý: Chỉ quản lý mới có thể gửi những thông báo này đến nhân viên.
         * Ví dụ:
         * - Nhắc nhở
         * - Thông báo
         * - Hướng dẫn
         */
        MANAGER("MANAGER");

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
