package dev.madcat.m3dc3t.features.modules.client;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;

public class Title extends Module {
    private static Title INSTANCE = new Title();
    public Title() {
        super("ClientTitle", "Change client title.", Category.CLIENT, true, false, false);
        this.setInstance();
    }
    
    public static Title getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Title();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
    public Setting<String> titleName= this.register(new Setting<String>("TitleName", "CuteCat :3", "client title."));

}
