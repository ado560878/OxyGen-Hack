package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class Reach extends Module {
    private final NumberSetting distance = new NumberSetting("Distance", "Reach distance", 3.5, 3.0, 6.0, 0.1);

    public Reach() {
        super("Reach", "Extended reach", Category.COMBAT);
        addSetting(distance);
    }
}
