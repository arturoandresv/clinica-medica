package edu.unimagdalena.clinica.security.service;

import edu.unimagdalena.clinica.dto.request.PasswordResetRequestDTO;
import edu.unimagdalena.clinica.dto.request.SignUpRequest;
import edu.unimagdalena.clinica.exception.ResourceNotFoundException;
import edu.unimagdalena.clinica.exception.alreadyexists.EmailAlreadyExistsException;
import edu.unimagdalena.clinica.exception.alreadyexists.UsernameAlreadyExistsException;
import edu.unimagdalena.clinica.exception.notfound.RoleNotFoundException;
import edu.unimagdalena.clinica.model.Doctor;
import edu.unimagdalena.clinica.model.Role;
import edu.unimagdalena.clinica.model.User;
import edu.unimagdalena.clinica.repository.DoctorRepository;
import edu.unimagdalena.clinica.repository.RoleRepository;
import edu.unimagdalena.clinica.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final DoctorRepository doctorRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new UserInfoDetail(user);
    }

    public void resetPassword(PasswordResetRequestDTO passwordResetRequestDTO) {
        User user = userRepository.findByUsername(passwordResetRequestDTO.email())
                .orElseThrow(() -> new ResourceNotFoundException("No existe una cuenta con ese correo"));

        user.setPassword(encoder.encode(passwordResetRequestDTO.newPassword()));
        userRepository.save(user);
    }

    public List<String> getUserRoles(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName().toUpperCase())
                .toList();
    }

    public SignUpRequest addUser(SignUpRequest userInfo) {
        if (userRepository.existsByUsername(userInfo.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(userInfo.email())) {
            throw new EmailAlreadyExistsException("Email is already taken");
        }

        User user = User.builder()
                .username(userInfo.email())
                .email(userInfo.email())
                .password(encoder.encode(userInfo.password()))
                .build();
        Set<String> strRoles = userInfo.roles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ADMIN")
                                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName("USER")
                                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new SignUpRequest(user.getUsername(), user.getEmail(), userInfo.password(), userInfo.roles());
    }

    public Long getDoctorIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getUser().getId().equals(user.getId()))
                .map(Doctor::getId)
                .findFirst()
                .orElse(null);
    }

}
