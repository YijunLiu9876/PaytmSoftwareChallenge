# Paytm Software Developer Challenge
Solving Paytm challenge @  https://github.com/PaytmLabs/SoftwareChallenge

### Environment
* Java 8
* Maven 3
* Cassandra 3.11.2 (running on port 9042)

### How to Run
* `git clone https://github.com/YijunLiu9876/PaytmSoftwareChallenge`
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

### TODO
* Add WebFlux and Reactive Cassandra to make this service fully reative.
* Find a better way to generate random tweet, instead of filtering the REST endpoint response against db.


