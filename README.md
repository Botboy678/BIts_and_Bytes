# BIts_and_Bytes
A social portfolio platform API where developers can showcase projects, connect with friends, write developer blogs, and manage profiles — secured with Spring Security, HTTP Basic auth, Google OAuth2 login, and role-based access control (RBAC). Built using SDLC principles

🔗 **Live app:** [Coming soon], but also you could just clone it lol

---

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3.5.4
- Spring Security 6 (HTTP Basic auth)
- Spring Security OAuth2 Client (Google Login)
- Spring Data JPA / Hibernate
- MySQL (via `mysql-connector-j`)
- Maven build system (with Maven Wrapper)
- Lombok for boilerplate reduction
- AWS SDK v2 for S3 cloud storage
- springdoc-openapi (Swagger UI)
- Spring Boot Actuator
- JUnit 5 & Mockito for unit testing
- Java Faker for test data generation
- SLF4J for logging

**Frontend**
- Not yet built — this repo is currently backend-only.

---

## Project Structure

```
BIts_and_Bytes/
├── .gitignore
└── backend/
    ├── .gitattributes
    ├── .gitignore
    ├── .mvn/
    │   └── wrapper/
    │       └── maven-wrapper.properties
    ├── README.md
    ├── mvnw / mvnw.cmd              # Maven wrapper
    ├── pom.xml                      # Maven dependencies
    └── src/
        ├── main/java/com/bits/bytes/bits/bytes/
        │   ├── Configs/
        │   │   ├── AppConfigs.java
        │   │   ├── SecurityConfigs.java     # HTTP Basic + OAuth2 login setup
        │   │   └── S3/
        │   │       ├── S3Buckets.java
        │   │       └── S3Config.java
        │   ├── Controllers/
        │   │   ├── BugReportsController.java
        │   │   ├── DeveloperController.java
        │   │   ├── FriendController.java
        │   │   ├── LoginController.java
        │   │   ├── ProfileController.java
        │   │   ├── ProjectController.java
        │   │   └── UserController.java
        │   ├── DTOs/
        │   │   ├── BugReportsDTO.java
        │   │   ├── DeveloperBlogCommentsDTO.java
        │   │   ├── DeveloperBlogDTO.java
        │   │   ├── FriendsDTO.java
        │   │   ├── ProfilesDTO.java
        │   │   ├── ProjectCommentsDTO.java
        │   │   ├── ProjectsDTO.java
        │   │   └── UsersDTO.java
        │   ├── MapDTOs/
        │   │   └── MapDTOs.java
        │   ├── Models/
        │   │   ├── BugReports.java
        │   │   ├── DeveloperBlog.java
        │   │   ├── DeveloperBlogComments.java
        │   │   ├── Friends.java
        │   │   ├── Permissions.java              # RBAC permission enum
        │   │   ├── Profiles.java
        │   │   ├── ProjectComments.java
        │   │   ├── Projects.java
        │   │   ├── Role.java                      # DEVELOPER / USER roles
        │   │   ├── Users.java
        │   │   └── MiscellaneousModels/
        │   │       ├── CustomOAuth2UserService.java
        │   │       ├── FriendsId.java
        │   │       ├── LeetCodeProfile.java
        │   │       ├── MyCurrentUser.java
        │   │       ├── MyUserDetailsService.java
        │   │       └── UserPrincipal.java
        │   ├── Repo/
        │   │   ├── BugReportsRepo.java
        │   │   ├── DeveloperBlogCommentsRepo.java
        │   │   ├── DeveloperBlogRepo.java
        │   │   ├── FriendsRepo.java
        │   │   ├── ProfilesRepo.java
        │   │   ├── ProjectCommentsRepo.java
        │   │   ├── ProjectRepo.java
        │   │   └── UserRepo.java
        │   ├── Services/
        │   │   ├── Impl/
        │   │   │   └── UserServicesImpl.java
        │   │   ├── S3Service.java
        │   │   └── UserServices.java
        │   ├── Factories.java
        │   └── MainApplication.java
        └── test/java/com/bits/bytes/bits/bytes/
            ├── MainApplicationTests.java
            └── Service/
                └── UserServicesImplTests.java
```

---

## Installation

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+ (or use the included `mvnw` wrapper)
- AWS Account (for S3 profile photo uploads)
- Google Cloud Console credentials (for OAuth2 login)

### Backend Setup

1. **Navigate to the backend directory**
```bash
cd backend
```

2. **Create the database**
```bash
mysql -u root -p -e "CREATE DATABASE bits_and_bytes;"
```

3. **Configure application properties**

Create `src/main/resources/application.properties` (not committed to the repo) with your own values:
```properties
# Application
spring.application.name=bits-bytes
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/bits_and_bytes
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile

# AWS S3 (for profile photos)
aws.s3.bucket=your-bucket-name
aws.region=us-east-1
aws.accessKeyId=YOUR_AWS_ACCESS_KEY
aws.secretAccessKey=YOUR_AWS_SECRET_KEY
```

4. **Build and run**
```bash
./mvnw clean spring-boot:run
```

The API will be available at `http://localhost:8080`, with Swagger UI (springdoc-openapi) available at `http://localhost:8080/swagger-ui.html`.

---

## Key Features

- **Authentication:** HTTP Basic auth alongside Google OAuth2 social login
- **Authorization:** Role-Based Access Control (`DEVELOPER` / `USER` roles) via a custom `Permissions` enum, enforced with `@EnableMethodSecurity`
- **Social Features:** Friend requests (send, accept, decline)
- **Developer Blog:** Create/delete blog posts and blog comments (developer-permission gated)
- **Portfolio Management:** Users can create, update, and delete projects, and comment on projects
- **Bug Reports:** Submit and list bug reports
- **Cloud Storage:** AWS S3 integration for profile photo uploads
- **API Docs:** Swagger UI via springdoc-openapi
- **Monitoring:** Spring Boot Actuator
- **Unit Testing:** JUnit 5 and Mockito, with Java Faker for generating test data
- **Clean Architecture:** DTOs, MapDTOs, service interfaces + implementations, and repository layer

---

## API Endpoints

Endpoints as actually defined in the controllers:

| Method | Endpoint | Description |
|--------|----------|--------------|
| PUT | `/register` | Register a new user |
| GET | `/api/v1/user/profile` | Get the authenticated user's profile |
| GET | `/api/v1/user/projects` | Get the authenticated user's projects |
| POST | `/api/v1/profile/update` | Update profile (multipart form, for photo upload) |
| DELETE | `/api/v1/profile/delete/{username}` | Delete a profile |
| PUT | `/api/v1/project/add` | Create a project |
| POST | `/api/v1/project/update/{title}` | Update a project |
| DELETE | `/api/v1/project/delete/{title}` | Delete a project |
| PUT | `/api/v1/project/comment/{title}/{projectOwner}` | Comment on a project |
| PUT | `/api/v1/friend/add` | Send a friend request |
| PUT | `/api/v1/friend/accept` | Accept a friend request |
| PUT | `/api/v1/friend/decline` | Decline a friend request |
| GET | `/api/v1/friend/allSentFriendRequest` | List sent friend requests |
| PUT | `/api/v1/DeveloperBlog/add` | Write a developer blog post |
| DELETE | `/api/v1/DeveloperBlog/delete/{title}` | Delete a blog post |
| PUT | `/api/v1/DeveloperBlog/comment/{title}/{projectOwner}` | Comment on a blog post |
| DELETE | `/api/v1/DeveloperBlog/comment/{title}/{projectOwnerName}` | Delete a blog comment |
| GET | `/api/v1/DeveloperBlog/getAllUsersDevBlogs` | List all developer blog posts |
| PUT | `/api/v1/Bugreport/add` | Submit a bug report |
| GET | `/api/v1/Bugreport/allBugReports` | List all bug reports |

---

## Testing

```bash
cd backend
./mvnw test
```

Current coverage includes `MainApplicationTests` (context load) and `UserServicesImplTests` (service-layer tests using Mockito and Java Faker for mock data).

---

## Commit History Highlights

- **Aug 15, 2025:** Initial domain modeling (Users, Profiles, Projects, Blogs)
- **Aug 16, 2025:** Added Blog entities and Profile functionality
- **Aug 17–18, 2025:** CRUD operations for profiles and projects
- **Aug 19, 2025:** Project comments implemented
- **Aug 24, 2025:** Refactored UserServices with helper functions
- **Aug 28, 2025:** Split controllers, added DTOs for all domain models
- **Aug 29, 2025:** Created UserService interface and implementation
- **Sep 1, 2025:** Added Roles & Permissions (RBAC)
- **Sep 1, 2025:** Added Google OAuth2 login
- **Sep 3, 2025:** Friend request logic (send, accept, decline)
- **Sep 4, 2025:** Blog permissions enforced (developer role only)
- **Sep 10–11, 2025:** Completed DTO mappers for all controllers
- **Sep 12, 2025:** Added SLF4J logging to services
- **Sep 16, 2025:** First unit tests written
- **Sep 19–20, 2025:** Expanded test coverage for user services
- **Oct 1, 2025:** Reorganized into monorepo structure (`backend/` folder)
- **Oct 2, 2025:** Mirrored interfaces to backend DTOs
- **Oct 3, 2025:** Switched to constructor injection
- **Oct 13, 2025:** Integrated AWS S3 for profile photos
- **Oct 15, 2025:** Added API versioning (`/api/v1`)

---

## Future Improvements

- Add integration tests (`@SpringBootTest` with Testcontainers)
- Add a `docker-compose.yml` to spin up DB + app together
- Build out the React frontend
- Add Redis caching for frequently accessed profiles
- Implement real-time notifications for friend requests

---

## License

This is a personal project. Feel free to use it as a base for your own portfolio platform! Suggestions and pull requests are always welcome.
