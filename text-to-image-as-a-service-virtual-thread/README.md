## Configure Spring 6 / Spring Boot 3 to use virtual threads

The new Spring 6 and Spring Boot 3 should be compatible with
the virtual threads of Java 19.

To run this example
```
  export PATH=/path/to/java19
  mvn spring-boot:run
```

then open http://localhost:8080

To test, we currently need to use the milestone versions of Spring / Spring Boot
because there is no release yet.

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

We also need to fix the imports in JPA code because Spring 6 stop supporting
the JavaEE spec and support the Jakarta EE spec instead.

[Invoice.java](https://github.com/forax/dop-examples/blob/master/text-to-image-as-a-service-virtual-thread/src/main/java/com/github/forax/dop/db/Invoice.java#L3)
```java
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
```



That's all !
