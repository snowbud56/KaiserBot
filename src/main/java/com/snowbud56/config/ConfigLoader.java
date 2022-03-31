package com.snowbud56.config;

/*
 * Created by snowbud56 on September 15, 2020
 * Do not change or use this code without permission
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigLoader {

    String load(File file) throws IOException {
        return new String(
                Files.readAllBytes(file.toPath())
        );
    }
}
