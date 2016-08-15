package com.koens.autograph;

import com.koens.autograph.actionbar.ActionBarAPI;
import com.koens.autograph.commands.AutographCommand;
import com.koens.autograph.commands.InfoCommand;
import com.koens.autograph.listeners.PlayerDropItemListener;
import com.koens.autograph.listeners.PlayerInventoryClickListener;
import com.koens.autograph.listeners.PlayerJoinListener;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Autograph extends JavaPlugin {

    private String requesttxt;
    private String requestsenttxt;
    private String requestexpiredtxt;
    private String requestfilledtxt;
    private String requestalreadyrunningtxt;
    private String signconfirmtxt;
    private String accepttxt;
    private String denytxt;
    private String countdowntxt;
    private String bookname;
    private String norequeststxt;
    private String nothingtosigntxt;
    private int requesttime;
    private int bookslot;
    private boolean doprefix;
    private boolean alwaysgiveonjoin;

    public HashMap<String, Integer> requests;
    public HashMap<String, ItemStack> playerbooks;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        requests = new HashMap<String, Integer>();
        playerbooks = new HashMap<String, ItemStack>();
        ActionBarAPI actionbar = new ActionBarAPI();
        actionbar.setup();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, alwaysgiveonjoin), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryClickListener(this), this);
        final AutographCommand maincmd = new AutographCommand(this, doprefix);
        final InfoCommand info = new InfoCommand();
        getCommand("autograph").setExecutor(maincmd);
        getCommand("a").setExecutor(maincmd);
        getCommand("sign").setExecutor(maincmd);
        getCommand("signature").setExecutor(maincmd);
        getCommand("ainfo").setExecutor(info);
        getCommand("autographinfo").setExecutor(info);
        getLogger().info("Autograph v." + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        requests.clear();
        playerbooks.clear();
        getLogger().info("Autograph v." + getDescription().getVersion() + " has been disabled!");
    }

    private void loadConfig() {
        requesttxt = getConfig().getString("requesttxt");
        requestsenttxt = getConfig().getString("requestsenttxt");
        requestexpiredtxt = getConfig().getString("requestexpiredtxt");
        requestfilledtxt = getConfig().getString("requestfilledtxt");
        requestalreadyrunningtxt = getConfig().getString("requestalreadyrunningtxt");
        signconfirmtxt = getConfig().getString("signconfirmtxt");
        accepttxt = getConfig().getString("accepttxt");
        denytxt = getConfig().getString("denytxt");
        countdowntxt = getConfig().getString("countdowntxt");
        bookname = getConfig().getString("book-name");
        norequeststxt = getConfig().getString("norequeststxt");
        nothingtosigntxt = getConfig().getString("nothingtosigntxt");
        requesttime = getConfig().getInt("requesttime");
        bookslot = getConfig().getInt("book-slot");
        doprefix = getConfig().getBoolean("do-prefix");
        alwaysgiveonjoin = getConfig().getBoolean("always-give-on-join");
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

    public String getRequestfilledtxt() {
        return requestfilledtxt;
    }

    public String getRequestalreadyrunningtxt() {
        return requestalreadyrunningtxt;
    }

    public String getSignconfirmtxt() {
        return signconfirmtxt;
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

    public String getNorequeststxt() {
        return norequeststxt;
    }

    public String getNothingtosigntxt() {
        return nothingtosigntxt;
    }

    public int getRequesttime() {
        return requesttime;
    }

    public int getBookslot() {
        return bookslot;
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

    public void putIntoBooksMap(String player, ItemStack book) {
        playerbooks.put(player, book);
    }

    public ItemStack getFromBooksMap(String key) {
        return playerbooks.get(key);
    }

    public void removeFromBooksMap(String key) {
        playerbooks.remove(key);
    }

    public boolean hasBooksMap(String key) {
        return playerbooks.containsKey(key);
    }
}
