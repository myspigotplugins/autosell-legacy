package io.github.shiryu.autosell.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static FileUtil instance;

    private FileUtil(){

    }

    @NotNull
    public File getFile(@NotNull String name, @NotNull final File file){

        if (!name.endsWith(".yml")) name = name + ".yml";

        if (file == null) return null;
        if (file.listFiles() == null) return null;

        for (File fileli : file.listFiles()){
            if (fileli.getName().equals(name)){
                return fileli;
            }
        }

        return null;
    }

    @NotNull
    public File createDirectoryIfDoNotExists(@NotNull final String name, @NotNull final File directory){
        File file = new File(directory + "/" + name + "/");

        if (!file.exists()){
            file.mkdirs();
        }

        return file;
    }

    @NotNull
    public File createFileIfDoNotExists(@NotNull final String name, @NotNull final File directory){
        File file = new File(directory, name);

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return file;
    }

    @NotNull
    public FileConfiguration loadFile(@NotNull final File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    @NotNull
    public ConfigurationSection getOrCreateSection(@NotNull final File file, @NotNull final String path){
        final FileConfiguration config = loadFile(file);

        if (config.get(path) == null){
            return config.createSection(path);
        }

        return config.getConfigurationSection(path);
    }

    public void saveFile(@NotNull final File file, @NotNull final FileConfiguration configuration){
        try{
            configuration.save(file);
            configuration.load(file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static synchronized FileUtil getInstance(){
        if (instance == null) instance = new FileUtil();

        return instance;
    }
}
