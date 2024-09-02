package com.MDB;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.UUID;

import com.MDB;

public class CommandMDB extends CommandBase {

    @Override
    public String getCommandName() {
        return "mdb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/mdb add <user> | /mdb view <page>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Usage: /mdb add <user> | /mdb view <page>"));
            return;
        }

        switch (args[0]) {
            case "add":
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /mdb add <user>"));
                    return;
                }
                String username = args[1];
                UUID uuid = getPlayerUUID(username);
                if (uuid == null) {
                    sender.addChatMessage(new ChatComponentText("Player not found!"));
                    return;
                }
                String url = "https://sky.shiiyu.moe/stats/" + username;
                String entry = username + "." + uuid.toString() + "." + url;
                ((MDB) Minecraft.getMinecraft().getModLoader().getMod("MacroDB")).saveEntryToDatabase(entry);
                sender.addChatMessage(new ChatComponentText("Added " + username + " to the cheater database."));
                break;

            case "view":
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(new ChatComponentText("Invalid page number."));
                        return;
                    }
                }
                displayDatabasePage(sender, page);
                break;

            default:
                sender.addChatMessage(new ChatComponentText("Unknown command. Usage: /mdb add <user> | /mdb view <page>"));
                break;
        }
    }

    private UUID getPlayerUUID(String username) {
        NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(username);
        return playerInfo != null ? playerInfo.getGameProfile().getId() : null;
    }

    private void displayDatabasePage(ICommandSender sender, int page) {
        CheaterLoggerMod modInstance = (CheaterLoggerMod) Minecraft.getMinecraft().getModLoader().getMod("cheaterlogger");
        int entriesPerPage = 10;
        int start = (page - 1) * entriesPerPage;
        int end = Math.min(start + entriesPerPage, modInstance.cheaterDatabase.size());

        if (start >= modInstance.cheaterDatabase.size() || start < 0) {
            sender.addChatMessage(new ChatComponentText("Page " + page + " does not exist."));
            return;
        }

        sender.addChatMessage(new ChatComponentText("Cheater Database - Page " + page));
        for (int i = start; i < end; i++) {
            sender.addChatMessage(new ChatComponentText(modInstance.cheaterDatabase.get(i)));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; //why the fuck would you need operator? are you dumb?
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; //again, this is client side
    }
}
