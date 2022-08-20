package org.d1p4k.netherportalboom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class NetherPortalBoom implements ModInitializer {

    public static Logger logger = Logger.getLogger("NetherPortalBoom");
    private final File configFolder = FabricLoader.getInstance().getConfigDir().toFile();
    private final File configFile = new File(configFolder, "NetherPortalBoom.properties");

    private static float explosionRadius = 8.0F;

    @Override
    public void onInitialize() {

        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream(configFile);
            prop.load(input);
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        explosionRadius = Float.parseFloat(prop.getProperty("explosionRadius", "8.0"));
        FileWriter writer;
        try {
            writer = new FileWriter(configFile);
            prop.setProperty("explosionRadius", String.valueOf(explosionRadius));
            prop.store(writer, "PortalExplode configuration");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static float explosionRadius() {
        return explosionRadius;
    }
}
