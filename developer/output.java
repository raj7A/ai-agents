```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>customer-loan-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>customer-loan-service</name>
    <description>REST based application for creating loans</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

```java
package com.example.customerloanservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Period cannot be null")
    @Min(value = 1, message = "Period must be at least 1")
    private Integer period;
}
```

```java
package com.example.customerloanservice.controller;

import com.example.customerloanservice.model.Loan;
import com.example.customerloanservice.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public Mono<ResponseEntity<Loan>> createLoan(@Valid @RequestBody Loan loan) {
        return loanService.createLoan(loan)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

    }
}
```

```java
package com.example.customerloanservice.service;

import com.example.customerloanservice.model.Loan;
import com.example.customerloanservice.repository.LoanRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }


    public Mono<Loan> createLoan(Loan loan) {
        return Mono.fromCallable(() -> loanRepository.save(loan));
    }
}
```

```java
package com.example.customerloanservice.repository;

import com.example.customerloanservice.model.Loan;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends ReactiveCrudRepository<Loan, Long> {
}
```

```java
package com.example.customerloanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerLoanServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerLoanServiceApplication.class, args);
    }

}
```

To run this application:

1.  **Save the code:** Save the above code as separate files in the appropriate directory structure (e.g., `com/example/customerloanservice`).  Make sure the project structure matches the package declarations.
2.  **Build with Maven:** Navigate to the root directory of your project in the terminal and run `mvn clean install`. This will compile the code and create a runnable JAR file.
3.  **Run the application:** After successful build, run the JAR file using `java -jar target/customer-loan-service-0.0.1-SNAPSHOT.jar` (or the appropriately named JAR file).


This will start the Spring Boot application. You can then test the REST endpoint using a tool like Postman or curl.  For example, to create a loan you would send a POST request to `http://localhost:8080/loans` with a JSON payload like this:

```json
{
  "customerId": 123,
  "period": 12
}
```

Remember to adjust the port if necessary.  The application includes validation;  sending a request with missing or invalid data will result in appropriate error responses.