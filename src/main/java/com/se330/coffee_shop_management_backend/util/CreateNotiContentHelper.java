package com.se330.coffee_shop_management_backend.util;

import java.util.List;
import java.util.UUID;

public class CreateNotiContentHelper {

    public static String createOrderSuccessContentCustomer(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được tạo thành công. Cảm ơn bạn đã sử dụng dịch vụ của BCoffee!",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderFailedPaymentContent(UUID orderId) {
        return String.format("Thanh toán cho đơn hàng #%s thất bại. Vui lòng kiểm tra lại phương thức thanh toán hoặc số dư tài khoản của bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderFailedIngredientsContent(UUID orderId) {
        return String.format("Đơn hàng #%s không thể thực hiện do không đủ nguyên liệu. Vui lòng thử lại sau hoặc điều chỉnh đơn hàng của bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderInvalidContent(UUID orderId) {
        return String.format("Đơn hàng #%s không hợp lệ. Vui lòng kiểm tra lại thông tin đơn hàng của bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderReceivedContent(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được tiếp nhận và đang được xử lý. BCoffee sẽ sớm chuẩn bị đơn hàng cho bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderCompletedContent(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được hoàn thàn. Cảm ơn bạn đã đặt hàng tại BCoffee!",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderCancelledContent(UUID orderId) {
        return String.format("Đơn hàng #%s đã bị hủy. Nếu bạn có thắc mắc, vui lòng liên hệ với BCoffee để được hỗ trợ.",
                orderId.toString().substring(0, 8));
    }

    public static String createInStorePurchaseContent(UUID orderId) {
        return String.format("Cảm ơn bạn đã mua hàng tại cửa hàng của BCoffee. Mã đơn hàng của bạn: #%s",
                orderId.toString().substring(0, 8));
    }

    // DISCOUNT notification content methods

    public static String createDiscountForManager(String discountName) {
        return String.format("Thông báo: Chương trình khuyến mãi '%s' đã được tạo thành công. Vui lòng kiểm tra chi tiết trong hệ thống.",
                discountName);
    }

    public static String updateDiscountForManager(String discountName) {
        return String.format("Thông báo: Chương trình khuyến mãi '%s' đã được cập nhật thành công. Vui lòng kiểm tra chi tiết trong hệ thống.",
                discountName);
    }

    public static String createDiscountAddedContent(String discountName, String discountValue, String startDate, String endDate, String branchName) {
        return String.format("🎉 Ưu đãi mới! '%s' với giá trị %s tại chi nhánh %s! Áp dụng từ %s đến %s. Đừng bỏ lỡ!",
                discountName, discountValue, branchName, startDate, endDate);
    }

    public static String createDiscountDeletedContent(String discountName, String branchName) {
        return String.format("Thông báo: Chương trình khuyến mãi '%s' tại chi nhánh '%s' đã kết thúc. Cảm ơn quý khách đã tham gia!",
                discountName, branchName);
    }

    public static String createDiscountUpdatedContent(String discountName, String branchName) {
        return String.format("⚠️ Chú ý! Thông tin khuyến mãi '%s' tại chi nhánh %s vừa được cập nhật. Vui lòng kiểm tra chi tiết mới nhất!",
                discountName, branchName);
    }

    public static String createDiscountExpiringContent(String discountName, int daysRemaining, String branchName) {
        return String.format("⏰ Sắp kết thúc! Chỉ còn %d ngày để tận hưởng ưu đãi '%s' tại chi nhánh %s. Hãy ghé BCoffee ngay hôm nay!",
                daysRemaining, discountName, branchName);
    }

    // SYSTEM notification content methods
    public static String createMaintenanceNotificationContent(String startTime, String duration) {
        return String.format("Hệ thống sẽ bảo trì vào lúc %s, dự kiến kéo dài %s. Mong quý khách thông cảm.",
                startTime, duration);
    }

    public static String createVersionUpdateContent(String version) {
        return String.format("Hệ thống đã được cập nhật lên phiên bản %s với nhiều tính năng mới.", version);
    }

    public static String createPolicyChangeContent(String policyName) {
        return String.format("Chính sách '%s' đã được cập nhật. Vui lòng xem chi tiết trong phần Điều khoản sử dụng.",
                policyName);
    }

    // INVENTORY notification content methods
    public static String createLowStockWarningContent(String itemName, int currentQuantity) {
        return String.format("Cảnh báo: Sản phẩm '%s' sắp hết hàng (số lượng hiện tại: %d). Vui lòng nhập thêm.",
                itemName, currentQuantity);
    }

    public static String createExpirationWarningContent(String itemName, int daysRemaining) {
        return String.format("Cảnh báo: Sản phẩm '%s' sẽ hết hạn trong %d ngày. Vui lòng kiểm tra kho.",
                itemName, daysRemaining);
    }

    // EMPLOYEE notification content methods
    public static String createWelcomeBranchContent(String branchName) {
        return String.format("Chào mừng bạn đến với chi nhánh %s! Chúc bạn có những trải nghiệm tuyệt vời.", branchName);
    }

    public static String createNewShiftAssignmentContent(String shift, String date) {
        return String.format("Bạn đã được phân công ca làm việc %s vào ngày %s.", shift, date);
    }

    public static String createCheckinSuccessContent(String time) {
        return String.format("Bạn đã checkin thành công vào lúc %s. Chúc bạn có một ngày làm việc hiệu quả!", time);
    }

    public static String createSalaryNotificationContent(String month, String amount) {
        return String.format("Lương tháng %s của bạn đã được chuyển với tổng số tiền %s.", month, amount);
    }

    // MANAGER notification content methods
    public static String createManagerNotificationSentContent(String employeeName) {
        return String.format("Thông báo của bạn đã được gửi thành công đến %s.", employeeName);
    }

    public static String createManagerNotificationSentContentForMany(List<String> employeeNames) {
        if (employeeNames.size() == 2) {
            return String.format("Thông báo của bạn đã được gửi thành công đến %s, %s.", employeeNames.get(0), employeeNames.get(1));
        } else {
            return String.format("Thông báo của bạn đã được gửi thành công đến %s, %s, ... .", employeeNames.get(0), employeeNames.get(1));
        }
    }

    public static String createManagerNotificationSentContentForAll() {
        return "Thông báo của bạn đã được gửi thành công đến tất cả nhân viên.";
    }

    public static String createManagerNotificationReceivedContent(String managerName, String content) {
        return String.format("Bạn có thông báo mới từ %s: \"%s\"", managerName, content);
    }

    // INVOICE notification content methods
    public static String createInvoiceSuccessContent(UUID invoiceId, String warehouseName) {
        return String.format("Phiếu nhập kho #%s đã được tạo thành công cho kho %s.",
                invoiceId.toString().substring(0, 8), warehouseName);
    }

    public static String createInvoiceFailedContent(UUID invoiceId, String reason) {
        return String.format("Nhập kho thất bại cho phiếu #%s. Lý do: %s",
                invoiceId.toString().substring(0, 8), reason);
    }

    public static String createInvoiceUpdatedContent(UUID invoiceId) {
        return String.format("Phiếu nhập kho #%s đã được cập nhật thành công.",
                invoiceId.toString().substring(0, 8));
    }

    public static String createInvoiceCancelledContent(UUID invoiceId) {
        return String.format("Phiếu nhập kho #%s đã bị hủy.", invoiceId.toString().substring(0, 8));
    }

    // TRANSFER notification content methods
    public static String createTransferSuccessContent(UUID transferId, String branchName) {
        return String.format("Phiếu xuất kho #%s đến chi nhánh %s đã được tạo thành công.",
                transferId.toString().substring(0, 8), branchName);
    }

    public static String createTransferFailedContent(UUID transferId, String reason) {
        return String.format("Xuất kho thất bại cho phiếu #%s. Lý do: %s",
                transferId.toString().substring(0, 8), reason);
    }

    public static String createTransferInsufficientContent(UUID transferId, String itemName) {
        return String.format("Xuất kho thất bại cho phiếu #%s. Kho không đủ số lượng sản phẩm '%s'.",
                transferId.toString().substring(0, 8), itemName);
    }

    public static String createTransferUpdatedContent(UUID transferId) {
        return String.format("Phiếu xuất kho #%s đã được cập nhật thành công.",
                transferId.toString().substring(0, 8));
    }

    public static String createTransferCancelledContent(UUID transferId) {
        return String.format("Phiếu xuất kho #%s đã bị hủy.", transferId.toString().substring(0, 8));
    }
}
