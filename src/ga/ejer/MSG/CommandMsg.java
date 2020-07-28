package ga.ejer.MSG;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CommandMsg implements CommandExecutor {
    public static HashMap<UUID, UUID> msg = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ((s.equalsIgnoreCase("msg")) || (s.equalsIgnoreCase("message")) || (s.equalsIgnoreCase("t")) || (s.equalsIgnoreCase("tell")) || (s.equalsIgnoreCase("pm"))) {
            String message = "";
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                if (strings.length <= 0) {
                    player.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "Usage: " + ChatColor.GOLD + "/" + s.toString() + " <player> [message]");
                    return true;
                } else if (strings.length == 1) {
                    player.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "Please provide a message.");
                    return true;
                } else if (strings.length >= 2) {
                    Player target = Bukkit.getPlayerExact(strings[0]);
                    if (target == null) {
                        player.sendMessage(ChatColor.GOLD + "» " + ChatColor.RED + "The player " + ChatColor.GOLD + target.getName() + ChatColor.RED + " cannot be found");
                        return true;
                    }
                    for (int i = 1; i < strings.length; i++) {
                        message = message + strings[i] + " ";
                    }

                    player.sendMessage(ChatColor.GRAY + "(To " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ") " + ChatColor.GRAY + message);
                    target.sendMessage(ChatColor.GRAY + "(From " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + ") " + ChatColor.GRAY + message);
                    msg.put(player.getUniqueId(), target.getUniqueId());
                    msg.put(target.getUniqueId(), player.getUniqueId());
                    return true;
                }
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "Only players can use this command");
                return true;
            }
        }

        if ((s.equalsIgnoreCase("reply")) || (s.equalsIgnoreCase("r"))) {
            String message = "";
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                if (strings.length <= 0) {
                    player.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "Usage: " + ChatColor.GOLD + "/" + s.toString() + " [message]");
                    return true;
                } else if (strings.length >= 1) {
                    UUID targetUUID = msg.get(player.getUniqueId());

                    if (targetUUID != null) {
                        if (Bukkit.getPlayer(targetUUID) == null) {
                            msg.remove(player.getUniqueId());
                            return true;
                        } else {
                            Player target = Bukkit.getPlayer(targetUUID);
                            for (int i = 0; i < strings.length; i++) {
                                message = message + strings[i] + " ";
                            }
                            player.sendMessage(ChatColor.GRAY + "(To " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ") " + ChatColor.GRAY + message);
                            Bukkit.getPlayer(targetUUID).sendMessage(ChatColor.GRAY + "(From " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + ") " + ChatColor.GRAY + message);
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.GOLD + "» " + ChatColor.RED + "An error occurred.");
                        return true;
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "Only players can use this command");
                return true;
            }
        }

        return true;
    }

    public void clearMsg() {
        CommandMsg.msg.clear();
    }
}
