package ga.ejer.MSG;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class CommandMsg implements CommandExecutor {
    public static HashMap<UUID, UUID> msg = new HashMap<>();
    public static HashSet<UUID> spy = new HashSet<UUID>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ((s.equalsIgnoreCase("msg")) || (s.equalsIgnoreCase("message")) || (s.equalsIgnoreCase("t")) || (s.equalsIgnoreCase("tell")) || (s.equalsIgnoreCase("pm"))) {
            String message = "";
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                if (strings.length <= 0) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.GRAY + "Usage: " + ChatColor.GOLD + "/" + s.toString() + " <player> [message]");
                    return true;
                } else if (strings.length == 1) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.GRAY + "Please provide a message.");
                    return true;
                } else if (strings.length >= 2) {
                    if (Bukkit.getPlayerExact(strings[0]) == null) {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.RED + "The player " + ChatColor.GOLD + strings[0] + ChatColor.RED + " cannot be found");
                        return true;
                    }
                    for (int i = 1; i < strings.length; i++) {
                        message = message + strings[i] + " ";
                    }
                    Player target = Bukkit.getPlayerExact(strings[0]);

                    player.sendMessage(ChatColor.GRAY + "(To " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ") " + ChatColor.GRAY + message);
                    target.sendMessage(ChatColor.GRAY + "(From " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + ") " + ChatColor.GRAY + message);
                    for (UUID staff : spy) {
                        Bukkit.getPlayer(staff).sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Staff » " + ChatColor.GRAY + "(" + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " -> " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ") " + message);
                    }
                    msg.put(player.getUniqueId(), target.getUniqueId());
                    msg.put(target.getUniqueId(), player.getUniqueId());
                    return true;
                }
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.GRAY + "Only players can use this command");
                return true;
            }
        }

        if ((s.equalsIgnoreCase("reply")) || (s.equalsIgnoreCase("r"))) {
            String message = "";
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                if (strings.length <= 0) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.GRAY + "Usage: " + ChatColor.GOLD + "/" + s.toString() + " [message]");
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
                            for (UUID staff : spy) {
                                Bukkit.getPlayer(staff).sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Staff » " + ChatColor.GRAY + "(" + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " -> " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ") " + message);
                            }
                            msg.put(player.getUniqueId(), Bukkit.getPlayer(targetUUID).getUniqueId());
                            msg.put(Bukkit.getPlayer(targetUUID).getUniqueId(), player.getUniqueId());
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.RED + "An error occurred.");
                        return true;
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.GRAY + "Only players can use this command");
                return true;
            }
        }

        if ((s.equalsIgnoreCase("spy")) || (s.equalsIgnoreCase("socialspy")) || (s.equalsIgnoreCase("msgspy"))) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                if (player.hasPermission("MSG.spy")) {
                    if (spy.contains(player.getUniqueId())) {
                        spy.remove(player.getUniqueId());
                        commandSender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Staff » " + ChatColor.GRAY + "SocialSpy: " + ChatColor.RED + "OFF");
                    } else {
                        spy.add(player.getUniqueId());
                        commandSender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Staff » " + ChatColor.GRAY + "SocialSpy: " + ChatColor.GREEN + "ON");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.RED + "You don't have permission to use this command");
                }
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "» " + ChatColor.GRAY + "Only players can use this command");
            }
            return true;
        }
        return true;
    }

    public void clearMsg() {
        CommandMsg.msg.clear();
    }
}
