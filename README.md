# DropwizardExample
A hello world server you can register yourself on. It is build with dropwizard and gradle.

### Building
Did you see this gradlew? Execute it with oneJar.
You have to have a PostgreSQL server up and running with a database called HelloWorld and a table called users. The table should have two columns, name and address. Both are varchar (choose a reasonably size yourself) and name is primary key. You also have to configure the connection using the hello-world.yml, only md5 is tested.

### Eclipse
Just import this project as a gradle project (File -> Import -> Gradle)

### Running
```java -jar DropwizardExample-standalone.jar server hello-world.yml```
