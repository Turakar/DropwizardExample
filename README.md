# DropwizardExample
A hello world server you can register yourself on. It is build with dropwizard and gradle.

### Building
Just run `./gradlew jar` (`gradlew.bat jar` for windows users). The jar will be in `build/libs/xxx.jar`.
You have to have a PostgreSQL server up and running with a database called HelloWorld. You can create the necessary tables by running:

```java -jar xxx.jar db migrate hello-world.yml```

### Eclipse
Just import this project as a gradle project (File -> Import -> Gradle)

### Running
```java -jar xxx.jar server hello-world.yml```

### Usage
Everything happens on `/hello-world`. A normal GET gives you a "Hello Stranger!". If you specify the param name in the url, it will greet you by name. If you do a POST with the data `name=yourname&address=Mr` it will save that and greet more gracefully on GETs. If you send a DELETE with a param name in the url, the corresponding record in the database will be deleted.

### Curl examples:

```curl -i http://localhost:8080/hello-world```

```curl -i http://localhost:8080/hello-world?name=Foo```

```curl --data "name=Foo&address=Dr" http://localhost:8080/hello-world```
