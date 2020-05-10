# Test task Commerzbank
## Backend REST application with Spring security and JWT exposing API for manipulating entities stored in db
Application uses HATEOAS project to expose richer REST APIs

Domain model: book shop.  
Application handles accounting of books, book availability and order processing.

Authentication steps:
1. Get the JWT based token from the authentication endpoint `/auth/signin`.
2. Extract token from the authentication result.
3. Set the HTTP header `Authorization` value as `Bearer jwt_token`.
4. Then send a request to access the protected resources. 
5. If the requested resource is protected, Spring Security will use custom `Filter` to validate the JWT token, and build an `Authentication` object and set it in Spring Security specific `SecurityContextHolder` to complete the authentication progress.
6. If the JWT token is valid it will return the requested resource to client.

Preloaded user credentials:  
Username: user  
Password: password

## Running locally

### With maven command line
```
./mvnw spring-boot:run
```

## Entities

Application contains preloaded data for following entities:

Name|description
---|---
Book| book entity with name, author, description and price attributes and out of stock flag.
BookAvailability| entity representing availability of book, contains count of available items and book reference.
Order| contains status, ordered items, description and sum total.
OrderItem| contains book reference, count of ordered books and reference to order.

## Endpoints

Book availability

URI|request|response|description
---|---|---|---
/bookAvailability|GET|200|Get all book availabilities.
/bookAvailability|POST {"count": 22, "book": { "id":"9cd32bd0-5153-479c-84a4-ac7824e9dc38"}}|201, 404 if book not found, 400 if such book availability already created or if validation error |Create a new book availability.
/bookAvailability/{id}|GET| 200, 404 |Get a book availability by id.
/bookAvailability/{id}|PUT {"count": 22, "book": { "id":"9cd32bd0-5153-479c-84a4-ac7824e9dc38"}}|201, 404 if book or availability not found, 400 if such book availability already created or if validation error |Update book availability by id.
/bookAvailability/{id}|DELETE|204, 404 if availability not found |Delete book availability by id.

Book

URI|request|response|description
---|---|---|---
/book|GET|200|Get all books.
/book|POST {"name": "name", "author": "author","description": "description","price": 10}|201, 400 if validation error |Create a new book.
/book/{id}|GET| 200, 404 |Get a book by id.
/book/{id}|PUT {"name": "name", "author": "author","description": "description","price": 10}|201, 404 if book not found, 400 if validation error |Update book by id.
/book/{id}|DELETE|204, 404 |Delete book by id.

Order

URI|request|response|description
---|---|---|---
/order|GET|200|Get all orders.
/order|POST {"items": [{"book": {"id": "b9ec54d6-69b1-4337-9afa-6c30c722f7c3"},"count": 4}],"priceSum": 10,"description": "description"}|201, 400 if validation error or if book unavailable, 404 if book or book availability not found |Create a new order.
/order/{id}|GET| 200, 404 |Get a order by id.
/order/{id}|PUT {"items": [{"book": {"id": "b9ec54d6-69b1-4337-9afa-6c30c722f7c3"},"count": 4}],"priceSum": 10,"description": "description"}|201, 400 if validation error or if book unavailable, 404 if order, book or book availability not found, 405 if order update not allowed in current status of order |Update order by id.
/order/{id}|DELETE|204, 404 |Delete order by id.
/order/{id}/cancel|POST|200, 404, 405 if order status change not allowed |Set cancelled status to order by id.
/order/{id}/complete|POST|200, 404, 405 if order status change not allowed |Set completed status to order by id.
/order/{id}/items|POST [{"book": {"id": "d8189234-d0d9-4d4a-b086-e22309bb80ab"},"count": 7},{"book": {"id": "43a24631-fd7c-4e82-b114-20b2090bdcac"},"count": 7}]|201, 400 if validation error or if book unavailable, 404 if order, book or book availability not found, 405 if order items update not allowed in current status of order |Replaces items in order by id.