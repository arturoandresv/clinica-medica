package edu.unimagdalena.clinica.security.service;

import edu.unimagdalena.clinica.dto.request.SignUpRequest;
import edu.unimagdalena.clinica.exception.notfound.RoleNotFoundException;
import edu.unimagdalena.clinica.model.Role;
import edu.unimagdalena.clinica.model.User;
import edu.unimagdalena.clinica.repository.RoleRepository;
import edu.unimagdalena.clinica.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
        return new UserInfoDetail(user);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public SignUpRequest addUser(SignUpRequest userInfo) {
        User user = User.builder()
                .username(userInfo.username())
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
}
