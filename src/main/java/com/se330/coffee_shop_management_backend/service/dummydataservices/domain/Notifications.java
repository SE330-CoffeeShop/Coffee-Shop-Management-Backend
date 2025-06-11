package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.NotificationRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.se330.coffee_shop_management_backend.util.Constants.NotificationTypeEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class Notifications {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create() {
        createNotifications();
    }

    private void createNotifications() {
        log.info("Creating notifications...");

        // Get all users and managers
        List<User> allUsers = userRepository.findAll();
        List<User> managers = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);

        if (allUsers.isEmpty()) {
            log.error("Cannot create notifications: No users found");
            return;
        }

        Random random = new Random();
        List<Notification> notifications = new ArrayList<>();

        int totalNotificationsTarget = 100000;
        int notificationsPerUser = Math.max(1, totalNotificationsTarget / allUsers.size());

        for (User user : allUsers) {
            for (int i = 0; i < notificationsPerUser; i++) {
                // Select random notification type
                Constants.NotificationTypeEnum notificationType = Constants.NotificationTypeEnum.values()[
                        random.nextInt(Constants.NotificationTypeEnum.values().length)];

                // Generate relevant Vietnamese content
                String content = generateVietnameseContent(notificationType, random);

                // Decide sender: null (system) or random manager
                User sender = random.nextBoolean() ? null :
                        (!managers.isEmpty() ? managers.get(random.nextInt(managers.size())) : null);

                // Create notification
                Notification notification = Notification.builder()
                        .notificationType(notificationType)
                        .notificationContent(content)
                        .sender(sender)
                        .receiver(user)
                        .isRead(random.nextBoolean())
                        .build();

                notifications.add(notification);
            }
        }

        notificationRepository.saveAll(notifications);
        log.info("Created {} notifications for {} users", notifications.size(), allUsers.size());
    }

    private String generateVietnameseContent(Constants.NotificationTypeEnum type, Random random) {
        return switch (type) {
            case ORDER -> {
                String[] orderMessages = {
                        "Đơn hàng #ORD-%04d đã được tạo thành công",
                        "Đơn hàng #ORD-%04d đã được thanh toán",
                        "Đơn hàng #ORD-%04d đang được xử lý",
                        "Đơn hàng #ORD-%04d đã hoàn thành",
                        "Đơn hàng #ORD-%04d đã bị hủy do hết nguyên liệu",
                        "Cảm ơn bạn đã mua hàng tại cửa hàng của chúng tôi"
                };
                yield String.format(orderMessages[random.nextInt(orderMessages.length)], random.nextInt(10000));
            }
            case DISCOUNT -> {
                String[] discountMessages = {
                        "Khuyến mãi mới: Giảm 20% cho tất cả đồ uống",
                        "Ưu đãi đặc biệt: Mua 1 tặng 1 vào thứ hai",
                        "Khuyến mãi sắp kết thúc! Còn 3 ngày để nhận ưu đãi",
                        "Mừng sinh nhật với ưu đãi 50% cho đơn hàng đầu tiên",
                        "Giảm 30% cho đơn hàng trên 100.000đ"
                };
                yield discountMessages[random.nextInt(discountMessages.length)];
            }
            case SYSTEM -> {
                String[] systemMessages = {
                        "Hệ thống sẽ bảo trì từ 00:00 đến 02:00 ngày mai",
                        "Đã cập nhật phiên bản mới của ứng dụng",
                        "Thay đổi chính sách bảo mật từ ngày 01/06/2023",
                        "Cập nhật điều khoản sử dụng dịch vụ",
                        "Hệ thống đang gặp sự cố, chúng tôi đang khắc phục"
                };
                yield systemMessages[random.nextInt(systemMessages.length)];
            }
            case INVENTORY -> {
                String[] inventoryMessages = {
                        "Cảnh báo: Cà phê arabica sắp hết hàng",
                        "Đã nhập thêm nguyên liệu vào kho",
                        "Kiểm kê kho định kỳ vào cuối tháng",
                        "Một số nguyên liệu sắp hết hạn sử dụng",
                        "Cập nhật tồn kho thành công"
                };
                yield inventoryMessages[random.nextInt(inventoryMessages.length)];
            }
            case EMPLOYEE -> {
                String[] employeeMessages = {
                        "Chào mừng bạn gia nhập chi nhánh mới",
                        "Ca làm việc của bạn đã được cập nhật",
                        "Lương tháng này đã được chuyển",
                        "Checkin thành công vào lúc 08:00",
                        "Vui lòng hoàn thành báo cáo cuối ngày"
                };
                yield employeeMessages[random.nextInt(employeeMessages.length)];
            }
            case MANAGER -> {
                String[] managerMessages = {
                        "Quản lý đã gửi một thông báo mới",
                        "Cuộc họp nhân viên vào ngày mai lúc 09:00",
                        "Vui lòng cập nhật tình trạng công việc",
                        "Nhắc nhở: Đóng cửa hàng đúng giờ",
                        "Kiểm tra và báo cáo tồn kho cuối ngày"
                };
                yield managerMessages[random.nextInt(managerMessages.length)];
            }

            // Other notification types with Vietnamese content
            case INVOICE -> {
                String[] invoiceMessages = {
                        "Hóa đơn nhập kho #INV-%04d đã được tạo",
                        "Đã nhập thành công 50kg cà phê vào kho",
                        "Cập nhật hóa đơn nhập kho #INV-%04d",
                        "Hóa đơn #INV-%04d đã được thanh toán",
                        "Một số mặt hàng thiếu so với đơn đặt hàng"
                };
                yield String.format(invoiceMessages[random.nextInt(invoiceMessages.length)], random.nextInt(10000));
            }
            case TRANSFER -> {
                String[] transferMessages = {
                        "Đã xuất 20kg đường cho chi nhánh Quận 1",
                        "Yêu cầu chuyển kho đã được phê duyệt",
                        "Chuyển kho thành công: 30 hộp sữa đến chi nhánh Quận 3",
                        "Chuyển kho bị từ chối: Kho nguồn không đủ hàng",
                        "Cập nhật thông tin chuyển kho #TRF-%04d"
                };
                yield String.format(transferMessages[random.nextInt(transferMessages.length)], random.nextInt(10000));
            }
            case BRANCH -> {
                String[] branchMessages = {
                        "Chi nhánh mới đã mở tại Quận 7",
                        "Chi nhánh Quận 2 sẽ đóng cửa để sửa chữa từ ngày 15/07",
                        "Chi nhánh Quận 1 đạt doanh thu cao nhất tháng này",
                        "Cập nhật giờ mở cửa cho chi nhánh Thủ Đức",
                        "Kiểm tra thiết bị tại chi nhánh Bình Thạnh"
                };
                yield branchMessages[random.nextInt(branchMessages.length)];
            }
            case SUPPLIER -> {
                String[] supplierMessages = {
                        "Nhà cung cấp mới: Công ty TNHH Cà Phê Việt",
                        "Cập nhật thông tin liên hệ nhà cung cấp",
                        "Đã hủy hợp đồng với nhà cung cấp Nguyên liệu ABC",
                        "Đơn hàng từ nhà cung cấp đã được xác nhận",
                        "Đàm phán giá thành công với nhà cung cấp sữa"
                };
                yield supplierMessages[random.nextInt(supplierMessages.length)];
            }
            case PRODUCT -> {
                String[] productMessages = {
                        "Sản phẩm mới: Cà phê dừa đã có mặt tại cửa hàng",
                        "Cập nhật giá cho một số sản phẩm",
                        "Ngừng kinh doanh sản phẩm Trà sữa trân châu",
                        "Thêm kích cỡ mới cho các loại đồ uống",
                        "Cập nhật công thức cho Cappuccino"
                };
                yield productMessages[random.nextInt(productMessages.length)];
            }
            case WAREHOUSE -> {
                String[] warehouseMessages = {
                        "Kho mới đã được thêm vào hệ thống",
                        "Cập nhật địa chỉ kho hàng trung tâm",
                        "Kho hàng quận 9 sẽ đóng cửa để kiểm kê vào ngày 20/08",
                        "Cập nhật quy trình xuất nhập kho",
                        "Phân công nhân viên quản lý kho mới"
                };
                yield warehouseMessages[random.nextInt(warehouseMessages.length)];
            }
            default -> "Thông báo hệ thống mới";
        };
    }
}
