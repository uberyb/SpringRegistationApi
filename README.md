# Okta (Custom) Spring Registration API

## Running the Project
In terms of dependencies, I used [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), and the Okta Java SDK version 6.0.0. Also, this is a Maven project. The project can be run by cding into the backend directory and running


```
mvn spring-boot:run
```

## Setup
You'll need to edit `backend/src/main/resources/application.properties` to include you Okta URL (https://subdomain.okta.com) and an Okta API token generated for this tenant. This setup assumes firstName and lastName are not required attributes, so you will have to mark these as not required in your Okta profile editor.

## Testing
Postman or any SPA can be used to invoke the API. If running locally:

```
POST https://localhost:8080/register
{
	"email":"dan.yb.tobias@gmail.com"
}
```

Will create and activate a user with this login and email in your Okta tenant. Keep note of the response. For the email specified, the user should receive an activation token. From there we can do

```
POST http://localhost:8080/activate
{
	"token": {token},
	"password": {password},
	"email": {email}
}
```

This will prompt the API to validate the activation token, and if successful it will set the user's password to the specified password as well as update the user's login to what is specified in the `email` field.
