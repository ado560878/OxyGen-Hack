package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "1.21 style critical hits when combo with KillAura", Category.COMBAT);
    }

    // KillAura kontrol eder, burada ek işlem yok
    // Sadece toggle durumunu bildirir
}
