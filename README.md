# The REST API for Quote Sharing App

This project implements the following system functionality:
* Creation of new users
* Addition, viewing, modification and deletion of quotes
* Like and Dislike quotes
* Viewing votes by user and quote
* Lists of top and worse quotes
* Evolution of votes over time

Include tests for some api endpoints.

# Stack
* Java 17
* Spring Boot 3.2
* Spring Data JPA
* H2 Database
* Lombok
* Gradle
* Docker
* JUnit Jupiter
* Hamcrest
* Mockito

## To start

### From GitHub
* Download the project from GitHub 
* Build and run docker services
```
docker compose build

docker compose up
```

### From [Docker Hub](https://hub.docker.com/repository/docker/kofayoh/spring-boot-quote-app/general)
* Pull docker image
* Run docker image
```
docker pull kofayoh/spring-boot-quote-app:latest

docker run -p 8081:8081 --rm kofayoh/spring-boot-quote-app:latest 
```

### Swagger
```
http://localhost:8081/swagger
```

### After work
```
# Stop services
Ctrl + C

# Stop and delete containers
docker compose down
```

## REST API Endpoints

### User Manager
```
# A new user
POST /api/users/new
http://localhost:8081/api/users/new?name=Marina&email=marina%40gmail.com&password=marinamarina

# The user with id
GET /api/users/{id}
http://localhost:8081/api/users/1
```

### Quote Manager
```
# A new quote by the user
POST /api/quotes/user/{id}/new
http://localhost:8081/api/quotes/user/1/new?content=My%20new%20quote

# New data for the quote
PUT /api/quotes/{id}/change
http://localhost:8081/api/quotes/1/change?content=Something%20is%20changing...

# The quote's data
GET /api/quotes/{id}
http://localhost:8081/api/quotes/1

# A random quote's data
GET /api/quotes/random
http://localhost:8081/api/quotes/random

# The top quotes
GET /api/quotes/top
http://localhost:8081/api/quotes/top?page=0&size=10

# The worse quotes
GET /api/quotes/flop
http://localhost:8081/api/quotes/flop?page=0&size=10

# Deletion of the quote
DELETE /api/quotes/{id}/delete
http://localhost:8081/api/quotes/1/delete
```

### Vote Manager
```
# A new vote for the quote
POST /api/votes/new
http://localhost:8081/api/votes/new?userId=1&quoteId=1&assessment=LIKE

# The votes for the quote
GET /api/votes/for-quote/{id}
http://localhost:8081/api/votes/for-quote/1?page=0&size=5

# The votes by the user
GET /api/votes/by-user/{id}
http://localhost:8081/api/votes/by-user/1?page=0&size=5

#The evolution of the quote's votes
GET /api/votes/evolution/quote/{id}
http://localhost:8081/api/votes/evolution/quote/1
```