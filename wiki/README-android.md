### Netflix-like Android App Documentation

---

## 🚀 Cloning and Running the Application

### 📥 Cloning the Repository
To get started, clone the repository by running the following command in your terminal:

```bash
git clone https://github.com/your-repository/android-netflix-app.git
```

Navigate to the project directory:

```bash
cd android-netflix-app
```

---

### 🛠 Running the Application

#### Using Docker Compose
If the repository contains a `docker-compose.yml` file, you can easily run the application using Docker Compose. Navigate to the project directory (if not already there) and run:

```bash
docker-compose up --build
```

#### What This Command Does:
- **Builds the Docker images** for the Android app backend and any required services (e.g., database, API server).
- **Starts all necessary containers** to run the application.
- **Displays logs** in the terminal for monitoring the startup process.

Once the application is fully started, you can access it through your Android emulator or physical device by connecting to the appropriate IP/port.

---

## 📱 App Overview
This Android app is designed to mimic the functionality of Netflix, allowing users to browse movies, watch trailers, and manage their profiles. The app is divided into several key sections:

- **User Profiles** 👤
- **Movies** 🎬
- **Categories** 🏷️
- **Recommendations** ⭐
- **Manager Features** 🔐 (for admin users)

---
## 👤 User Profiles

### Sign In / Sign Up
- **SignIn Page Dark Mode** (`SignIn_page_dark_mode`) - The login screen in dark mode.
- ![SignIn_page_dark_mode](https://github.com/user-attachments/assets/b4685584-bd75-4c29-b2a4-d7b66286bd24)

- **SignIn Page Light Mode** (`SignIn_page_lights_mode`) - The login screen in light mode.
- ![SignIn_page_lights_mode](https://github.com/user-attachments/assets/651e8d05-795c-4277-96be-3ad655aa723e)

- **SignIn Page Unsuccessful** (`SignIn_pageUnSuccessful`) - Displays an error message when login fails.
- ![SignIn_pageUnSuccessful](https://github.com/user-attachments/assets/a6a95592-63a7-40e0-92a4-6edc29ef03a4)

- **SignUp Page Dark Mode** (`SignUp_page_dark_mode`) - The registration screen in dark mode.
- ![SignUp_page_dark_mode](https://github.com/user-attachments/assets/fb65e7ae-56e4-4548-8a19-85d786e0a782)

- **SignUp Page Light Mode** (`SignUp_page_light_mode`) - The registration screen in light mode.
- ![SignUp_page_light_mode](https://github.com/user-attachments/assets/bab46b88-a3ac-4ddf-a287-615c5051491d)

- **SignUp Page Unsuccessful** (`SignUp_pageUnSuccessful`) - Displays an error message when registration fails.
![SignUp_pageUnSuccessful](https://github.com/user-attachments/assets/0f6c0c50-2d72-4f7d-950d-870a60f7bc15)
- **Home Page Dark Mode** (`Home_page_darks_mode`) - This is the main screen of the app in dark mode, displaying featured movies and categories.
- ![Home_page_darks_mode](https://github.com/user-attachments/assets/791783ca-8514-4c3b-859e-42ba2c331bda)

- **Home Page Light Mode** (`Home_page_light_mode`) - The same screen but in light mode for user preference.
- ![Home_page_light_mode](https://github.com/user-attachments/assets/f4defddb-4346-4660-802f-8ce2b99bdede)

- **Netflix Page Dark Mode** (`Netflix_page_dark_mode`) - A dark-themed Netflix-like homepage.
- ![Netflix_page_dark_mode](https://github.com/user-attachments/assets/ca4020dd-1fa1-4269-892a-cd913ea9ac36)

- **Netflix Page Light Mode** (`Netflix_page_light_mode`) - A light-themed Netflix-like homepage.
![Netflix_page_light_mode](https://github.com/user-attachments/assets/dd307f1d-8d2d-45e9-88bb-c31dd1042858)

### User Management
- **Manager User** (`managerUser`) - Screen for managing user accounts (admin-only feature).
![managerUser](https://github.com/user-attachments/assets/14eb4366-33aa-43c7-b43f-bd62f25082de)


### Movie Recommendations
- **Shows Movie** (`ShowsMovie`) - Displays the movie
- ![ShowsMovie](https://github.com/user-attachments/assets/45e29f41-4b5a-49a6-b873-5b09ffc03b7e)


---
## 🎬 Movies

### Browse Movies

### Movie Details
- **MovieDetailsWithoutRecommend** - Displays detailed information about a movie without recommendations.
- ![MovieDetailsWithoutRecommend](https://github.com/user-attachments/assets/890ca73a-8ab6-482d-b6cd-d91ab98b736f)

- **MovieDetailsWithRecommend** - Displays detailed information about a movie along with recommended similar movies.
![MovieDetailsWithRecommend](https://github.com/user-attachments/assets/e74880c6-4df1-42ae-97f2-89c9a4c10a95)

### Search Movies
- **Search Page Dark Mode** (`search_page_dark_mode`) - The search screen in dark mode, allowing users to search for movies by title.
- ![search_page_dark_mode](https://github.com/user-attachments/assets/f47cb7d9-1938-49e6-9d73-a737893714cb)

- **Search Page Light Mode** (`search_page_light_mode`) - The search screen in light mode.
- ![search_page_light_mode](https://github.com/user-attachments/assets/f3cc8467-53b4-4ae8-84a0-1113e6b4b8af)

- **Search Unsuccessful Null Search** (`search_unsuccessful_null_search`) - Displays a message when no results are found for a search query.
![search_unsuccesful_null_search](https://github.com/user-attachments/assets/025698e7-ff9a-4356-b533-6d5c9d5f5760)

---

## 🏷️ Categories

### Manage Categories
- **Controller Categories Dark** (`controllerCategoriesDark`) - The category management screen in dark mode fo![controllerCategoriesDark](https://github.com/user-attachments/assets/a81b9fb1-c9af-4179-ab5e-dfffc11a5170)
r admins.
- 
- **Controller Categories Light** (`controllerCategoriesLight`) - The same screen in light mode.
- ![controllerCategoriesLight](https://github.com/user-attachments/assets/5ec95b85-ba0a-45b8-ae1f-b0375f64c429)

- **Add Comedies Category** (`AddComediesCategory`) - Screen for adding a new category (e.g., Comedies).
- ![AddComediesCategory](https://github.com/user-attachments/assets/f5d9f074-11e5-4cc1-b902-5dfeb7877cbd)

- **After Add Comedies Category** (`AfterAddComediesCategory`) - Displays the updated list of categories after adding a new one.
- ![AfterAddComediesCategory](https://github.com/user-attachments/assets/e5b6c7a6-7968-4b6e-b15f-85abaf9c2827)

- **Update Adventure Category** (`updateAdventureCategory`) - Screen for updating an existing category (e.g., Adventure).
- ![updateAdvantureCategory](https://github.com/user-attachments/assets/a84b5d90-d2ab-4534-84f2-7e906a9f4490)

- **After Update Adventure Category** (`AfterUpdateAdventureCategory`) - Displays the updated list of categories after modifications.
- ![AfterUpdateAdvantureCategory](https://github.com/user-attachments/assets/46f8927e-5f1a-4d5d-8cc6-f9fabd59b721)

- **Delete Comedy Category** (`deleteComedyCategory`) - Confirmation screen for deleting a category.
- ![deleteComedyCategory](https://github.com/user-attachments/assets/fdc2250a-508f-4032-b6d7-0b062ba7cab3)

- **Delete Movie** (`deleteMovie`) - Confirmation screen for deleting a movie.
![deleteMovie](https://github.com/user-attachments/assets/919b385d-8b76-4f02-9e7e-e7ba4d053dbb)

---



## 🔐 Manager Features

### Admin Dashboard
- **Manager Page Dark** (`manager_page_dark`) - The admin dashboard in dark mode, allowing managers to add, update, or delete movies and categories.
- ![manager_page_dark](https://github.com/user-attachments/assets/2e51da29-19f3-4e43-8c3b-7e726b33782f)

- **Manager Page Light** (`manager_page_light`) - The same dashboard in light mode.
![manager_page_light](https://github.com/user-attachments/assets/dc6dce6f-d2fe-4e6d-aa97-eea3e351bbae)

### Add/Update Movies
- **Add Movie** (`addMovie`) - Screen for adding a new movie to the platform.
- ![addMovie](https://github.com/user-attachments/assets/6228a05a-cb44-4876-840f-e1ccde2b3ad1)

- **Update Movie** (`updateMovie`) - Screen for updating an existing movie's details.
- ![updateMovie](https://github.com/user-attachments/assets/46f99cac-8f6a-4323-bdd1-b5aa8b022968)

- **After Update and Add Movies** (`AfterUpdateAndAddMovies`) - Displays the updated list of movies after adding or modifying.
![AfterUpdateAndAddMovies](https://github.com/user-attachments/assets/b1313de2-2f7a-4f84-b229-93236898d437)

