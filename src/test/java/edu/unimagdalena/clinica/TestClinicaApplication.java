package edu.unimagdalena.clinica;

import org.springframework.boot.SpringApplication;

public class TestClinicaApplication {

    public static void main(String[] args) {
        SpringApplication.from(ClinicaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
