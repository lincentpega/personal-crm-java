package com.lincentpega.personalcrmjava;

import org.springframework.boot.SpringApplication;

public class TestPersonalCrmJavaApplication {

    public static void main(String[] args) {
        SpringApplication.from(PersonalCrmJavaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
