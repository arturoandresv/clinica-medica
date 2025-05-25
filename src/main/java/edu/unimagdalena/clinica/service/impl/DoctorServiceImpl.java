package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.request.DoctorRequestDTO;
import edu.unimagdalena.clinica.dto.request.DoctorWithUserDTO;
import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;
import edu.unimagdalena.clinica.exception.alreadyexists.EmailAlreadyExistsException;
import edu.unimagdalena.clinica.exception.alreadyexists.UsernameAlreadyExistsException;
import edu.unimagdalena.clinica.exception.notfound.DoctorNotFoundException;
import edu.unimagdalena.clinica.exception.notfound.RoleNotFoundException;
import edu.unimagdalena.clinica.mapper.DoctorMapper;
import edu.unimagdalena.clinica.model.Doctor;
import edu.unimagdalena.clinica.model.User;
import edu.unimagdalena.clinica.repository.DoctorRepository;
import edu.unimagdalena.clinica.repository.RoleRepository;
import edu.unimagdalena.clinica.repository.UserRepository;
import edu.unimagdalena.clinica.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctorMapper::toDto).toList();
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository.findById(id).map(doctorMapper::toDto)
                .orElseThrow(()-> new DoctorNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = doctorMapper.toEntity(doctorRequestDTO);
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(()-> new DoctorNotFoundException("Doctor not found with id: " + id));
        existing.setFullName(doctorRequestDTO.fullName());
        existing.setEmail(doctorRequestDTO.email());
        existing.setSpecialty(doctorRequestDTO.specialty());
        existing.setAvailableFrom(doctorRequestDTO.availableFrom());
        existing.setAvailableTo(doctorRequestDTO.availableTo());
        return doctorMapper.toDto(doctorRepository.save(existing));
    }

    @Override
    public void deleteDoctor(Long id) {
        if(!doctorRepository.existsById(id)){
            throw new DoctorNotFoundException("Doctor not found with id: " + id);
        }

        doctorRepository.deleteById(id);
    }

    @Override
    public List<DoctorResponseDTO> findBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);
        if(doctors.isEmpty()){
            throw new DoctorNotFoundException("Doctor not found with specialty: " + specialty);
        }

        return doctors.stream().map(doctorMapper::toDto).toList();
    }

    @Override
    public DoctorResponseDTO registerDoctorWithUser(DoctorWithUserDTO doctorRequestDTO) {
        String email = doctorRequestDTO.email(); // usaremos esto como username también

        if (userRepository.existsByUsername(email)) {
            throw new UsernameAlreadyExistsException("El correo ya está en uso como usuario");
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Ya existe un usuario con este correo electrónico");
        }

        // Crear el usuario
        User user = User.builder()
                .username(email)
                .email(email)
                .password(encoder.encode(doctorRequestDTO.password()))
                .roles(Set.of(roleRepository.findByName("USER")
                        .orElseThrow(() -> new RoleNotFoundException("Rol USER no encontrado"))))
                .build();
        userRepository.save(user);

        // Crear el doctor vinculado al usuario
        Doctor doctor = Doctor.builder()
                .fullName(doctorRequestDTO.fullName())
                .email(email)
                .specialty(doctorRequestDTO.specialty())
                .availableFrom(doctorRequestDTO.availableFrom())
                .availableTo(doctorRequestDTO.availableTo())
                .user(user)
                .build();

        doctor = doctorRepository.save(doctor);

        return doctorMapper.toDto(doctor);
    }


}
