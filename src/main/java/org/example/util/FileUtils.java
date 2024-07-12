package org.example.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".png", ".jpg", ".jpeg");

    public static String getProfilePhotoPath(String username, Long userId) {
        for (String extension : SUPPORTED_EXTENSIONS) {
            String path = "/profile photos/" + username + "-" + userId + extension;
            if (Files.exists(Paths.get(path))) {
                return "/" + path;
            }
        }
        return "/profile-photos/default.jpg"; // путь к изображению по умолчанию, если файл не найден
    }
}
