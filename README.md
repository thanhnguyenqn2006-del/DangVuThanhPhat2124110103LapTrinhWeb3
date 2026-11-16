# LAB 01 – KHỞI TẠO VÀ GIAO TIẾP GIỮA CÁC MICROSERVICES
**Môn học:** Lập trình Web 3 – Spring Boot & Microservices

---

## 1. Cấu trúc Dự án
```
lab01-microservices/
├── .gitignore
├── Lab01_Microservices.postman_collection.json
├── README.md
├── frontend/
│   └── index.html                # Giao diện Frontend kiểm thử Microservices
├── product-service/              # Microservice Quản lý Sản phẩm (Port 8081)
│   ├── src/
│   │   └── main/java/com/example/productservice/
│   │       ├── ProductServiceApplication.java
│   │       └── ProductController.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
└── order-service/                # Microservice Quản lý Đơn hàng (Port 8082)
    ├── src/
    │   └── main/java/com/example/orderservice/
    │       ├── OrderServiceApplication.java
    │       └── OrderController.java
    ├── src/main/resources/application.properties
    └── pom.xml
```

---

## 2. Hướng dẫn chạy 2 Services đồng thời

Mở 2 Terminal riêng biệt trong VS Code (Terminal ➔ New Terminal):

### Terminal 1: Chạy Product Service
```bash
cd product-service
mvn spring-boot:run
# hoặc dùng wrapper:
./mvnw spring-boot:run
```
*Dịch vụ sẽ khởi động tại cổng **8081**.*

### Terminal 2: Chạy Order Service
```bash
cd order-service
mvn spring-boot:run
# hoặc dùng wrapper:
./mvnw spring-boot:run
```
*Dịch vụ sẽ khởi động tại cổng **8082**.*

---

## 3. Danh sách REST API & Luồng hoạt động

### 3.1. Product Service (`http://localhost:8081`)
| Phương thức | Endpoint | Mô tả | Dữ liệu mẫu trả về |
|-------------|----------|-------|-------------------|
| `GET` | `/api/products` | Lấy danh sách tất cả sản phẩm | `[{"id": 1, "name": "Mouse", "price": 150000}, {"id": 2, "name": "Keyboard", "price": 250000}]` |
| `GET` | `/api/products/{id}` | Lấy chi tiết sản phẩm theo ID | `{"id": 1, "name": "Mouse", "price": 150000}` |

### 3.2. Order Service (`http://localhost:8082`)
| Phương thức | Endpoint | Mô tả | Cơ chế |
|-------------|----------|-------|--------|
| `GET` | `/api/orders` | Lấy danh sách đơn hàng | Trả về JSON đơn hàng nội bộ |
| `GET` | `/api/orders/product/{id}` | **Giao tiếp Microservices**: Order Service gọi Product Service | Sử dụng `RestClient` gọi sang `http://localhost:8081/api/products/{id}` |
| `GET` | `/api/orders/{id}` | *(Mở rộng)* Chi tiết đơn hàng tổng hợp từ Product Service kèm tổng tiền | Order Service lấy Order, gọi Product Service lấy giá, tính `totalPrice = price * quantity` |

---

## 4. Kiểm thử với Frontend
Một giao diện Frontend hoàn chỉnh đã được xây dựng sẵn tại:
👉 `frontend/index.html`

**Cách chạy:**
1. Mở file `frontend/index.html` trực tiếp bằng trình duyệt (Chrome/Edge) hoặc dùng extension **Live Server** trong VS Code.
2. Giao diện hỗ trợ:
   - Tự động kiểm tra trạng thái **ONLINE/OFFLINE** của Product Service và Order Service.
   - Nút test API lấy danh sách và chi tiết sản phẩm.
   - Nút test API lấy danh sách đơn hàng.
   - Nút test giao tiếp liên dịch vụ: Order Service gọi Product Service.
   - Nút test bài tập mở rộng hiển thị đầy đủ chi tiết đơn hàng + thông tin sản phẩm và tổng tiền.

---

## 5. Kiểm thử bằng Postman
File collection kiểm thử sẵn: `Lab01_Microservices.postman_collection.json`.

1. Mở Postman.
2. Bấm nút **Import** ở góc trên bên trái, chọn file `Lab01_Microservices.postman_collection.json`.
3. Chạy lần lượt các request:
   - `GET http://localhost:8081/api/products`
   - `GET http://localhost:8081/api/products/1`
   - `GET http://localhost:8082/api/orders`
   - `GET http://localhost:8082/api/orders/product/1`
   - `GET http://localhost:8082/api/orders/101`

---

## 6. Đẩy mã nguồn lên GitHub

1. Tạo repository mới trên GitHub (ví dụ: `lab01-microservices`).
2. Mở Terminal tại thư mục gốc `lab01-microservices` và chạy:
```bash
git remote add origin https://github.com/USERNAME/lab01-microservices.git
git push -u origin main
```
*(Thay `USERNAME` bằng username tài khoản GitHub của bạn)*.
