package top.mrxiaom.sweet.mmorpg;
        
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.registry.NamespacedRegistry;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.resolver.DefaultLibraryResolver;
import top.mrxiaom.pluginbase.utils.ClassLoaderWrapper;
import top.mrxiaom.pluginbase.utils.ConfigUtils;
import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.pluginbase.utils.scheduler.FoliaLibScheduler;
import top.mrxiaom.sweet.mmorpg.api.StatType;
import top.mrxiaom.sweet.mmorpg.comp.AuraStats;
import top.mrxiaom.sweet.mmorpg.comp.EnumManager;
import top.mrxiaom.sweet.mmorpg.comp.MMOHook;
import top.mrxiaom.sweet.mmorpg.comp.Placeholders;
import top.mrxiaom.sweet.mmorpg.database.PlayerDatabase;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

public class SweetMMORPG extends BukkitPlugin {
    public static SweetMMORPG getInstance() {
        return (SweetMMORPG) BukkitPlugin.getInstance();
    }

    public SweetMMORPG() throws Exception {
        super(options()
                .bungee(false)
                .adventure(false)
                .database(true)
                .reconnectDatabaseWhenReloadConfig(false)
                .scanIgnore("top.mrxiaom.sweet.mmorpg.libs")
        );
        scheduler = new FoliaLibScheduler(this);

        info("正在检查依赖库状态");
        File librariesDir = ClassLoaderWrapper.isSupportLibraryLoader
                ? new File("libraries")
                : new File(this.getDataFolder(), "libraries");
        DefaultLibraryResolver resolver = new DefaultLibraryResolver(getLogger(), librariesDir);

        YamlConfiguration overrideLibraries = ConfigUtils.load(resolve("./.override-libraries.yml"));
        for (String key : overrideLibraries.getKeys(false)) {
            resolver.getStartsReplacer().put(key, overrideLibraries.getString(key));
        }
        resolver.addResolvedLibrary(BuildConstants.RESOLVED_LIBRARIES);

        List<URL> libraries = resolver.doResolve();
        info("正在添加 " + libraries.size() + " 个依赖库到类加载器");
        for (URL library : libraries) {
            this.classLoader.addURL(library);
        }
    }
    private PlayerDatabase playerDatabase;
    private EnumManager manager;

    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    public EnumManager getManagerType() {
        return manager;
    }

    @Override
    protected void beforeLoad() {
        for (StatType type : StatType.values())
            type.registerStat();
    }

    @Override
    protected void beforeEnable() {
        options.registerDatabase(
                playerDatabase = new PlayerDatabase(this)
        );
    }

    @Override
    protected void beforeReloadConfig(FileConfiguration config) {
        if (manager == null) {
            manager = Util.valueOr(EnumManager.class, config.getString("manager", "BuiltIn"), EnumManager.BuiltIn);
        }
    }

    @Override
    protected void afterEnable() {
        FileConfiguration config = getConfig();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
        }
        MMOHook handler = new MMOHook(this);
        handler.setRPG();
        if (config.getBoolean("hooks.AuraSkills", false)) {
            AuraSkillsApi api = AuraSkillsApi.get();
            NamespacedRegistry registry = api.useRegistry(getDescription().getName().toLowerCase(), getDataFolder());
            registry.registerStat(AuraStats.STAMINA);
        }
        Bukkit.getOnlinePlayers().forEach(playerDatabase::load);
        getLogger().info("SweetMMORPG 加载完毕");
    }
}
