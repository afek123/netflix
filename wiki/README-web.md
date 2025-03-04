Netflix API Documentation

Cloning and Running the Application

Cloning the Repository
To get started, clone the Netflix repository by running the following command in your terminal:

```bash
git clone https://github.com/afek123/netflix.git
```

Navigate to the project directory:

```bash
cd netflix
```

Running the Application

Using Docker Compose
If the repository contains a netflix-master directory, navigate into it:

```bash
cd netflix-master
```

Run Docker Compose to build and start the application:

```bash
docker-compose up --build
```

What This Command Does:
- Builds the Docker images for the Netflix application.
- Starts all necessary containers.
- Displays logs in the terminal for monitoring.

Once the application is fully started, open your browser and visit:

🔗 [http://localhost:3000/](http://localhost:3000)

You should now see the Netflix application running locally. Browse movies, watch trailers, and explore all features!

API Overview
This document provides detailed information about the API endpoints available in the Netflix application. The API is divided into several sections:

- Movies 🎬
- Categories 🏷️
- Users 👤
- Recommendations ⭐
- Manager Routes 🔐
  - **Image: home Page Dark Mode**
- ![homePageDarkMode](https://github.com/user-attachments/assets/af88fc5f-0cea-4b8f-ac3e-702daf9bcfb2)
 - **Image: home Page Light Mode**

![homePageLightMode](https://github.com/user-attachments/assets/8a492947-37d2-4b6a-a1b3-17f4ddd9850a)

Users

Sign In
- **Image: Successful Sign-In** - This image shows a successful sign-in screen on the platform.
  ![succesfulSignIn](https://github.com/user-attachments/assets/e013c2b5-804d-4b05-a8cc-f4fa9a4e7e6a)

- **Image: Unsuccessful Sign-In** - This image shows an unsuccessful sign-in attempt, where the user is denied access.
![unsuccesfulSignIn](https://github.com/user-attachments/assets/2f2fc308-fa41-47b6-94b2-b5865ecaaa03)

Register a User
- **Image: Unsuccessful Sign-Up** - This image shows an unsuccessful sign-up attempt where the user is denied.
- ![unsuccesfulSignUp](https://github.com/user-attachments/assets/fdf99eda-05a2-44b4-8590-dbc74fef510a)

- **Image: Sign-Up Page Dark Mode** - This image displays the dark mode version of the sign-up page for the Netfli![signUpPageDarkMode](https://github.com/user-attachments/assets/25e33677-b5a2-44b8-980d-34bfb7597d4c)
x platform.
- 
- **Image: Sign-Up Page Light Mode** - This image shows the light mode version of the sign-up page for users registering for an account.
![signUpPageLightMode](https://github.com/user-attachments/assets/44cbdf91-48e9-491d-8613-c89c0ecb992f)

**Endpoint:** POST /api/users

**Description:** Registers a new user. Supports file uploads (e.g., profile picture).

**Request Body:**
- Username
- Password
- Profile picture

**Response:** Created user object.

Get User Details
**Endpoint:** GET /api/users/:id

**Description:** Retrieves details of a specific user by their ID.

**Response:** User object.

Add Movie to Watched List
**Endpoint:** POST /api/users/add-movie-to-watched

**Description:** Adds a movie to the user's watched list.

**Request Body:** Movie ID.

**Response:** Updated user object.

Login User
**Endpoint:** POST /api/login

**Description:** Authenticates a user and generates a JWT token.

**Request Body:**
- Username
- Password

**Response:** JWT token.

Check User Token
**Endpoint:** POST /api/tokens

**Description:** Verifies if a user is registered and returns their ID.

**Request Body:** JWT token.

**Response:** User ID.

Recommendations

Get Recommendations
- **Image: Recommended** - This image shows a page of recommendations based on the user's movie interactions.
![recommended](https://github.com/user-attachments/assets/1c7fd48e-0c46-49ac-994b-f2c881e4395c)

**Endpoint:** GET /api/:userId/:movieId/recommend

**Description:** Retrieves recommendations for a specific user and movie.

**Response:** Array of recommendation objects.

Add Recommendation
**Endpoint:** POST /api/:userId/:movieId/recommend

**Description:** Adds a recommendation for a specific user and movie.

**Request Body:** Recommendation details.

**Response:** Created recommendation object.

Delete Recommendation
**Endpoint:** DELETE /api/:userId/:movieId/recommend

**Description:** Deletes a specific recommendation.

**Response:** Success message.

Movies

Get All Movies
**Endpoint:** GET /api/movies

**Description:** Retrieves a list of all movies.

**Response:** Array of movie objects.

Create a Movie
- **Image: Manager Add Movie** - This image shows the manager interface for adding a movie to the platform.
- ![managerAddMovie](https://github.com/user-attachments/assets/5540e02c-2dbc-4708-ae4d-4ac737407814)

- **Image: Manager Add Movie 2** - This image shows another screen in the manager interface for movie addition.
- ![managerAddMovie2](https://github.com/user-attachments/assets/b9f5e4cd-b1aa-4a87-ab93-79b5248e269e)

- **Image: Manager Add Movie 3** - This image displays another movie addition screen where further details are configured.
![managerAddMovie3](https://github.com/user-attachments/assets/26bb5213-2c1f-48ea-b884-2c745f846d9a)

**Endpoint:** POST /api/movies

**Description:** Creates a new movie. Supports file uploads.

**Request Body:**
- Title
- Description
- File uploads (e.g., movie poster, video file)

**Response:** Created movie object.

Get a Movie by ID
- **Image: Movies Page Dark Mode** - This image shows the movie detail page in dark mode.
- ![moviesPageDarkMode](https://github.com/user-attachments/assets/d913952d-a839-4d7a-9b4c-e6c58446781d)

- **Image: Movies Page Light Mode** - This image shows the movie detail page in light mode.
![moviesPageLightMode](https://github.com/user-attachments/assets/c74bbe1e-5ec5-445c-b116-ec4e65f9fe93)

**Endpoint:** GET /api/movies/:id

**Description:** Retrieves a specific movie by its ID.

**Response:** Movie object.

Update a Movie
- **Image: Manager Update Movie** - This image shows the manager  for updating movie details.
- ![managerUpdateMovie](https://github.com/user-attachments/assets/89645ad6-f44c-41b8-b10e-95fec7d8e10a)

- **Image: Manager Update Movie 2** - This image shows another screen for updating movie information.
- ![managerUpdateMovie2](https://github.com/user-attachments/assets/6dcb44cf-f34d-4fc0-85a0-badadcbed267)

- **Image: Manager Update Movie 3** - This image shows further options to edit movie details in the manager .
- ![managerUpdateMovie3](https://github.com/user-attachments/assets/0309f2d3-3e4b-442d-907d-8a1f4c9a6ec6)

- **Image: Manager Update Movie 4** - This image shows another screen for updating movie details.
![managerUpdateMovie4](https://github.com/user-attachments/assets/2cde9597-d01e-413e-9269-55c726f7bda1)

**Endpoint:** PUT /api/movies/:id

**Description:** Updates a movie's details. Supports file uploads.

**Request Body:** Updated movie details.

**Response:** Updated movie object.

Delete a Movie
- **Image: Manager Delete Movie** - This image shows the manager interface for deleting a movie.
- ![managerDeleteMovie](https://github.com/user-attachments/assets/960d7c99-f435-4ab1-94c0-b2729f179a5c)

- **Image: Manager Delete Movie 2** - This image shows another confirmation screen for deleting a movie.
- ![managerDeleteMovie2](https://github.com/user-attachments/assets/b80e03ff-089e-4208-b9ff-f95b705ec204)

- **Image: Manager Delete Movie 3** - This image shows the final confirmation step to delete a movie from the platform.
![managerDeleteMovie3](https://github.com/user-attachments/assets/94b918ab-4c54-4684-8645-d452f8a80a47)

**Endpoint:** DELETE /api/movies/:id

**Description:** Deletes a specific movie by its ID.

**Response:** Success message.

Search Movies
- **Image: Search Dark Mode** - This image shows the dark mode version of the movie search page.
- ![searchDarkMode](https://github.com/user-attachments/assets/d078ee4b-7a35-4c11-9e77-f502bdf6b491)

- **Image: Search Light Mode** - This image shows the light mode version of the movie search page.
![searchLightMode](https://github.com/user-attachments/assets/1f883346-37ad-44a1-9c9b-79a830e32958)

**Endpoint:** GET /api/movies/search/:query

**Description:** Searches for movies by title.

**Response:** Array of matching movie objects.

Categories

Get All Categories
**Endpoint:** GET /api/categories

**Description:** Retrieves a list of all categories.

**Response:** Array of category objects.

Create a Category
- **Image: Manager Add Category** - This image shows the manager interface for adding a category to the platform.
![managerAddCategory](https://github.com/user-attachments/assets/fa4ba3dc-e9bf-455e-aac3-87a1a952efcd)

**Endpoint:** POST /api/categories

**Description:** Creates a new category.

**Request Body:**
- Name
- Description

**Response:** Created category object.

Get a Category by ID
**Endpoint:** GET /api/categories/:id

**Description:** Retrieves a specific category by its ID.

**Response:** Category object.

Update a Category
- **Image: Manager Update Category** - This image shows the manager interface for updating category details.
- ![managerUpdateCategory](https://github.com/user-attachments/assets/cb2efb25-a240-4b91-a36a-6dba789d19f1)

- **Image: Manager Update Category 2** - This image shows the second screen in the manager interface to update category details.
- ![managerUpdate2Category](https://github.com/user-attachments/assets/663bd75e-df7a-479d-9bce-e97472a42851)

- **Image: Manager Update Category 3** - This image shows the final screen for updating category details in the manager![managerUpdate3Category](https://github.com/user-attachments/assets/619dcde4-4d4f-4399-b8d3-5df5f8f9539e)
 interface.

**Endpoint:** PATCH /api/categories/:id

**Description:** Updates a category's details.

**Request Body:** Updated category details.

**Response:** Updated category object.

Delete a Category
- **Image: Manager Delete Category** - This image shows the manager interface for deleting a category.
- ![managerDeleteCategory](https://github.com/user-attachments/assets/fe0eb4a6-2e77-4738-baad-f8cd5da04230)

- **Image: Manager Delete Category 2** - This image shows another confirmation screen for deleting a category.
- ![managerDeleteCategory2](https://github.com/user-attachments/assets/affeb3fc-7d16-41ae-a754-8cbf683edcc0)

- **Image: Manager Delete Category 3** - This image shows the final confirmation step to delete a category from the platform.
![managerDeleteCategory3](https://github.com/user-attachments/assets/f445f697-a9ae-45fe-afaf-9ab7e23ff92b)

**Endpoint:** DELETE /api/categories/:id

**Description:** Deletes a specific category by its ID.

**Response:** Success message.

Manager Routes

Restricted Routes for Managers
- **Image: Manager** - This image shows the manager interface.
- ![manager](https://github.com/user-attachments/assets/4c2872ba-1f6a-4182-8ddc-f13dc9feec4c)

- **Image: Manager Profile** - This image shows the manager profile page.
- ![managerProfile](https://github.com/user-attachments/assets/4ec0ffa2-bdee-40f9-894e-bdd4c1525f0c)

- **Image: Access Denied** - This image shows an access denied screen for unauthorized users.
![accessDenied](https://github.com/user-attachments/assets/53e1a5fd-b160-4b2e-9e1e-b4849a202cc3)

**Endpoint:** /api/manager

**Description:** Routes restricted to users with the **"manager"** role.

**Middleware:** checkRole('manager') is applied to these routes to ensure only authorized users can access them.

