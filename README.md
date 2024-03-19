# Microservices Prototype for a E-Commerce Application.

Simple Curl to Create a New Spring Boot Project: 
```
curl https://start.spring.io/starter.zip 
    -d groupId=com.aromablossom 
    -d type=maven-project 
    -d artifactId=<NAME_OF_SERVICE>
    -d dependencies=<DEPENDENCIES_TO_ADD>
    -o <NAME_OF_SERVICE>.zip
```

# Project Structure
```
                      +-------------------------------------------+
 +-------------+      |            +---------------+              | ╔=========╗         
 | API Gateway |------+----------->| Order Service |================║ OrderDB ║
 +-------------+      |            +-------+-------+              | ╚=========╝        
      ^               |                    |                      |
      |               |                    |                      |
 +-----------+        |                    | +-----------------+  | ╔===========╗ 
 | Okta Auth |        |                    +-| Payment Service |====║ PaymentDB ║
 +-----------+        |                    | +-----------------+  | ╚===========╝ 
                      |                    |                      | 
                      | +---------------+  | +-----------------+  | ╔===========╗ 
          config_uri--+-| Config Server |  +-| Product Service |====║ ProductDB ║
                      | +---------------+    +-----------------+  | ╚===========╝ 
                      |                                           | 
                      +-------------------------------------------+ 
```
