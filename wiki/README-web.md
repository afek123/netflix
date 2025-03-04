# Netflix API Documentation

## 🚀 Cloning and Running the Application

### 📥 Cloning the Repository
To get started, clone the Netflix repository by running the following command in your terminal:

```bash
git clone https://github.com/afek123/netflix.git
```

Navigate to the project directory:

```bash
cd netflix
```

---

### 🛠 Running the Application

#### Using Docker Compose
If the repository contains a `netflix-master` directory, navigate into it:

```bash
cd netflix-master
```

Run Docker Compose to build and start the application:

```bash
docker-compose up --build
```

#### What This Command Does:
- Builds the Docker images for the Netflix application.
- Starts all necessary containers.
- Displays logs in the terminal for monitoring.

Once the application is fully started, open your browser and visit:

🔗 **[http://localhost:3000/](http://localhost:3000)**

You should now see the Netflix application running locally. Browse movies, watch trailers, and explore all features!

---

## 📌 API Overview
This document provides detailed information about the API endpoints available in the Netflix application. The API is divided into several sections:

- **Movies** 🎬
- **Categories** 🏷️
- **Users** 👤
- **Recommendations** ⭐
- **Manager Routes** 🔐

---

![homePageDarkMode](https://github.com/user-attachments/assets/47fc3022-404c-4b9e-ab6a-492e10151903)
![homePageLightMode](https://github.com/user-attachments/assets/45070a87-848e-4545-889c-8216355bff52)

## 👤 Users
![succesfulSignIn](https://github.com/user-attachments/assets/ae5c82f4-38df-464c-bb9a-fe4552912e91)
![unsuccesfulSignIn](https://github.com/user-attachments/assets/d98db536-0f79-43c8-995d-61b9b6770937)

### 📝 Register a User
![unsuccesfulSignUp](https://github.com/user-attachments/assets/65a9e52b-5125-4f00-ad0b-802a9cc9dc69)
![signUpPageDarkMode](https://github.com/user-attachments/assets/493deac4-21c6-456c-98a1-31974f5be854)
![signUpPageLightMode](https://github.com/user-attachments/assets/2cce95e0-b39d-48d3-9ee8-45160195b6d9)

**Endpoint:** `POST /api/users`

**Description:** Registers a new user. Supports file uploads (e.g., profile picture).

**Request Body:**
- Username
- Password
- Profile picture

**Response:** Created user object.

---

### 🔍 Get User Details
**Endpoint:** `GET /api/users/:id`

**Description:** Retrieves details of a specific user by their ID.

**Response:** User object.

---

### 🎥 Add Movie to Watched List
**Endpoint:** `POST /api/users/add-movie-to-watched`

**Description:** Adds a movie to the user's watched list.

**Request Body:** Movie ID.

**Response:** Updated user object.

---

### 🔑 Login User
**Endpoint:** `POST /api/login`

**Description:** Authenticates a user and generates a JWT token.

**Request Body:**
- Username
- Password

**Response:** JWT token.

---

### ✅ Check User Token
**Endpoint:** `POST /api/tokens`

**Description:** Verifies if a user is registered and returns their ID.

**Request Body:** JWT token.

**Response:** User ID.

---

## ⭐ Recommendations
![recommended](https://github.com/user-attachments/assets/8ffcd73a-05ce-4f9f-bc04-ae0076c67181)

### 📜 Get Recommendations
**Endpoint:** `GET /api/:userId/:movieId/recommend`

**Description:** Retrieves recommendations for a specific user and movie.

**Response:** Array of recommendation objects.

---

### ➕ Add Recommendation
**Endpoint:** `POST /api/:userId/:movieId/recommend`

**Description:** Adds a recommendation for a specific user and movie.

**Request Body:** Recommendation details.

**Response:** Created recommendation object.

---

### 🗑 Delete Recommendation
**Endpoint:** `DELETE /api/:userId/:movieId/recommend`

**Description:** Deletes a specific recommendation.

**Response:** Success message.
## 🎬 Movies

### 📜 Get All Movies
**Endpoint:** `GET /api/movies`

**Description:** Retrieves a list of all movies.

**Response:** Array of movie objects.

---

### ➕ Create a Movie
![managerAddMovie](https://github.com/user-attachments/assets/2ea812e8-9ee2-4b0a-8688-b1e7d9bf1036)
![managerAddMovie2](https://github.com/user-attachments/assets/ac14516a-46d7-4d25-be9c-9e2f3ac9e251)
![managerAddMovie3](https://github.com/user-attachments/assets/016e369d-6ecb-440c-bba7-162adeb74586)

**Endpoint:** `POST /api/movies`

**Description:** Creates a new movie. Supports file uploads.

**Request Body:**
- Title
- Description
- File uploads (e.g., movie poster, video file)

**Response:** Created movie object.

---

### 🔍 Get a Movie by ID
![moviesPageDarkMode](https://github.com/user-attachments/assets/9e6fe02c-6fc6-4ee2-bb1a-432fa41ba18d)
![moviesPageLightMode](https://github.com/user-attachments/assets/1b5cb400-1cd4-4c4f-a3e8-8a809edead5f)

**Endpoint:** `GET /api/movies/:id`

**Description:** Retrieves a specific movie by its ID.

**Response:** Movie object.

---

### ✏️ Update a Movie
![managerUpdateMovie](https://github.com/user-attachments/assets/195e4b19-f755-41c6-b400-6b6d700977be)
![managerUpdateMovie2](https://github.com/user-attachments/assets/9607a67d-a104-4fd6-b0ef-7390b1f2ccb1)
![managerUpdateMovie3](https://github.com/user-attachments/assets/c89d7aa4-db7f-4ec7-8cd5-746fa2443983)
![managerUpdateMovie4](https://github.com/user-attachments/assets/fb08312f-4e32-4149-85a8-3f206510ce5c)

**Endpoint:** `PUT /api/movies/:id`

**Description:** Updates a movie's details. Supports file uploads.

**Request Body:** Updated movie details.

**Response:** Updated movie object.

---

### 🗑 Delete a Movie
![managerDeleteMovie](https://github.com/user-attachments/assets/e7a4f785-076b-4c6e-963e-391cf827bb6d)
![managerDeleteMovie2](https://github.com/user-attachments/assets/a0c37a91-684c-4c62-958b-6a4494de9441)
![managerDeleteMovie3](https://github.com/user-attachments/assets/cd8603d5-8ffe-4575-ab34-f6cd2a739d55)

**Endpoint:** `DELETE /api/movies/:id`

**Description:** Deletes a specific movie by its ID.

**Response:** Success message.

---

### 🔎 Search Movies
![searchDarkMode](https://github.com/user-attachments/assets/25f424a6-8728-429f-8534-f153615ec6c5)
![searchLightMode](https://github.com/user-attachments/assets/c369ee44-38e7-4081-8044-a1e0764d9677)

**Endpoint:** `GET /api/movies/search/:query`

**Description:** Searches for movies by title.

**Response:** Array of matching movie objects.

---

## 🏷️ Categories

### 📜 Get All Categories
**Endpoint:** `GET /api/categories`

**Description:** Retrieves a list of all categories.

**Response:** Array of category objects.

---

### ➕ Create a Category
![managerAddCategory](https://github.com/user-attachments/assets/c561da39-46e1-4d34-81bb-e2422933f29e)

**Endpoint:** `POST /api/categories`

**Description:** Creates a new category.

**Request Body:**
- Name
- Description

**Response:** Created category object.

---

### 🔍 Get a Category by ID
**Endpoint:** `GET /api/categories/:id`

**Description:** Retrieves a specific category by its ID.

**Response:** Category object.

---

### ✏️ Update a Category
![managerUpdateCategory](https://github.com/user-attachments/assets/76cecdcc-6c41-43b8-86af-bd0c13eb9749)
![managerUpdate2Category](https://github.com/user-attachments/assets/9f18a918-03e1-46a3-945a-caa7ed6f4bb3)
![managerUpdate3Category](https://github.com/user-attachments/assets/87244ad0-b37c-4497-b237-dbfbd20dd4ea)

**Endpoint:** `PATCH /api/categories/:id`

**Description:** Updates a category's details.

**Request Body:** Updated category details.

**Response:** Updated category object.

---

### 🗑 Delete a Category
![managerDeleteCategory](https://github.com/user-attachments/assets/9b2780cb-cc8a-473a-970b-502615a33e1b)
![managerDeleteCategory2](https://github.com/user-attachments/assets/8789c012-8c07-44aa-91f1-07163e447f6f)
![managerDeleteCategory3](https://github.com/user-attachments/assets/9770234d-eb89-491e-9f99-ef03bef9f3cb)

**Endpoint:** `DELETE /api/categories/:id`

**Description:** Deletes a specific category by its ID.

**Response:** Success message.

---

---

## 🔐 Manager Routes
![manager](https://github.com/user-attachments/assets/0141604d-82b9-4f23-a271-5fc0a9bd4e65)
![managerProfile](https://github.com/user-attachments/assets/da7800c3-f171-41f7-8f3c-9e7c623f11ae)
![accessDenied](https://github.com/user-attachments/assets/29873aa2-de28-4bda-b1cf-9754653d50be)

### 🚀 Restricted Routes for Managers
**Endpoint:** `/api/manager`

**Description:** Routes restricted to users with the **"manager"** role.

**Middleware:** `checkRole('manager')` is applied to these routes to ensure only authorized users can access them.

---

## 🎯 Conclusion
This API allows users to browse and manage movies, categorize them, handle user accounts, and get recommendations based on viewing history. Managers have exclusive access to admin routes. 🎥🍿🚀 write it normally without ## and so on
