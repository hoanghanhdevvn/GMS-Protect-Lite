# GMS Protect Lite

**Lightweight GMS & FCM Protection for Xiaomi HyperOS**

> **Tối giản cơ chế · Tối ưu hoạt động nền · Tự động bảo vệ**

**GMS Protect Lite** là ứng dụng hỗ trợ duy trì hoạt động của **Google Mobile Services (GMS)** và kết nối **Firebase Cloud Messaging (FCM)** trên thiết bị Xiaomi chạy **HyperOS**, đặc biệt là ROM nội địa Trung Quốc.

Ứng dụng được xây dựng theo một nguyên tắc: **không giữ tiến trình chạy thường trực chỉ để chờ sự kiện**. Thay vào đó, GMS Protect Lite tận dụng khả năng lập lịch theo sự kiện của Android để kiểm tra và khôi phục cấu hình bảo vệ khi cần, nhằm hạn chế công việc nền không cần thiết.

## 📥 Tải xuống

### **[⬇️ Download GMS Protect Lite — Latest Release](https://github.com/hoanghanhdevvn/GMS-Protect-Lite/releases/latest)**

Xem danh sách các phiên bản tại **[GitHub Releases](https://github.com/hoanghanhdevvn/GMS-Protect-Lite/releases)**.

Mở bản phát hành bạn muốn cài đặt, chọn tệp **`.apk`** trong mục **Assets** rồi cài đặt lên điện thoại. APK được phân phối qua **Releases**, không phải thư mục mã nguồn của repository.

## ✨ Tính năng nổi bật

| Tính năng | Mô tả |
| --- | --- |
| **Không chạy Foreground Service thường trực** | Không cần duy trì thông báo của một dịch vụ chạy nền liên tục chỉ để theo dõi MILLET. |
| **Theo dõi theo sự kiện** | Dùng `JobScheduler` để nhận sự kiện thay đổi cấu hình bảo vệ khi Android chuyển công việc đến ứng dụng. |
| **MILLET Protection** | Hỗ trợ duy trì các mục cần thiết trong `MILLET_NO_RESTRICT_APP` cho Google Play services, Google Play Store và chính ứng dụng. |
| **Tự khôi phục cấu hình** | Kiểm tra và thử bổ sung mục bảo vệ bị thiếu, thay vì ghi lại danh sách không cần thiết. |
| **FCM Recovery** | Có công cụ hỗ trợ kiểm tra và khôi phục kết nối FCM trong những trường hợp được ứng dụng hỗ trợ. |
| **GMS Binding** | Hỗ trợ tương tác với GMS khi tiến trình ứng dụng đang hoạt động; không phải cơ chế giữ tiến trình GMS sống vĩnh viễn. |
| **Không yêu cầu Root / ADB / Shizuku** | Quy trình sử dụng thông thường dựa trên các quyền mà thiết bị cho phép người dùng cấp. |

## ⚙️ Cơ chế hoạt động

GMS Protect Lite ưu tiên **Event-Driven Protection** thay cho việc giữ một dịch vụ hoạt động liên tục:

1. Khi người dùng bật bảo vệ, ứng dụng kiểm tra quyền cần thiết và cấu hình danh sách MILLET.
2. Ứng dụng đăng ký tác vụ theo dõi thay đổi thông qua `JobScheduler`.
3. Khi không có việc cần xử lý, ứng dụng không cần giữ Foreground Service thường trực.
4. Nếu Android kích hoạt công việc sau một thay đổi, Lite kiểm tra danh sách và **thử khôi phục** các mục còn thiếu.
5. Người dùng có thể sử dụng chức năng hỗ trợ khôi phục FCM khi cần.

```text
Thiết lập MILLET Protection
            ↓
Đăng ký JobScheduler
            ↓
Chờ sự kiện hệ thống
(không cần service thường trực)
            ↓
Kiểm tra danh sách bảo vệ
            ↓
Chỉ bổ sung khi phát hiện thiếu
```

> **Nguyên tắc tối ưu:** Không cố giữ ứng dụng chạy liên tục. Chỉ xử lý khi có công việc cần thiết và Android cho phép thực thi.

**Lưu ý:** `JobScheduler` chịu sự điều phối của Android/HyperOS. Một tác vụ đã đăng ký không được bảo đảm kích hoạt ngay lập tức, đặc biệt trong Doze hoặc khi có giới hạn nền.

## 🔋 Tối ưu hoạt động nền

Thiết kế của GMS Protect Lite hướng đến giảm tác vụ không cần thiết khi điện thoại ở trạng thái chờ:

- Không sử dụng Foreground Service thường trực để canh trạng thái bảo vệ.
- Không dùng vòng lặp kiểm tra nhanh liên tục.
- Ưu tiên sự kiện hệ thống thay vì tự đánh thức CPU để kiểm tra theo nhịp ngắn.
- Chỉ thử cập nhật danh sách MILLET khi phát hiện mục cần bảo vệ bị thiếu.
- Để Android quyết định thời điểm thực thi công việc đã lập lịch.

Những điểm trên phản ánh **kiến trúc hướng đến tiết kiệm tài nguyên**, không phải số liệu đo pin hay cam kết tiêu thụ thấp hơn trên mọi thiết bị.

### So sánh cách tối ưu nền với FCM Guard

| Cách tiếp cận | **GMS Protect Lite** | **[FCM Guard](https://github.com/ReedGAOOO/FCMGuard-HyperOS)** |
| --- | --- | --- |
| Theo dõi thay đổi MILLET | `JobScheduler` nhận công việc khi hệ thống kích hoạt | `ContentObserver` theo dõi thay đổi khi tiến trình đang hoạt động |
| Dịch vụ nền thường trực | Không sử dụng Foreground Service thường trực | Có chế độ Foreground Service **tùy chọn** |
| Kiểm tra dự phòng | Không sử dụng vòng polling liên tục | Có kiểm tra dự phòng khoảng 30 phút **khi tiến trình đang chạy** |
| Ưu tiên kiến trúc | Giảm nhu cầu duy trì tiến trình chờ | Theo dõi nhanh khi tiến trình còn chạy, có chế độ tăng khả năng tồn tại của tiến trình |

**Đây là so sánh về cách tổ chức hoạt động nền, không phải xếp hạng ứng dụng.** Mức hao pin và thời gian phản hồi thực tế còn tùy ROM, cài đặt và cách Android quản lý tiến trình.

## 📱 Cài đặt và sử dụng

1. Tải APK tại **[Releases](https://github.com/hoanghanhdevvn/GMS-Protect-Lite/releases/latest)**.
2. Cài đặt và mở **GMS Protect Lite**.
3. Cấp quyền **sửa đổi cài đặt hệ thống** khi ứng dụng yêu cầu và thiết bị hỗ trợ.
4. Bật bảo vệ, sau đó kiểm tra trạng thái trên màn hình chính.
5. Nếu thông báo vẫn chậm, kiểm tra trạng thái GMS/FCM và cài đặt pin của ứng dụng nhận thông báo trên HyperOS.

**Package:** `com.hanh.gmsprotect.lite`

## ❓ Câu hỏi thường gặp

**Có cần root, ADB hoặc Shizuku không?**  
Không cần cho quy trình sử dụng thông thường. Tuy nhiên, thao tác với MILLET phụ thuộc quyền và chính sách của phiên bản HyperOS đang dùng.

**MILLET Protection có giống quyền “Không hạn chế pin” không?**  
Không. `MILLET_NO_RESTRICT_APP` là danh sách riêng của Xiaomi, không đồng nghĩa với quyền miễn tối ưu pin Android/Doze hay tất cả cài đặt quản lý pin HyperOS.

**Có cần mở ứng dụng liên tục không?**  
Không cần giữ giao diện mở để sử dụng công việc theo sự kiện đã đăng ký. Tuy nhiên, Android/HyperOS có thể trì hoãn hoặc hạn chế việc thực thi trong nền.

**Ứng dụng có bảo đảm thông báo đến 100% không?**  
Không. Thông báo còn phụ thuộc mạng, Google Play services, ứng dụng nhận tin, Doze và chính sách quản lý nền của HyperOS.

**Ứng dụng có tự cấp được quyền “Không hạn chế pin” một cách âm thầm không?**  
Không. Quyền miễn tối ưu pin của Android và các cài đặt riêng của HyperOS có thể cần người dùng thao tác trong phần cài đặt hệ thống.

## ⚠️ Tương thích và giới hạn

- Tập trung vào **Xiaomi HyperOS**; hiệu quả và khả năng truy cập MILLET khác nhau theo phiên bản ROM.
- Không sửa đổi firmware hoặc thay thế hệ thống thông báo Android.
- Không bảo đảm FCM luôn kết nối hay tác vụ JobScheduler được thực thi tức thời.
- Không thể bỏ qua mọi giới hạn nền của Android/HyperOS.
- Nên cập nhật từ **GitHub Releases** và đọc ghi chú phiên bản trước khi cài.

## 👨‍💻 Tác giả

**Hoàng Đức Hanh**  
GitHub: [@hoanghanhdevvn](https://github.com/hoanghanhdevvn)  
Repository: [GMS-Protect-Lite](https://github.com/hoanghanhdevvn/GMS-Protect-Lite)

---

**GMS Protect Lite — Less Background Activity. Smarter Protection.**

*Dự án độc lập, không liên kết chính thức với Google hoặc Xiaomi.*
