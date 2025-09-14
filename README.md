# ECHO - Secure Journaling Application

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.16-brightgreen)
![MongoDB](https://img.shields.io/badge/MongoDB-4.4.0+-47A248?logo=mongodb&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-0.12.5-000000?logo=jsonwebtokens)

ECHO is a secure, privacy-focused journaling application that helps users document their thoughts and emotions with end-to-end encryption and mood analysis capabilities.

## ✨ Features

- 🔐 **Secure Authentication** - JWT-based authentication with Spring Security
- 📝 **Journal Management** - Create, read, update, and delete journal entries
- 📊 **Mood Analysis with Gemini AI** - Powered by Google's Gemini AI for sentiment analysis of journal entries to track emotional patterns
- ✉️ **Email Notifications** - Weekly digests and important updates
- 🔄 **RESTful API** - Clean, well-documented API endpoints
- 🚀 **Scalable Architecture** - Built with Spring Boot for easy scaling

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 2.7.16
- **Database**: MongoDB
- **AI Integration**: Google Gemini API for mood analysis
- **Security**: JWT, Spring Security
- **Build Tool**: Maven

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- MongoDB 4.4 or higher
- SMTP server (for email functionality)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Devansh-sys/echo.git
   cd echo
   ```

2. **Configure the application**
   Create an `application.properties` file in `src/main/resources/` with the following content:
   ```properties
   # Server Configuration
   server.port=8080
   
   # MongoDB Configuration
   spring.data.mongodb.uri=mongodb://localhost:27017/echo
   
   # JWT Configuration
   jwt.secret=your_jwt_secret_key_here
   jwt.expiration=86400000
   
   # Email Configuration
   spring.mail.host=your_smtp_host
   spring.mail.port=587
   spring.mail.username=your_email@example.com
   spring.mail.password=your_email_password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   
   # Gemini AI Configuration
   gemini.api.key=your_gemini_api_key_here
   ```

3. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`

## 📚 API Documentation

### Authentication

#### Login
```http
POST /public/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "yourpassword"
}
```

#### Sign Up
```http
POST /public/sign-up
Content-Type: application/json

{
  "username": "newuser@example.com",
  "password": "securepassword"
}
```

### User Endpoints

#### Get User Profile
```http
GET /user
Authorization: Bearer <JWT_TOKEN>
```

#### Update User
```http
PUT /user
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "username": "updated@example.com",
  "password": "newpassword"
}
```

#### Delete User
```http
DELETE /user
Authorization: Bearer <JWT_TOKEN>
```

### Journal Entries

#### Get All Entries
```http
GET /journal
Authorization: Bearer <JWT_TOKEN>
```

#### Create New Entry
```http
POST /journal
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "My Journal Entry",
  "content": "Today was a great day!"
}
```

#### Get Entry by ID
```http
GET /journal/id/{entryId}
Authorization: Bearer <JWT_TOKEN>
```

#### Update Entry
```http
PUT /journal/id/{entryId}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "Updated Title",
  "content": "Updated content"
}
```

#### Delete Entry
```http
DELETE /journal/id/{entryId}
Authorization: Bearer <JWT_TOKEN>
```

### Admin Endpoints

#### Get All Users (Admin Only)
```http
GET /admin/all-users
Authorization: Bearer <JWT_ADMIN_TOKEN>
```

#### Create Admin User (Admin Only)
```http
POST /admin/create-admin-user
Authorization: Bearer <JWT_ADMIN_TOKEN>
Content-Type: application/json

{
  "username": "admin@example.com",
  "password": "adminpassword"
}
```

### Health Check
```http
GET /public/health-check
```

> **Note**: All endpoints except `/public/*` require authentication via JWT token in the Authorization header.

## 🔒 Security

- **JWT Authentication**: All endpoints except `/public/*` require a valid JWT token
- **Password Hashing**: BCrypt for secure password storage
- **Role-based Access Control**:
  - Regular users can only access their own journal entries
  - Admin users have additional privileges to manage all users
- **Input Validation**: Comprehensive validation on all endpoints
- **CORS**: Configured to prevent unauthorized cross-origin requests
- **HTTPS**: Recommended for production use

## 📦 Project Structure

```
src/main/java/net/devansh/Muse/
├── config/           # Configuration classes
├── controller/       # REST API controllers
├── entity/           # Data models
├── repository/       # Data access layer
├── security/         # Security configurations
├── service/          # Business logic
└── utils/            # Utility classes
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👏 Acknowledgments

- Built with ❤️ using Spring Boot
- Inspired by the need for private, secure journaling solutions

---

<div align="center">
  Made with ❤️ by Devansh
   
> **Note**: ECHO (Every Chronicle Has an Odyssey) - A personal journey through words
</div>
