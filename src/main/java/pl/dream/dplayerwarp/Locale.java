package pl.dream.dplayerwarp;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.dream.dreamlib.Color;

import java.io.File;
import java.io.IOException;
import java.util.*;

public enum Locale {
    NO_PERMISSION("noPermission", ""),
    RELOAD("reload", ""),
    ;

    private final String path;
    private String text;
    private List<String> list;

    public static HashMap<String, String> ALIASES;

    /**
     * Lang enum constructor.
     * @param path The string path.
     * @param values The default string.
     */
    Locale(String path, String... values) {
        this.path = path;
        if(values.length==1){
            this.text = values[0];
        }
        else{
            this.list = Arrays.asList(values);
        }
    }

    public static void loadMessages(JavaPlugin plugin) {
        ALIASES = new HashMap<>();

        File lang = new File(plugin.getDataFolder(), "messages.yml");
        if (!lang.exists()) {
            plugin.saveResource("messages.yml", false);
            lang = new File(plugin.getDataFolder(), "messages.yml");
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);

        if(conf.getConfigurationSection("aliases")!=null){
            for(String path:conf.getConfigurationSection("aliases").getKeys(false)){
                ALIASES.put(path, conf.getString("aliases."+path));
            }
        }

        for(Locale item: values()) {
            if (conf.getString(item.getPath()) == null) {
                if(item.text!=null){
                    conf.set(item.getPath(), item.text);
                }
                else{
                    conf.set(item.getPath(), item.getList());
                }
            }
            else{
                if(!conf.getStringList(item.getPath()).isEmpty()){
                    item.updateList(conf.getStringList(item.getPath()));
                }
                else if(conf.getString(item.getPath())!=null){
                    item.updateText(conf.getString(item.getPath()));
                }
            }
        }

        try {
            conf.save(lang);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public @NotNull List<String> getList(){
        if(list==null || list.isEmpty()){
            return Collections.singletonList(text);
        }

        return list;
    }
    public @NotNull String getText() {
        if(text==null){
            if(list!=null && !list.isEmpty()){
                return list.get(0);
            }
            return "";
        }

        return text;
    }

    @Override
    public String toString(){
        return getText();
    }

    private String getPath() {
        return path;
    }
    private void updateText(String newText){
        text = newText;
        ALIASES.forEach((key, value) -> {
            key = "{"+key+"}";
            text = text.replace(key, value);
        });
        text = Color.fixRGB(text);

        list = null;
    }
    private void updateList(List<String> newList){
        list = new ArrayList<>();

        for(String line:newList){
            for(String alias:ALIASES.keySet()){
                line = line.replace("{"+alias+"}", ALIASES.get(alias));
            }

            line = Color.fixRGB(line);
            list.add(line);
        }

        text = null;
    }
}
