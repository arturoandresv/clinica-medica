package edu.unimagdalena.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Email
    private String email;

    private String specialty;

    private LocalTime availableFrom;

    private LocalTime availableTo;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;
}
