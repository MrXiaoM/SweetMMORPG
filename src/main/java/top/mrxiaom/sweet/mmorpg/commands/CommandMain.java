package top.mrxiaom.sweet.mmorpg.commands;
        
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.func.AbstractModule;

import java.util.*;

@AutoRegister
public class CommandMain extends AbstractModule implements CommandExecutor, TabCompleter, Listener {
    public CommandMain(SweetMMORPG plugin) {
        super(plugin);
        registerCommand("sweetmmorpg", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1 && "reload".equalsIgnoreCase(args[0]) && sender.isOp()) {
            if (args.length == 2 && "database".equalsIgnoreCase(args[1])) {
                plugin.options.database().reloadConfig();
                plugin.options.database().reconnect();
                return t(sender, "&a数据库已重新连接");
            }
            plugin.reloadConfig();
            return t(sender, "&a配置文件已重载");
        }
        return true;
    }

    private static final List<String> emptyList = Lists.newArrayList();
    private static final List<String> listArg0 = Lists.newArrayList();
    private static final List<String> listOpArg0 = Lists.newArrayList("reload");
    private static final List<String> listArgs1Reload = Lists.newArrayList("database");
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
