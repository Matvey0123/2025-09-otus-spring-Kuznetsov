package ru.otus.hw;

import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @ConditionalOnBooleanProperty("mongock.enabled")
    public MongockInitializingBeanRunner getBuilder(MongoClient reactiveMongoClient,
                                                    ApplicationContext context) {
        return MongockSpringboot.builder()
                .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, "library"))
                .addMigrationScanPackage("ru.otus.hw.mongock.changelog")
                .setSpringContext(context)
                .setTransactional(false)
                .buildInitializingBeanRunner();
    }

}
