# GMS Protect Lite

**Lightweight GMS & FCM Protection for Xiaomi HyperOS**

> **Tối giản cơ chế · Tối ưu tài nguyên · Hoạt động tự động**

GMS Protect Lite là ứng dụng hỗ trợ duy trì hoạt động của **Google Mobile Services (GMS)** và kết nối **Firebase Cloud Messaging (FCM)** trên các thiết bị Xiaomi chạy **HyperOS**, đặc biệt là ROM nội địa Trung Quốc.

Ứng dụng tập trung vào một mục tiêu: **hỗ trợ bảo vệ GMS khi cần, thay vì duy trì một dịch vụ chạy nền liên tục**. Thiết kế này hướng đến giảm số lần đánh thức CPU và tác vụ không cần thiết, nhưng hiệu quả tiết kiệm pin thực tế còn phụ thuộc thiết bị và phiên bản hệ thống.

## 📥 Tải xuống

### **[⬇️ Download GMS Protect Lite — Latest Release](https://github.com/hoanghanhdevvn/GMS-Protect-Lite/releases/latest)**

- Xem mọi phiên bản: **[GitHub Releases](https://github.com/hoanghanhdevvn/GMS-Protect-Lite/releases)**
- Tải tệp **`.apk`** trong mục **Assets** của bản phát hành, không cần tìm APK trong thư mục mã nguồn.
- Chỉ cài APK từ nguồn bạn tin cậy. Một số phiên bản HyperOS có thể yêu cầu cho phép cài đặt ứng dụng từ nguồn ngoài.

## ✨ Điểm nổi bật

| Tính năng | Mô tả |
| --- | --- |
| **Thiết kế tối giản** | Không duy trì Foreground Service thường trực hay vòng lặp kiểm tra dày đặc. |
| **MILLET Protection** | Hỗ trợ duy trì mục bảo vệ cho Google Play services và Google Play Store trong `MILLET_NO_RESTRICT_APP`. |
| **Tự khôi phục theo sự kiện** | Dùng `JobScheduler` để nhận sự kiện thay đổi và thử khôi phục danh sách bảo vệ. |
| **FCM Recovery** | Có chức năng hỗ trợ kiểm tra/khôi phục kết nối FCM trong những tình huống được ứng dụng hỗ trợ. |
| **GMS Binding** | Hỗ trợ tương tác với GMS khi tiến trình ứng dụng còn hoạt động; không phải dịch vụ giữ GMS sống vĩnh viễn. |
| **Không Root / ADB / Shizuku** | Không yêu cầu các công cụ này cho quy trình sử dụng thông thường, nếu quyền hệ thống cần thiết được cho phép. |
| **Lite đúng nghĩa** | Loại bỏ cơ chế MiPush và Aurogon của bản Full để giảm thành phần và tác vụ bổ sung. |

## ⚙️ Cơ chế hoạt động

GMS Protect Lite ưu tiên cách tiếp cận **event-driven** thay vì liên tục chạy nền:

1. Khi người dùng bật bảo vệ, ứng dụng kiểm tra quyền cần thiết và thiết lập danh sách MILLET phù hợp.
2. Ứng dụng đăng ký công việc theo dõi thay đổi bằng `JobScheduler`.
3. Khi không có việc cần xử lý, không cần duy trì Foreground Service chỉ để chờ.
4. Nếu Android chuyển sự kiện thay đổi đến Job, ứng dụng kiểm tra và **thử** khôi phục các mục MILLET bị thiếu.
5. Khi cần, người dùng có thể sử dụng tính năng hỗ trợ kết nối lại FCM.

```text
Thiết lập bảo vệ MILLET
          ↓
Đăng ký JobScheduler
          ↓
Chờ sự kiện (không cần service thường trực)
          ↓
Kiểm tra danh sách → Khôi phục nếu thiếu
```

**Nguyên tắc thiết kế:** *Không cố giữ ứng dụng chạy liên tục — chỉ xử lý khi hệ thống cho phép và có việc cần làm.*

> **Lưu ý kỹ thuật:** `JobScheduler` chịu sự điều phối của Android/HyperOS. Việc đăng ký thành công **không bảo đảm** tác vụ luôn chạy tức thời, nhất là khi thiết bị ở Doze hoặc bị hệ thống hạn chế.

## 📱 Hướng dẫn cài đặt và sử dụng

1. Mở **[Releases](https://github.com/hoanghanhdevvn/GMS-Protect-Lite/releases/latest)** và tải APK trong **Assets**.
2. Cài đặt APK, sau đó mở **GMS Protect Lite**.
3. Cấp **quyền sửa đổi cài đặt hệ thống** (nếu được yêu cầu và thiết bị hỗ trợ) để ứng dụng có thể cập nhật danh sách MILLET.
4. Bật chức năng bảo vệ và kiểm tra trạng thái trong ứng dụng.
5. Nếu thông báo vẫn chậm, kiểm tra kết nối GMS/FCM và cài đặt tiết kiệm pin của HyperOS.

**Package name:** `com.hanh.gmsprotect.lite` — khác với bản Full, nên có thể cài song song (nếu bản APK phát hành đúng package này).

## 🔋 Vì sao bản Lite hướng đến tối ưu pin?

Ứng dụng được thiết kế để **giảm hoạt động nền không cần thiết**, thay vì duy trì liên tục một tiến trình, Foreground Service hoặc vòng lặp thăm dò nhanh. Việc sử dụng `JobScheduler` cho phép Android quyết định thời điểm thích hợp để chạy công việc.

Điều này là **mục tiêu thiết kế, không phải cam kết mức tiêu thụ pin**. Cần đo kiểm thực tế trên từng thiết bị để kết luận mức tiết kiệm hoặc độ ổn định thông báo.



| Thành phần | GMS Protect Lite 
| MILLET Protection | ✅ Có 
| JobScheduler tự khôi phục | ✅ Có 
| Hỗ trợ FCM Reconnect | ✅ Có 
| GMS Binding | ✅ Có 
## ❓ Câu hỏi thường gặp

**Có cần root, ADB hoặc Shizuku không?**  
Không cần trong quy trình thiết lập thông thường. Tuy nhiên, khả năng sửa MILLET phụ thuộc vào API, quyền và chính sách của bản HyperOS đang sử dụng.

**MILLET Protection có phải là quyền “Không hạn chế pin” của Android không?**  
Không. Danh sách `MILLET_NO_RESTRICT_APP` là một phần cơ chế riêng của Xiaomi và **không đồng nghĩa** với việc ứng dụng được tự cấp quyền miễn tối ưu pin Android/Doze.

**Ứng dụng có bảo đảm thông báo đến 100% không?**  
Không. Thông báo còn phụ thuộc vào GMS, mạng, ứng dụng nhận tin, chế độ Doze và chính sách quản lý nền của HyperOS.

**Có cần mở GMS Protect Lite liên tục không?**  
Không cần giữ giao diện mở liên tục để sử dụng cơ chế theo sự kiện. Tuy nhiên, nếu Android/HyperOS trì hoãn hoặc chặn công việc nền, việc tự khôi phục cũng có thể bị chậm.

**Có thể cài cùng bản Full không?**  
Có, nếu APK Lite sử dụng package `com.hanh.gmsprotect.lite`. Quyền và thiết lập của hai ứng dụng được quản lý riêng.

## ⚠️ Giới hạn và tương thích

- Tập trung vào **Xiaomi HyperOS**; một số phiên bản ROM có thể không hỗ trợ đầy đủ cơ chế MILLET.
- Không sửa đổi firmware, không thay thế hệ thống thông báo Android.
- Không đảm bảo FCM luôn kết nối hoặc khôi phục ngay khi tắt màn hình.
- Một số quyền/cấu hình pin của Android **không thể được ứng dụng thường tự cấp âm thầm**.
- Nên tải bản mới nhất và kiểm tra phần ghi chú của từng Release trước khi cập nhật.

## 👨‍💻 Tác giả

**Hoàng Đức Hanh**  
GitHub: [@hoanghanhdevvn](https://github.com/hoanghanhdevvn)  
Repository: [GMS-Protect-Lite](https://github.com/hoanghanhdevvn/GMS-Protect-Lite)

---

**GMS Protect Lite — Less Background Activity. Smarter Protection.**

*Dự án độc lập, không có liên kết chính thức với Google hoặc Xiaomi.*
