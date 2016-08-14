package com.koens.autograph;

import com.koens.autograph.actionbar.ActionBarAPI;
import com.koens.autograph.commands.AutographCommand;
import com.koens.autograph.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class Autograph extends JavaPlugin {

    private String requesttxt;
    private String requestsenttxt;
    private String requestexpiredtxt;
    private String accepttxt;
    private String denytxt;
    private String countdowntxt;
    private String bookname;
    private String norequeststxt;
    private String nothingtosigntxt;
    private int requesttime;

    public HashMap<String, Integer> requests;
    public HashMap<String, List<String>> playerbooks;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        requests = new HashMap<String, Integer>();
        playerbooks = new HashMap<String, List<String>>();
        ActionBarAPI actionbar = new ActionBarAPI();
        actionbar.setup();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getCommand("autograph").setAliases(getConfig().getStringList("aliasses"));
        getCommand("autograph").setExecutor(new AutographCommand(this));
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {

    }

    private void loadConfig() {
        requesttxt = getConfig().getString("requesttxt");
        requestsenttxt = getConfig().getString("requestsenttxt");
        requestexpiredtxt = getConfig().getString("requestexpiredtxt");
        accepttxt = getConfig().getString("accepttxt");
        denytxt = getConfig().getString("denytxt");
        countdowntxt = getConfig().getString("countdowntxt");
        bookname = getConfig().getString("book-name");
        norequeststxt = getConfig().getString("norequeststxt");
        nothingtosigntxt = getConfig().getString("nothingtosigntxt");
        requesttime = getConfig().getInt("requesttime");
    }
    public String getRequesttxt() {
        return requesttxt;
    }
    public String getRequestsenttxt() {
        return requestsenttxt;
    }
    public String getRequestexpiredtxt() {
        return requestexpiredtxt;
    }
    public String getDenytxt() {
        return denytxt;
    }
    public String getAccepttxt() {
        return accepttxt;
    }
    public String getBookname() {
        return bookname;
    }
    public String getCountdowntxt() {
        return countdowntxt;
    }
    public String getNorequeststxt()  {
        return norequeststxt;
    }
    public String getNothingtosigntxt() {
        return nothingtosigntxt;
    }
    public int getRequesttime() {
        return requesttime;
    }

    public void putIntoRequestsMap(String key, Integer value) {
        requests.put(key, value);
    }
    public int getFromRequestsMap(String key) {
        return requests.get(key);
    }
    public void removeFromRequestsMap(String key) {
        requests.remove(key);
    }
    public boolean doesRequestMapContain(String key) {
        return requests.containsKey(key);
    }

    public void putIntoBooksMap(String player, List<String> pages) {
        playerbooks.put(player, pages);
    }
}
