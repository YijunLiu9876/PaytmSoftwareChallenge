# Paytm Software Developer Challenge
Solving Paytm challenge @  https://github.com/PaytmLabs/SoftwareChallenge
### Environment
* Java 8
* Maven 3
* Cassandra 3.11.2 running on port 9042
### How to Run
* `git clone <this repo>`
* `cd paytm-core-service`
* test: `mvn test`
* run application: `mvn spring-boot:run`
### How to Use
* Register User 
`curl -X POST http://localhost:8080/users/register -d "username=username&password=password"`
* Get Access Token
`curl -X POST -vu acme:acmesecret http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=password&username=username&grant_type=password"`
* Hello World
`curl -X GET http://localhost:8080/HelloWorld -H 'Authorization: Bearer <Access Token>'` 
* Tweets Search
`curl -X GET http://localhost:8080/tweets/search/<query string> -H 'Authorization: Bearer <Access Token>'` 
* I’m feeling lucky 
`curl -X GET http://localhost:8080/tweets/lucky/<query string> -H 'Authorization: Bearer <Access Token>'` 
* Change Password
`curl -X POST http://localhost:8080/users/changePassword -H 'Authorization: Bearer <Access Token>' -d "oldPassword=password&newPassword=newpass"`


