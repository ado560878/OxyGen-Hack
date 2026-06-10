package com.oxygenclient.module;

import com.oxygenclient.module.combat.*;
import com.oxygenclient.module.movement.*;
import com.oxygenclient.module.render.*;
import com.oxygenclient.module.misc.*;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        add(new KillAuraGhost());
        add(new SilentAim());
        add(new AutoClicker());
        add(new Reach());
        add(new Velocity());
        add(new HitBox());
        add(new BackTrack());
        add(new Criticals());
        
        add(new Speed());
        add(new Fly());
        add(new NoFall());
        add(new Sprint());
        add(new Step());
        add(new Jesus());
        
        add(new XRay());
        add(new ESP());
        add(new Fullbright());
        add(new Tracers());
        add(new ChestESP());
        
        add(new Disabler());
    }

    private void add(Module m) { modules.add(m); }

    public List<Module> getModules() { return modules; }
    
    public List<Module> getByCategory(Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }

    public void onTick() {
        modules.stream().filter(Module::isEnabled).forEach(Module::onTick);
    }
}
