Hệ thống quản lý phòng khám da liễu 
Ứng dụng Spring Boot hỗ trợ quản lý phòng khám da liễu, tập trung vào hỗ trợ khám bệnh , xác thực người dùng, quản lý media, gửi email và tích hợp AI Gemini.
Mục Đích Dự Án
Quản lý thông tin phòng khám , tài khoản người dùng và admin
Đăng nhập/xác thực bằng JWT và Google OAuth
Quản lý hình ảnh, tài liệu qua Cloudinary
Gửi email thông báo qua Gmail
Tích hợp AI Gemini hỗ trợ nhiều API key
Chức Năng Chính

Kết nối cơ sở dữ liệu bảo mật qua file .env
Xác thực người dùng bằng JWT
Đăng nhập bằng Google OAuth
Quản lý media với Cloudinary
Gửi email qua Gmail
Tích hợp AI Gemini
Hướng Dẫn Cài Đặt
Clone dự án
git clone <đường-dẫn-repo>
cd <thư-mục-dự-án>
Cấu hình biến môi trường
Sao chép file .env.example thành .env và điền thông tin cấu hình theo hướng dẫn trong file.
Build dự án
./gradlew build
Chạy ứng dụng
./gradlew bootRun
Biến Môi Trường Cần Thiết
DB_PASSWORD
JWT_SECRET, JWT_EXPIRATION
CLOUD_NAME, CLOUD_API_KEY, CLOUD_API_SECRET
MAIL_USERNAME, MAIL_PASSWORD
GOOGLE_CLIENT_ID
GEMINI_API_KEYS (nhiều key, phân cách bằng dấu phẩy)
