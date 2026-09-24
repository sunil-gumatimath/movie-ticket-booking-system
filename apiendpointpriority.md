# API Endpoints by Priority

## Priority 1
**POST** `/register` - Register new user
- Authentication: None
- Description: Register a new user account

## Priority 2
**POST** `/login` - Login and get JWT token
- Authentication: None
- Description: Authenticate user and get JWT token

## Priority 3
**PUT** `/update?email={email}` - Update user profile
- Authentication: JWT Token
- Description: Update user profile information

## Priority 4
**DELETE** `/delete?email={email}` - Soft delete user
- Authentication: JWT Token
- Description: Soft delete user account

## Priority 5
**POST** `/theater/register` - Register theater for authenticated owner
- Authentication: JWT Token with ROLE_THEATER_OWNER
- Description: Register a new theater (Theater Owner only)

## Priority 6
**GET** `/theater/{id}` - Get theater details
- Authentication: JWT Token
- Description: Get theater details by ID

## Priority 7
**PUT** `/theater/{id}` - Update theater (Theater Owner)
- Authentication: JWT Token with ROLE_THEATER_OWNER
- Description: Update theater details (Theater Owner only)

## Priority 8
**POST** `/screen?theaterId={id}` - Add screen to theater
- Authentication: JWT Token with ROLE_THEATER_OWNER
- Description: Add a screen to your own theater

## Priority 9
**GET** `/screen/{screenId}` - Get screen details
- Authentication: JWT Token
- Description: Get screen details by ID

## Priority 10
**POST** `/theaters/{theaterId}/screens/{screenId}/shows` - Add show to screen (Theater Owner)
- Authentication: JWT Token with ROLE_THEATER_OWNER
- Description: Add a show to a screen (Theater Owner only)

## Priority 11
**GET** `/movies/{movieId}` - Get movie details
- Authentication: JWT Token
- Description: Get movie details by ID

## Priority 12
**POST** `/movies/{movieId}/feedback` - Create feedback (User)
- Authentication: JWT Token with ROLE_USER
- Description: Create feedback for a movie (User only)

## Priority 13
**GET** `/movies/{movieId}/feedback` - Get movie feedbacks
- Authentication: JWT Token
- Description: Get all feedbacks for a movie
