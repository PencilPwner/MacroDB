package com.MDB;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = "MDB", name = "MacroDB", version = "1.0")
public class mdb {

    private static final String DATABASE_FILE = "cheater_database.txt";
    protected List<String> cheaterDatabase = new ArrayList<>();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        loadDatabase();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMDB());
    }

    private void loadDatabase() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, DATABASE_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    cheaterDatabase.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void saveEntryToDatabase(String entry) {
        cheaterDatabase.add(entry);
        File file = new File(Minecraft.getMinecraft().mcDataDir, DATABASE_FILE);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(entry + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
