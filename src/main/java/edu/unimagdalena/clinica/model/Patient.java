package edu.unimagdalena.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Email
    private String email;

    private String phone;

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    private Set<MedicalRecord> medicalRecords;
}
