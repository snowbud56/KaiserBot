package com.snowbud56.config;

/*
 * Created by snowbud56 on September 15, 2020
 * Do not change or use this code without permission
 */

//import org.json.JSONObject;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Config extends JSONObject {

    private static Config instance;
    private static File file;

    public Config(File file) throws IOException {
        super(new ConfigLoader().load(file));
        Config.file = file;
        instance = this;
    }

    public static Config getInstance() {
        return instance;
    }

    public static void reloadConfig() {
        try {
            instance = new Config(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

