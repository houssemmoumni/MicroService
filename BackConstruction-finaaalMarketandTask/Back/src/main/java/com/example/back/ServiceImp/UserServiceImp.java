package com.example.back.ServiceImp;

import com.example.back.Entities.User;
import com.example.back.Repositories.UserRepository;
import com.example.back.SecurityConfig.KeycloakConfig;
import com.example.back.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    Keycloak keycloak = KeycloakConfig.getInstance();

    @Override
    public User addUser(User user) {
        log.info("Role being saved: {}", user.getRole());

        return userRepository.save(user);
    }

    @Override
    public List<User> AddUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public User UpdateUser(User updatedUser) {
        try {
            // ✅ Fetch the user from Keycloak using their username (login)
            List<UserRepresentation> keycloakUsers = keycloak.realm("myRealm")
                    .users()
                    .search(updatedUser.getLogin());

            if (keycloakUsers.isEmpty()) {
                log.error("❌ User not found in Keycloak: " + updatedUser.getLogin());
                throw new RuntimeException("User not found in Keycloak");
            }

            UserRepresentation keycloakUser = keycloakUsers.get(0);
            String keycloakUserId = keycloakUser.getId(); // Get Keycloak user ID

            // ✅ Update user fields in Keycloak
            keycloakUser.setFirstName(updatedUser.getFirstName());
            keycloakUser.setLastName(updatedUser.getLastName());
            keycloakUser.setEmail(updatedUser.getEmail());

            // ✅ Send the update request to Keycloak
            keycloak.realm("myRealm").users().get(keycloakUserId).update(keycloakUser);
            log.info("✅ User updated in Keycloak: " + updatedUser.getLogin());

            // ✅ After successful Keycloak update, update the database
            return userRepository.save(updatedUser);
        } catch (Exception e) {
            log.error("❌ Failed to update user in Keycloak: " + e.getMessage());
            throw new RuntimeException("Failed to update user in Keycloak");
        }
    }

    public void DeleteUserByUserName(String username) {
        Long id = userRepository.findByLogin(username).getId_User();
        userRepository.deleteById(id);
    }


    @Override
    public List<User> GetAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> GetUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User GetUserByUserName(String username) {
        return userRepository.findByLogin(username);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return List.of();
    }


    public void assignRoles(String userId, List<String> roles) {
        List<RoleRepresentation> roleList = rolesToRealmRoleRepresentation(roles);
        keycloak.realm("myRealm")
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleList);
    }


    private List<RoleRepresentation> rolesToRealmRoleRepresentation(List<String> roles) {
        List<RoleRepresentation> existingRoles = keycloak.realm("myRealm")
                .roles()
                .list();

        List<String> serverRoles = existingRoles
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        List<RoleRepresentation> resultRoles = new ArrayList<>();

        for (String role : roles) {
            int index = serverRoles.indexOf(role);
            if (index != -1) {
                resultRoles.add(existingRoles.get(index));
            } else {
                log.info("Role doesn't exist");
            }
        }
        return resultRoles;
    }

    public List<User> getOuvriers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user.getRole().equalsIgnoreCase("ouvrier"))
                .collect(Collectors.toList());
    }


    @Override
    public User updateUserProfile(String email, String firstName, String lastName, int numTel) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setNum_tel(numTel);
            return userRepository.save(user);  // Sauvegarder les modifications dans la base de données
        }
        return null;  // Si l'utilisateur n'existe pas
    }





    private final UserRepository userRepos;


    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login) != null;
    }


    @Override
    public User findById(Long idadmin) {
        return userRepository.findById(idadmin).orElse(null);
    }
}
