# 🔗 URL Shortener - Bitly Clone

A full-stack URL shortening service built with Spring Boot and React, featuring user authentication, analytics, and URL expiration management.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.8-brightgreen)
![React](https://img.shields.io/badge/React-18.2.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-green)

## 📋 Project Overview

This URL Shortener application is a complete clone of Bitly, providing users with the ability to create short URLs from long ones. The application includes user authentication, URL management, click analytics, and URL expiration features. It's built with a Spring Boot backend and a modern React frontend.

## ✨ Features

### 🔐 Authentication & Authorization
- User registration and login
- JWT-based authentication
- Role-based access control
- Secure password handling

### 🔗 URL Management
- Create short URLs from long URLs
- Custom URL expiration (1-365 days)
- URL validation and sanitization
- Bulk URL management

### 📊 Analytics & Tracking
- Click count tracking
- Detailed click analytics with timestamps
- Date-range analytics
- User-specific analytics dashboard
- Interactive charts and graphs

### 🚀 Advanced Features
- Automatic URL expiration
- 302 redirects for valid URLs
- 410 Gone for expired URLs
- 404 Not Found for invalid URLs
- CORS support for frontend integration
- Responsive design

## 🛠️ Tech Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.4.8** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **MySQL 8.0** - Database
- **JWT (JSON Web Tokens)** - Authentication tokens
- **Maven** - Build tool
- **Lombok** - Code generation

### Frontend
- **React 18.2.0** - UI framework
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Styling framework
- **Material-UI** - Component library
- **Chart.js** - Data visualization
- **Axios** - HTTP client
- **React Router** - Client-side routing
- **React Query** - Data fetching and caching

## 📁 Project Structure

```
urlshortenersb/
├── src/main/java/com/url/urlshortenersb/
│   ├── controller/           # REST API controllers
│   │   ├── AuthController.java
│   │   ├── RedirectController.java
│   │   └── UrlMappingController.java
│   ├── models/              # JPA entities
│   │   ├── User.java
│   │   ├── UrlMapping.java
│   │   └── ClickEvent.java
│   ├── dtos/                # Data Transfer Objects
│   │   ├── CreateShortUrlRequest.java
│   │   ├── UrlMappingDTO.java
│   │   └── ClickEventDTO.java
│   ├── repository/          # Data access layer
│   │   ├── UserRepository.java
│   │   ├── UrlMappingRepository.java
│   │   └── ClickEventRepository.java
│   ├── service/             # Business logic
│   │   ├── UserService.java
│   │   ├── UrlMappingService.java
│   │   └── UrlCleanupService.java
│   └── security/            # Security configuration
│       ├── WebSecurityConfig.java
│       └── jwt/
│           ├── JwtUtils.java
│           └── JwtAuthenticationFilter.java
├── src/main/resources/
│   └── application.properties
└── url-shortener-frontend/  # React frontend
    ├── src/
    │   ├── components/      # React components
    │   ├── api/            # API service layer
    │   ├── contextApi/     # React context
    │   └── utils/          # Utility functions
    └── package.json
```

## 🚀 API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/public/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securepassword123"
}
```

#### Login User
```http
POST /api/auth/public/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "securepassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "johndoe",
  "email": "john@example.com"
}
```

### URL Management Endpoints

#### Create Short URL
```http
POST /api/urls/shorten
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "originalUrl": "https://www.example.com/very/long/url/here",
  "expiryInDays": 30
}
```

**Response:**
```json
{
  "id": 1,
  "originalUrl": "https://www.example.com/very/long/url/here",
  "shortUrl": "abc123",
  "clickCount": 0,
  "createdDate": "2025-01-27T10:30:00",
  "expiryDate": "2025-02-26T10:30:00"
}
```

#### Get User's URLs
```http
GET /api/urls/myurls
Authorization: Bearer <your-jwt-token>
```

#### Get URL Analytics
```http
GET /api/urls/analytics/abc123?StartDate=2025-01-01T00:00:00&EndDate=2025-01-31T23:59:59
Authorization: Bearer <your-jwt-token>
```

#### Get Total Clicks by Date
```http
GET /api/urls/totalClicks?StartDate=2025-01-01&EndDate=2025-01-31
Authorization: Bearer <your-jwt-token>
```

### Redirect Endpoint

#### Redirect Short URL
```http
GET /{shortUrl}
```

**Responses:**
- `302 Found` - Redirects to original URL
- `404 Not Found` - Short URL doesn't exist
- `410 Gone` - Short URL has expired

## 🛠️ Setup Instructions

### Prerequisites
- Java 21 or higher
- Node.js 18 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd urlshortenersb
   ```

2. **Set up environment variables**
   Create a `.env` file or set the following environment variables:
   ```bash
   export DATABASE_URL=jdbc:mysql://localhost:3306/urlshortener
   export DATABASE_USERNAME=your_db_username
   export DATABASE_PASSWORD=your_db_password
   export DATABASE_DIALECT=org.hibernate.dialect.MySQLDialect
   export JWT_SECRET=your_jwt_secret_key_here_minimum_256_bits
   export FRONTEND_URL=http://localhost:5173
   export BACKEND_URL=http://localhost:8080
   ```

3. **Create MySQL database**
   ```sql
   CREATE DATABASE urlshortener;
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The backend will be available at `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd url-shortener-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm run dev
   ```

   The frontend will be available at `http://localhost:5173`

### Database Migration (Optional)

If you want to manually run the database migration:
```bash
mysql -u your_username -p urlshortener < database_migration.sql
```

## 🧪 Testing

### Backend Testing
```bash
cd urlshortenersb
mvn test
```

### Frontend Testing
```bash
cd url-shortener-frontend
npm run test
```

### Manual Testing
1. Register a new user
2. Login and get JWT token
3. Create a short URL
4. Test the redirect functionality
5. Check analytics dashboard

## 🔧 Configuration

### Application Properties
The application uses environment variables for sensitive configuration. Key settings:

- **Database**: Configured via `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
- **JWT**: Secret key via `JWT_SECRET` (minimum 256 bits recommended)
- **CORS**: Frontend URL via `FRONTEND_URL`
- **Logging**: Set to INFO for production, DEBUG for development

### Security Features
- Password encryption using BCrypt
- JWT token expiration (configurable)
- CORS configuration for frontend integration
- Input validation and sanitization

## 📈 Performance Features
- Database indexing for optimal query performance
- Connection pooling
- Efficient URL generation algorithm
- Automatic cleanup of expired URLs

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Harshitha**
- GitHub: [@your-github-username](https://github.com/your-github-username)
- LinkedIn: [@your-linkedin-profile](https://linkedin.com/in/your-linkedin-profile)

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- React team for the amazing frontend framework
- Material-UI for beautiful components
- Chart.js for data visualization capabilities

---

⭐ If you found this project helpful, please give it a star!
