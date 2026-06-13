package com.oxygenclient.module;

import com.oxygenclient.module.combat.*;
import com.oxygenclient.module.movement.*;
import com.oxygenclient.module.render.*;
import com.oxygenclient.module.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static List<Module> modules = new ArrayList<>();
    
    public static void init() {
        // Combat
        modules.add(new KillAura());
        modules.add(new AutoClicker());
        modules.add(new SilentAim());
        modules.add(new Reach());
        modules.add(new Velocity());
        modules.add(new Criticals());
        
        // Movement
        modules.add(new Speed());
        modules.add(new Fly());
        modules.add(new NoFall());
        modules.add(new Sprint());
        modules.add(new Jesus());
        
        // Render
        modules.add(new XRay());
        modules.add(new ESP());
        modules.add(new Fullbright());
        
        // World
        modules.add(new Disabler());
    }
    
    public static List<Module> getModules() {
        return modules;
    }
    
    public static List<Module> getModulesInCategory(Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }
    
    public static Module getModuleByName(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    public static void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}
