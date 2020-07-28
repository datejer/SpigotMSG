package ga.ejer.MSG;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("msg").setExecutor(new CommandMsg());
    }

    private CommandMsg cmsg;

    @Override
    public void onDisable() {
        cmsg = new CommandMsg();
        cmsg.clearMsg();
    }
}
