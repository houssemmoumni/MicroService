package com.example.back.Controllers;

import com.example.back.Entities.User;
import com.example.back.Entities.UserWrapper;
import com.example.back.ExceptionHandling.ApiResponse;
import com.example.back.SecurityConfig.KeycloakConfig;
import com.example.back.ServiceImp.UserLoginTrackingService;
import com.example.back.Services.UserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

import static com.example.back.SecurityConfig.KeycloakConfig.keycloak;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserLoginTrackingService loginTrackingService;

    // Create a user in Keycloak and database
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/CreateUser")
    public ResponseEntity<ApiResponse> addUser(@RequestBody UserWrapper userWrapper) {
        Keycloak k = KeycloakConfig.getInstance();
        UserRepresentation userRep = userWrapper.getKeycloakUser();

        if (userService.existsByLogin(userRep.getUsername()) || userService.existsByLogin(userRep.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "User already exists in the database", null));
        }

        try (Response response = k.realm("myRealm").users().create(userRep)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse(false, response.readEntity(String.class), null));
            } else {
                UserRepresentation userRepresentation = k.realm("myRealm").users().search(userRep.getUsername()).get(0);
                userService.assignRoles(userRepresentation.getId(), userRep.getRealmRoles());
                try {
                    User u = userService.addUser(userWrapper.getUser());
                    return ResponseEntity.ok(new ApiResponse(true, "User created successfully in Keycloak and database", u));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse(false, e.getMessage(), null));
                }
            }
        }
    }

    // Update user details
    @PutMapping("/UpdateUser")
    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserWrapper user) {
        try {
            User updatedUser = userService.UpdateUser(user.getUser());
            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully in Keycloak and database", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Update Failed: " + e.getMessage(), null));
        }
    }

    // Get profile picture URL from Keycloak
    @GetMapping("/getProfilePicture/{userId}")
    public ResponseEntity<String> getProfilePicture(@PathVariable String userId) {
        try {
            UserResource userResource = keycloak.realm("myRealm").users().get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            List<String> profilePictureList = userRepresentation.getAttributes().get("profile_picture");
            if (profilePictureList == null || profilePictureList.isEmpty()) {
                return ResponseEntity.ok("No profile picture set.");
            }
            return ResponseEntity.ok(profilePictureList.get(0));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve profile picture: " + e.getMessage());
        }
    }

    // Register all users from Keycloak to local DB
    @PreAuthorize("hasAnyAuthority('admin', 'user','chef_projet','ouvrier','architecte','candidat')")
    @PostMapping("/registerAllFromKeycloak")
    public ResponseEntity<ApiResponse> registerAllUsersFromKeycloak() {
        List<UserRepresentation> keycloakUsers = KeycloakConfig.getAllUsers();
        List<String> registeredUsers = new ArrayList<>();

        for (UserRepresentation keycloakUser : keycloakUsers) {
            String login = keycloakUser.getUsername();
            String email = keycloakUser.getEmail();
            String firstName = keycloakUser.getFirstName();
            String lastName = keycloakUser.getLastName();

            if (!userService.existsByLogin(login) && !userService.existsByLogin(email)) {
                List<String> roles = keycloak.realm("myRealm")
                        .users().get(keycloakUser.getId())
                        .roles().realmLevel().listAll()
                        .stream().map(RoleRepresentation::getName).collect(Collectors.toList());

                String assignedRole = "user";
                if (roles.contains("admin")) assignedRole = "admin";
                else if (roles.contains("architecte")) assignedRole = "architecte";
                else if (roles.contains("chef_projet")) assignedRole = "chef_projet";
                else if (roles.contains("ouvrier")) assignedRole = "ouvrier";
                else if (roles.contains("candidat")) assignedRole = "candidat";


                User newUser = new User();
                newUser.setLogin(login);
                newUser.setEmail(email);
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setRole(assignedRole);

                userService.addUser(newUser);
                registeredUsers.add(login);
            }
        }

        return ResponseEntity.ok(new ApiResponse(true, "✅ Registered " + registeredUsers.size() + " users.", registeredUsers));
    }

    // Get user by username
    @PreAuthorize("hasAnyAuthority('admin', 'user','chef_projet','ouvrier','architecte','candidat')")
    @GetMapping("/GetUserByUserName/{username}")
    public ResponseEntity<ApiResponse> getUserByUserName(@PathVariable String username) {
        Keycloak k = KeycloakConfig.getInstance();
        UserWrapper userWrapper = new UserWrapper();

        User user = userService.GetUserByUserName(username);
        if (user != null) {
            userWrapper.setUser(user);
            return ResponseEntity.ok(new ApiResponse(true, "User found in database", userWrapper));
        }

        List<UserRepresentation> keycloakUsers = k.realm("myRealm").users().search(username);
        if (!keycloakUsers.isEmpty()) {
            UserRepresentation keycloakUser = keycloakUsers.get(0);
            String keycloakUserId = keycloakUser.getId();

            List<RoleRepresentation> roles = k.realm("myRealm")
                    .users().get(keycloakUserId).roles().realmLevel().listAll();

            String assignedRole = "user";
            for (RoleRepresentation role : roles) {
                String roleName = role.getName().toLowerCase();
                if (roleName.equals("admin") || roleName.equals("architecte") ||
                        roleName.equals("chef_projet") || roleName.equals("ouvrier") || roleName.equals("candidat")){
                    assignedRole = roleName;
                    break;
                }
            }

            User newUser = new User();
            newUser.setLogin(keycloakUser.getUsername());
            newUser.setEmail(keycloakUser.getEmail());
            newUser.setFirstName(keycloakUser.getFirstName());
            newUser.setLastName(keycloakUser.getLastName());
            newUser.setKeycloakId(keycloakUserId);
            newUser.setRole(assignedRole);

            newUser = userService.addUser(newUser);
            userWrapper.setUser(newUser);
            return ResponseEntity.ok(new ApiResponse(true, "User registered from Keycloak", userWrapper));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "User not found in Keycloak or database", null));
    }

    // Get user by email
    @PreAuthorize("hasAnyAuthority('admin', 'user','chef_projet','ouvrier','architecte','candidat')")
    @GetMapping("/GetUserByEmail/{email}")
    public ResponseEntity<ApiResponse> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.GetUserByEmail(email);
        if (user.isPresent()) {
            UserWrapper userWrapper = new UserWrapper();
            userWrapper.setUser(user.get());
            return ResponseEntity.ok(new ApiResponse(true, "User found", userWrapper));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "User not found", null));
    }

    // Delete user from Keycloak and DB
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/DeleteUser/{username}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String username){
        Keycloak k = KeycloakConfig.getInstance();
        UserRepresentation userRepresentation = k.realm("myRealm").users().search(username).get(0);
        try {
            k.realm("myRealm").users().get(userRepresentation.getId()).remove();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
        userService.DeleteUserByUserName(username);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully in Keycloak and database ", null));
    }

    // Debug/test endpoint
    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestHeader Map<String, String> headers) {
        headers.forEach((k, v) -> System.out.println(k + ": " + v));
        return ResponseEntity.ok("Hello from USER-SERVICE");
    }

    // Check current authentication
    @GetMapping("/Mysession")
    public Authentication authentication(Authentication authentication) {
        log.info("Authentication: {}", authentication);
        return authentication;
    }

    // Get all users from local database
    @GetMapping("/GetAllUsers")
    @PreAuthorize("hasAnyAuthority('admin' , 'chef_projet')")
    public List<User> getAllUsers(){
        return userService.GetAllUsers();
    }

    // Get user login history
    @GetMapping("/GetUserLoginHistory/{username}")
    public ResponseEntity<List<Map<String, Object>>> getUserLoginHistory(@PathVariable String username) {
        String realm = "myRealm";
        List<Map<String, Object>> loginHistory = KeycloakConfig.getUserLoginEvents(realm, username);
        return ResponseEntity.ok(loginHistory);
    }

    // Upload profile picture as blob by email
    @PreAuthorize("hasAnyAuthority('admin', 'user','chef_projet','ouvrier','architecte','candidat')")
    @PostMapping("/uploadProfilePictureAsBlobByEmail/{email}")
    public ResponseEntity<ApiResponse> uploadProfilePictureAsBlobByEmail(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> optionalUser = userService.GetUserByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }
            User user = optionalUser.get();
            user.setImage(file.getBytes());
            userService.UpdateUser(user);
            return ResponseEntity.ok(new ApiResponse(true, "Profile picture uploaded successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Upload failed: " + e.getMessage(), null));
        }
    }

    // Get profile picture blob as base64 by email
    @GetMapping("/getProfilePictureBlobByEmail/{email}")
    public ResponseEntity<String> getProfilePictureAsBase64ByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userService.GetUserByEmail(email);
        if (optionalUser.isEmpty() || optionalUser.get().getImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No profile picture set.");
        }
        byte[] imageData = optionalUser.get().getImage();
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        return ResponseEntity.ok("data:image/png;base64," + base64Image);
    }

    // Get all users from Keycloak
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/GetAllUsersFromKeycloak")
    public ResponseEntity<List<Map<String, Object>>> getAllUsersFromKeycloak() {
        List<UserRepresentation> keycloakUsers = KeycloakConfig.getAllUsers();
        List<Map<String, Object>> usersList = new ArrayList<>();

        for (UserRepresentation user : keycloakUsers) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("enabled", user.isEnabled());
            userInfo.put("roles", user.getRealmRoles());

            usersList.add(userInfo);
        }

        return ResponseEntity.ok(usersList);
    }
    @PreAuthorize("hasAuthority('chef_projet')")
    @GetMapping("/GetOuvriers")
    public ResponseEntity<List<User>> getOuvriers() {
        List<User> ouvriers = userService.getUsersByRole("ouvrier");
        return ResponseEntity.ok(ouvriers);
    }
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateUserProfile(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam int numTel) {

        User updatedUser = userService.updateUserProfile(email, firstName, lastName, numTel);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);  // Retourne l'utilisateur mis à jour
        } else {
            return ResponseEntity.status(404).body("User not found");  // Si l'utilisateur n'est pas trouvé
        }
    }


}