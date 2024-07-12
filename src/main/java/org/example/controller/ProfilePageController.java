package org.example.controller;

import org.example.model.User;
import org.example.model.Graduate;
import org.example.model.CertificateRegistry;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfilePageController {

    private static final Logger LOGGER = Logger.getLogger(ProfilePageController.class.getName());
    private final UserService userService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ProfilePageController(UserService userService, ResourceLoader resourceLoader) {
        this.userService = userService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable Long id, Model model) {
        LOGGER.info("Fetching user with ID: " + id);
        try {
            User user = userService.findById(id);
            if (user == null) {
                LOGGER.warning("User with ID " + id + " not found.");
                model.addAttribute("message", "User not found.");
                return "error";  // Error page template
            }

            LOGGER.info("User fetched: " + user);
            model.addAttribute("user", user);

            // Using the photoPath field to display the photo path
            String profilePhotoPath = determineProfilePhotoPath(user);
            LOGGER.info("Profile photo path: " + profilePhotoPath);
            model.addAttribute("profilePhoto", profilePhotoPath);

            // Fetching rating if user is a graduate
            if (user instanceof Graduate) {
                Graduate graduate = (Graduate) user;
                Integer rating = userService.getRatingForGraduate(graduate.getId());
                LOGGER.info("Rating fetched: " + rating);
                model.addAttribute("rating", rating);

                // Fetching similar users
                List<CertificateRegistry> certificateRegistryList = graduate.getCertificateRegistryList();
                if (certificateRegistryList != null && !certificateRegistryList.isEmpty()) {
                    String primarySpecialty = certificateRegistryList.get(0).getSpecialty();
                    try {
                        List<User> similarUsers = userService.findSimilarUsersBySpecialty(primarySpecialty);
                        LOGGER.info("Similar users found: " + similarUsers.size());
                        model.addAttribute("similarUsers", similarUsers);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error fetching similar users for specialty: " + primarySpecialty, e);
                        model.addAttribute("similarUsers", List.of());
                    }
                } else {
                    LOGGER.warning("No specialties found for graduate with ID: " + id);
                    model.addAttribute("similarUsers", List.of());
                }
            } else {
                LOGGER.info("User is not a graduate, skipping similar users search.");
                model.addAttribute("similarUsers", List.of());
            }

            return "profile";  // Profile template name
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching user with ID " + id, e);
            model.addAttribute("message", "An error occurred while fetching the profile.");
            return "error";  // Error page template
        }
    }


    public String determineProfilePhotoPath(User user) {
        String[] extensions = new String[] {"jpg", "jpeg", "png"};

        for (String extension : extensions) {
            String path = "classpath:static/profile-photos/" + user.getId() + "." + extension;
            if (fileExists(path)) {
                String webPath = "/profile-photos/" + user.getId() + "." + extension;
                LOGGER.info("Found profile photo: " + webPath);
                return webPath;
            } else {
                LOGGER.info("Profile photo not found for path: " + path);
            }
        }

        // Return default path if none of the extensions were found
        String defaultPath = "/profile-photos/default.jpg";
        LOGGER.info("Returning default profile photo path for user ID: " + user.getId());
        return defaultPath;
    }

    private boolean fileExists(String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            return resource.exists() && resource.isFile();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if file exists: " + path, e);
            return false;
        }
    }
}
