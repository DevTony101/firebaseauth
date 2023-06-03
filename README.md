# Firebase Auth [![License](https://img.shields.io/github/license/devtony101/firebaseauth?style=flat-square)](https://github.com/DevTony101/firebaseauth/blob/main/LICENSE)

If you need to protect your Spring Boot API with the help of Firebase quickly and stress-free then this is the library for you.
It will provide you with a simple yet powerful implementation to receive and decode a Firebase token and to put that information available
in Spring's own security context.

## Table of Contents
* [Features](#features)
* [Usage](#usage)
  * [Getting started](#getting-started)
  * [Implementing security configuration](#implementing-security-configuration)
  * [Setting Firebase configuration](#setting-firebase-configuration)
  * [Testing](#testing)
  * [About roles](#about-roles)
* [About](#about)
  * [Why should you use this library?](#why-should-you-use-this-library)
  * [FAQ](#faq)

## Features
- Quick and seamless security setup for your API
- CORS configuration and 401 error validations are already taken care of
- The information obtained from the decoded firebase token is put directly in the security context with a default role if one hast not been provided by the token (more of that later!)
- You won't need to manually read your firebase configuration file

## Usage
### Getting started
Before starting you need to make sure you are all set to begin installing maven
dependencies from **GitHub Packages**. If you are not sure or if this is the
first time you are going to use a dependency from there please go the
official GitHub documentation on the subject [here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

If you are ready to go please go ahead and open your `pom.xml` file and paste this:
```xml
<dependency>
  <groupId>io.github.devtony101</groupId>
  <artifactId>firebaseauth</artifactId>
  <version>1.2.3</version>
</dependency>
```
### Implementing security configuration
Once you have installed dependency go ahead and create a class called `SecurityConfiguration` and make it implement the `SimpleFirebaseSecurityConfiguration` interface, this will allow you
to make use of a pre-implemented CORS configuration. Don't forget to annotate the class with `@EnableWebSecurity`.

This library provides a custom security filter that receives the token and decodes it. All you need to do is set up the filter when configuring the rules of access to the different
paths of your API. Below is an example of how you may use it in your code:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().formLogin().disable()
        .httpBasic().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
        .and().authorizeRequests().permitAll().anyRequest().authenticated().and()
        .addFilterBefore(new FirebaseAuthFilter(), UsernamePasswordAuthenticationFilter.class);
  return http.build();
}
```

A few things to notice here:
1. We are passing a `CorsConfigurationSource` bean using the `corsConfigurationSource()` function. You don't need to implement this as is already provided to yoy by the `SimpleFirebaseSecurityConfiguration` interface. This implementation provides a basic CORS configuration allowing requests of types `GET`, `POST`, `PUT` and `DELETE`. You could always override this definition if you want.
2. We are passing a `AuthenticationEntryPoint` bean using the `restAuthenticationEntryPoint()` function. As before, this is provided to you already by the interface and its only purpose is to handle errors related to de decoding of the Firebase token.
3. We are passing an instance of the Firebase filter as a `UsernamePasswordAuthentication` filter.

### Setting Firebase configuration
You are halfway there! Now it's time to create a Firebase project, if you haven't already. If it's the first time ypu are working with firebase
you can begin by looking at Google's [official documentation](https://firebase.google.com/docs/projects/learn-more#setting_up_a_firebase_project_and_registering_apps) on how to start creating projects from the Firebase console.

Once you are ready, go to your project settings and generate a new service account key, which comes as a JSON file. Download it and put it in your Spring Boot application, specifically in your `resources` folder. Beware to ignore this file from any VCS as it holds sensitive information.
Now go to your Firebase console one more time and copy the url of your database instance.

For the Firebase security filter to function it needs to connect to your Firebase project, in order to do that you will have to provide it the JSON file you download it earlier. Configure these two environment variables before starting up your Spring Boot application:

- `FIREBASE_CONFIG_PATH`: The path to your JSON configuration file from Firebase. This path is relative to your `resources` folder and needs to be in the following format: `classpath:<dir>/<your_config_file>.json`
- `FIREBASE_DATABASE_URL`: The url of your realtime firebase database

### Testing
Everything is ready! Now you just need to initialize your Firebase configuration for your security filter to work. Go to your main class and annotate it with `@EnableFirebaseAuth`, like this:
```java
@EnableFirebaseAuth
@SpringBootApplication
public class MySpringBootApplication {

  public static void main(String[] args) {
    SpringApplication.run(MySpringBootApplication.class, args);
  }
}
```

If you did everything right, when you run your application you should see this message firing up in the console: `Firebase initialized correctly`.
Now you can begin making HTTP requests to your API, note that every endpoint will be secured and will ask for a valid Firebase token in the `Authorization` header. If you don't know how to generate a valid Firebase token take a look [here](https://firebase.google.com/docs/auth/admin/manage-users#create_a_user).

### About roles
When the Firebase token is decoded correctly, the security filter will try to extract the user's role from its custom claims, specifically it will look for the property `role` and will put it in the security context with the prefix `ROLE_`. That means that,
for example, if the logged-in user has a custom claim that says its role is `cashier`, then the role that will be available in the security context is `ROLE_CASHIER`.

If the logged-in user has no custom claim called `role` then it will be assigned the role of `ROLE_USER`.

### Example
If ypu want to see all these steps put together in a real Spring Boot project or want to see how  to protect your endpoints individually based on the role of the current logged-in user you can go to [this repo](https://github.com/DevTony101/spring-firebase-auth-example).

## About
### Why should you use this library?
You won't have to go through the burden of setting up your Spring Boot security from scratch let alone create a custom filter to decode your Firebase token.

With the help of this library and the steps provided here you could have a working, secured API in a matter of minutes, without ever worrying about the internals of the Firebase admin sdk.
### FAQ
#### How can I administrate the roles of my logged-in users?
As explained before the Firebase security filter provided to you attempts to extract the role from a custom claim called `role`. If you wish to update the role 
of a given user then you will need to set up this property using its UID. You can look up how to accomplish that [here](https://firebase.google.com/docs/auth/admin/custom-claims#set_and_validate_custom_user_claims_via_the_admin_sdk).
