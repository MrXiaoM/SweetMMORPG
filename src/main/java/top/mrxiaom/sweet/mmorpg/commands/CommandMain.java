package top.mrxiaom.sweet.mmorpg.commands;
        
import com.google.common.collect.Lists;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.ResourceData;
import top.mrxiaom.sweet.mmorpg.api.ResourceRegainReason;
import top.mrxiaom.sweet.mmorpg.func.AbstractModule;

import java.util.*;

@AutoRegister
public class CommandMain extends AbstractModule implements CommandExecutor, TabCompleter, Listener {
    private String msgReload, msgReloadDatabase, msgNotType, msgNotOnline, msgNotNumber, msgGiveMana, msgGiveStamina;
    public CommandMain(SweetMMORPG plugin) {
        super(plugin);
        registerCommand("sweetmmorpg", this);
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        msgReload = config.getString("messages.reload", "");
        msgReloadDatabase = config.getString("messages.reload-database", "");
        msgNotType = config.getString("messages.not-type", "");
        msgNotOnline = config.getString("messages.not-online", "");
        msgNotNumber = config.getString("messages.not-number", "");
        msgGiveMana = config.getString("messages.give-mana", "");
        msgGiveStamina = config.getString("messages.give-stamina", "");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 4 && "give".equalsIgnoreCase(args[0]) && sender.isOp()) {
            String type = args[1];
            if (!type.equals("mana") && !type.equals("stamina")) {
                return t(sender, msgNotType);
            }
            Player player = Util.getOnlinePlayer(args[2]).orElse(null);
            ResourceData data = plugin.getPlayerDatabase().getOrCached(player);
            RPGPlayer rpg = data == null ? null : data.getPlayer();
            if (player == null || data == null || rpg == null || !data.toMythicLib().isOnline()) {
                return t(sender, msgNotOnline);
            }
            Double value = Util.parseDouble(args[3]).orElse(null);
            if (value == null) {
                return t(sender, msgNotNumber);
            }
            boolean silent = args.length >= 5 && args[4].equals("-s");
            if (type.equals("mana")) {
                rpg.giveMana(value);
                if (!silent) return t(sender, msgGiveMana
                        .replace("%player%", player.getName())
                        .replace("%value%", String.valueOf(value)));
            }
            if (type.equals("stamina")) {
                rpg.giveStamina(value);
                if (!silent) return t(sender, msgGiveStamina
                        .replace("%player%", player.getName())
                        .replace("%value%", String.valueOf(value)));
            }
            return true;
        }
        if (args.length >= 1 && "reload".equalsIgnoreCase(args[0]) && sender.isOp()) {
            if (args.length == 2 && "database".equalsIgnoreCase(args[1])) {
                plugin.options.database().reloadConfig();
                plugin.options.database().reconnect();
                return t(sender, msgReloadDatabase);
            }
            plugin.reloadConfig();
            return t(sender, msgReload);
        }
        return true;
    }

    private static final List<String> emptyList = Lists.newArrayList();
    private static final List<String> listArg0 = Lists.newArrayList();
    private static final List<String> listOpArg0 = Lists.newArrayList("give", "reload");
    private static final List<String> listArgs1Reload = Lists.newArrayList("database");
    private static final List<String> listArgs1Give = Lists.newArrayList("mana", "stamina");
    private static final List<String> listArgs4Give = Lists.newArrayList("-s");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(sender.isOp() ? listOpArg0 : listArg0, args[0]);
        }
        if (args.length == 2) {
            if ("reload".equalsIgnoreCase(args[0]) && sender.isOp()) {
                return startsWith(listArgs1Reload, args[1]);
            }
            if ("give".equalsIgnoreCase(args[0]) && sender.isOp()) {
                return startsWith(listArgs1Give, args[1]);
            }
        }
        if (args.length == 3) {
            if ("give".equalsIgnoreCase(args[0]) && sender.isOp()) {
                return null;
            }
        }
        if (args.length == 5) {
            if ("give".equalsIgnoreCase(args[0]) && sender.isOp()) {
                return startsWith(listArgs4Give, args[4]);
            }
        }
        return emptyList;
    }

    public List<String> startsWith(Collection<String> list, String s) {
        return startsWith(null, list, s);
    }
    public List<String> startsWith(String[] addition, Collection<String> list, String s) {
        String s1 = s.toLowerCase();
        List<String> stringList = new ArrayList<>(list);
        if (addition != null) stringList.addAll(0, Lists.newArrayList(addition));
        stringList.removeIf(it -> !it.toLowerCase().startsWith(s1));
        return stringList;
    }
}
