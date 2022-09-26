# Text to Image as a Service (using virtual threads)

This is an example of how to use the data oriented programming in Java
to implement a simple REST API.

This service fakes the creation of images using DALL-E or Stable Diffusion.

The code provides 4 times the same code (the REST controller) starting
with a code using records and refactoring the code using the data oriented
programming.

To run this example
```
  export PATH=/path/to/java19
  mvn spring-boot:run
```

then open http://localhost:8080

## Configure Spring 6 / Spring Boot 3 to use virtual threads

Spring 6 and Spring Boot 3 should be compatible with the virtual threads  of Java 19.

To test, we currently need to use the milestone versions

```xml
   ...
    <repositories>
        <repository>
            <id>spring-libs-milestone</id>
            <url>https://repo.spring.io/libs-milestone</url>
        </repository>
    </repositories>

    <pluginRepositories>
        <pluginRepository>
            <id>spring-libs-milestone</id>
            <url>https://repo.spring.io/libs-milestone</url>
        </pluginRepository>
    </pluginRepositories>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.apache.tomcat.embed</groupId>
                <artifactId>tomcat-embed-core</artifactId>
                <version>10.1.0-M17</version>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>3.0.0-M5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ...
```

Then we need to configure the Tomcat thread pools to use virtual threads.
Tomcat uses two different pools, the `AsyncTaskExecutor` for the async tasks
and the usual Tomcat pool that can be set using a `TomcatProtocolHandlerCustomizer`.

```java
@SpringBootApplication
public class SpringApp {
	@Bean
	public AsyncTaskExecutor applicationTaskExecutor() {
		// executor used for async calls
		var executor = Executors.newVirtualThreadPerTaskExecutor();
		return new TaskExecutorAdapter(executor);
	}

	@Bean
	public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
		// executor used for sync calls
		return handler -> handler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}
}
```

That's all !
