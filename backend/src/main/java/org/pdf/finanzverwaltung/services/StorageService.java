package org.pdf.finanzverwaltung.services;

import java.io.File;
import java.security.SecureRandom;
import java.util.Random;

import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.models.DUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    @Autowired
    private AppConfiguration config;

    private final Random random;

    public StorageService() {
        random = new SecureRandom();
    }

    public File getUserDir() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DUser user = (DUser) authentication.getPrincipal();

        File userDir = new File(config.getAppFolder(), user.getId() + "");
        if (!userDir.exists() && !userDir.mkdirs())
            return null;
        return userDir;
    }

    public File getNewUserFilePath(String name) {
        final File userDir = getUserDir();
        if (userDir == null)
            return null;

        File file = new File(userDir, name);
        if (file.exists())
            return null;

        return file;
    }

    public File getNewRandomUserFilePath(String extention) {
        return getNewRandomUserFilePath(null, extention);
    }

    public File getNewRandomUserFilePath(String prefix, String extention) {
        final File userDir = getUserDir();
        if (userDir == null)
            return null;

        File parent = new File(userDir, prefix == null ? "" : prefix);
        if (!parent.exists() && !parent.mkdirs())
            return null;

        File newFile;
        for (int i = 0; i < 3; i++) {
            newFile = new File(parent, Math.abs(random.nextInt()) + extention);
            if (!newFile.exists())
                return newFile;
        }

        return null;
    }
}
