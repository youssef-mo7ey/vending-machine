This backend application satisfy all the requirements with jwt authentication

1- you can register user with fields from "/api/auth/register" Not Protected
The required variables in the body are:
(username,password,role),
role is enum value so it's (USER or SELLER)
2-you can login user with fields from "/api/auth/login" Not Protected
The required variables in the body are:
(username,password),
The Response value is jwt Token
3-you can get a list of all product from "/api/product/
Not Protected
4-you can post product from "/api/product"
Protected
but you must login with role SELLER account then you put the token in Authorization Header and the entity of product require in the body (name,price,amount)
5-you can update product from "/api/product/{id}"
Protected
but you must login with role SELLER account and the post with this id is posted by the same seller then you put the token in Authorization Header and the entity of product requires the value you want to update in the request body wether its (name or price or amount) or any combination of them
6-you can delete product from "/api/product/{id}"
Protected
but you must login with role SELLER account and the post with this id is posted by the same seller then you put the token in Authorization Header and no body
7-you can deposit money in your account from "/api/user/deposit"
Protected
you provide its token in the authorization header with body (deposit)
8- you can buy product from "/api/product/buy/{prodId}" 
Protected
But make sure you provide the valid jwt token in the authorization token and you deposited money more than or equal the total value you want to buy
9-you can reset you deposit from 
"/api/user/reset" with your token in the Authorization header

**Notes**
-the port is by default 8080 so if you run it locally the base url should be: http://localhost:8080
-any thing wrapped in curly brackets {} in the url is path variable (param)
-all the urls with Protected require valid jwt token in the Authorization header
-you can change the data source from src -> main -> recources -> application.properties from fields:
-spring.datasource.url
-spring.datasource.username
-spring.datasource.password
since i didnt deploy my database
