package com.oxygenclient.ui;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.ModuleManager;
import com.oxygenclient.module.settings.BooleanSetting;
import com.oxygenclient.module.settings.ModeSetting;
import com.oxygenclient.module.settings.NumberSetting;
import com.oxygenclient.module.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    public static ClickGUI INSTANCE = new ClickGUI();
    
    private List<Panel> panels = new ArrayList<>();
    private Module selectedModule = null;
    private boolean bindingKey = false;
    private Setting slidingSetting = null;
    private boolean mousePressed = false;
    
    public ClickGUI() {
        super(java.net.URI.create(""));
        
        int x = 10;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, 20, 110, 260));
            x += 120;
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arka plan
        context.fill(0, 0, width, height, 0xAA000000);
        
        // Panelleri çiz
        for (Panel panel : panels) {
            panel.render(context, mouseX, mouseY, this);
        }
        
        // Keybind bekleme mesajı
        if (bindingKey && selectedModule != null) {
            String msg = "Press a key for " + selectedModule.getName() + "... (ESC to clear)";
            int msgWidth = textRenderer.getWidth(msg);
            context.fill(width/2 - msgWidth/2 - 5, height/2 - 12, width/2 + msgWidth/2 + 5, height/2 + 8, 0xCC000000);
            context.drawText(textRenderer, msg, width/2 - msgWidth/2, height/2 - 5, 0xFFD46BFF, true);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mousePressed = true;
        slidingSetting = null;
        
        for (Panel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button, this)) {
                return true;
            }
        }
        
        // Menü dışına tıklanırsa seçili modülü kapat
        if (selectedModule != null && !bindingKey) {
            selectedModule = null;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mousePressed = false;
        slidingSetting = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (slidingSetting != null && slidingSetting instanceof NumberSetting) {
            NumberSetting num = (NumberSetting) slidingSetting;
            double percent = (mouseX - (sliderX + 5)) / sliderWidth;
            percent = Math.max(0, Math.min(1, percent));
            double value = num.getMin() + (num.getMax() - num.getMin()) * percent;
            value = Math.round(value / num.getIncrement()) * num.getIncrement();
            value = Math.max(num.getMin(), Math.min(num.getMax(), value));
            num.setValue(value);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingKey) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                selectedModule.setKey(0);
                NotificationManager.add(new Notification("§b" + selectedModule.getName() + " §7→ Key Unbound", 1500));
            } else {
                selectedModule.setKey(keyCode);
                String keyName = GLFW.glfwGetKeyName(keyCode, scanCode);
                if (keyName == null) keyName = "KEY_" + keyCode;
                NotificationManager.add(new Notification("§b" + selectedModule.getName() + " §7→ §d" + keyName, 1500));
            }
            bindingKey = false;
            selectedModule = null;
            return true;
        }
        
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void close() {
        bindingKey = false;
        selectedModule = null;
        slidingSetting = null;
        super.close();
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
    
    // PANEL SINIFI
    class Panel {
        private Category category;
        private int x, y, width, height;
        private int scrollY = 0;
        private int expandedModuleY = -1;
        private Module expandedModule = null;
        
        public Panel(Category category, int x, int y, int width, int height) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, ClickGUI gui) {
            MinecraftClient mc = MinecraftClient.getInstance();
            var tr = mc.textRenderer;
            
            // Başlık
            context.fill(x, y, x + width, y + 18, 0xFF1a1a1a);
            context.fill(x, y, x + width, y + 2, 0xFFD46BFF);
            context.drawText(tr, category.name(), x + 5, y + 5, 0xFFD46BFF, true);
            
            // İçerik
            context.fill(x, y + 18, x + width, y + height, 0xCC111111);
            
            List<Module> modules = ModuleManager.getModulesInCategory(category);
            int moduleY = y + 22 - scrollY;
            
            for (Module module : modules) {
                // Modül butonu
                boolean isExpanded = (gui.selectedModule == module);
                int btnColor = module.isEnabled() ? 0xFFD46BFF : 0xFF333333;
                context.fill(x + 3, moduleY, x + width - 3, moduleY + 18, btnColor);
                
                // İsim
                context.drawText(tr, module.getName(), x + 7, moduleY + 5, 0xFFFFFFFF, true);
                
                // Değer varsa göster (KillAura range vb)
                String displayValue = module.getDisplayValue();
                if (displayValue != null && !displayValue.isEmpty()) {
                    context.drawText(tr, "§7" + displayValue, x + width - tr.getWidth(" " + displayValue) - 10, moduleY + 5, 0xFFAAAAAA, false);
                }
                
                // Ayarları varsa "▼" göster
                if (!module.getSettings().isEmpty()) {
                    context.drawText(tr, "▼", x + width - 15, moduleY + 5, 0xFFD46BFF, true);
                }
                
                // EXPANDED MENU (sağ tık ile açılan ayar menüsü)
                if (isExpanded && !gui.bindingKey) {
                    renderSettingsMenu(context, module, x + width + 5, moduleY, mouseX, mouseY, gui);
                }
                
                moduleY += 22;
            }
            
            height = Math.min(260, (modules.size() * 22) + 25);
        }
        
        private int sliderX, sliderWidth; // Drag için global
        
        private void renderSettingsMenu(DrawContext context, Module module, int menuX, int menuY, int mouseX, int mouseY, ClickGUI gui) {
            MinecraftClient mc = MinecraftClient.getInstance();
            var tr = mc.textRenderer;
            List<Setting> settings = module.getSettings();
            
            int menuWidth = 140;
            int menuHeight = 20 + (settings.size() * 32) + 35;
            
            // Menü arka planı
            context.fill(menuX, menuY, menuX + menuWidth, menuY + menuHeight, 0xDD111111);
            context.fill(menuX, menuY, menuX + menuWidth, menuY + 2, 0xFFD46BFF);
            
            // Başlık
            context.drawText(tr, "§l" + module.getName() + " Settings", menuX + 5, menuY + 6, 0xFFD46BFF, true);
            
            int currentY = menuY + 22;
            
            // Settings
            for (Setting setting : settings) {
                context.drawText(tr, setting.getName(), menuX + 8, currentY + 2, 0xFFCCCCCC, true);
                
                // BOOLEAN SETTING
                if (setting instanceof BooleanSetting) {
                    BooleanSetting bool = (BooleanSetting) setting;
                    String status = bool.isEnabled() ? "ON" : "OFF";
                    int color = bool.isEnabled() ? 0xFFD46BFF : 0xFF666666;
                    int statusX = menuX + menuWidth - tr.getWidth(status) - 10;
                    context.drawText(tr, status, statusX, currentY + 2, color, true);
                    
                    if (gui.mousePressed && isHovering(mouseX, mouseY, statusX - 5, currentY, tr.getWidth(status) + 10, 12)) {
                        bool.toggle();
                        NotificationManager.add(new Notification("§b" + setting.getName() + " §7→ " + (bool.isEnabled() ? "§aON" : "§cOFF"), 1000));
                        try { Thread.sleep(100); } catch(Exception e) {}
                    }
                }
                // NUMBER SETTING (SLIDER)
                else if (setting instanceof NumberSetting) {
                    NumberSetting num = (NumberSetting) setting;
                    
                    // Değer
                    String valueStr = String.valueOf(num.getValue());
                    context.drawText(tr, "§d" + valueStr, menuX + menuWidth - tr.getWidth(valueStr) - 10, currentY + 2, 0xFFD46BFF, true);
                    
                    // Slider
                    int sliderXPos = menuX + 8;
                    int sliderYPos = currentY + 14;
                    int sliderW = menuWidth - 16;
                    int progress = (int)((num.getValue() - num.getMin()) / (num.getMax() - num.getMin()) * sliderW);
                    
                    context.fill(sliderXPos, sliderYPos, sliderXPos + sliderW, sliderYPos + 4, 0xFF333333);
                    context.fill(sliderXPos, sliderYPos, sliderXPos + progress, sliderYPos + 4, 0xFFD46BFF);
                    
                    // Slider knob
                    context.fill(sliderXPos + progress - 2, sliderYPos - 2, sliderXPos + progress + 2, sliderYPos + 6, 0xFFFFFFFF);
                    
                    // Slider drag
                    if (gui.mousePressed && isHovering(mouseX, mouseY, sliderXPos, sliderYPos - 2, sliderW, 8)) {
                        gui.slidingSetting = setting;
                        gui.sliderX = sliderXPos;
                        gui.sliderWidth = sliderW;
                        double percent = (mouseX - sliderXPos) / (double) sliderW;
                        percent = Math.max(0, Math.min(1, percent));
                        double value = num.getMin() + (num.getMax() - num.getMin()) * percent;
                        value = Math.round(value / num.getIncrement()) * num.getIncrement();
                        value = Math.max(num.getMin(), Math.min(num.getMax(), value));
                        num.setValue(value);
                    }
                }
                // MODE SETTING
                else if (setting instanceof ModeSetting) {
                    ModeSetting mode = (ModeSetting) setting;
                    String modeText = mode.getMode();
                    
                    // Sol ok
                    context.drawText(tr, "<", menuX + menuWidth - 35, currentY + 2, 0xFFD46BFF, true);
                    // Mode adı
                    context.drawText(tr, modeText, menuX + menuWidth - tr.getWidth(modeText) - 15, currentY + 2, 0xFFFFFFFF, true);
                    // Sağ ok
                    context.drawText(tr, ">", menuX + menuWidth - 12, currentY + 2, 0xFFD46BFF, true);
                    
                    if (gui.mousePressed) {
                        // Sol ok
                        if (isHovering(mouseX, mouseY, menuX + menuWidth - 35, currentY, 10, 12)) {
                            mode.prevMode();
                            NotificationManager.add(new Notification("§b" + setting.getName() + " §7→ §d" + mode.getMode(), 1000));
                        }
                        // Sağ ok
                        if (isHovering(mouseX, mouseY, menuX + menuWidth - 12, currentY, 10, 12)) {
                            mode.nextMode();
                            NotificationManager.add(new Notification("§b" + setting.getName() + " §7→ §d" + mode.getMode(), 1000));
                        }
                    }
                }
                
                currentY += 32;
            }
            
            // KEYBIND BÖLÜMÜ
            context.fill(menuX, currentY - 2, menuX + menuWidth, currentY + 28, 0xFF222222);
            String keyText = module.getKey() == 0 ? "NONE" : getKeyName(module.getKey());
            context.drawText(tr, "§7Keybind:", menuX + 8, currentY + 5, 0xFFAAAAAA, true);
            context.drawText(tr, "§d" + keyText, menuX + menuWidth - tr.getWidth(keyText) - 10, currentY + 5, 0xFFD46BFF, true);
            
            // Değiştir butonu
            int btnW = 50;
            int btnH = 16;
            int btnX = menuX + menuWidth / 2 - btnW / 2;
            int btnY = currentY + 8;
            context.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0xFFD46BFF);
            context.drawText(tr, "CHANGE", btnX + 8, btnY + 4, 0xFF000000, true);
            
            if (gui.mousePressed && isHovering(mouseX, mouseY, btnX, btnY, btnW, btnH)) {
                gui.bindingKey = true;
                gui.selectedModule = module;
            }
        }
        
        private String getKeyName(int keyCode) {
            String name = GLFW.glfwGetKeyName(keyCode, 0);
            if (name != null) return name.toUpperCase();
            switch(keyCode) {
                case GLFW.GLFW_KEY_SPACE: return "SPACE";
                case GLFW.GLFW_KEY_LEFT_SHIFT: return "L_SHIFT";
                case GLFW.GLFW_KEY_RIGHT_SHIFT: return "R_SHIFT";
                case GLFW.GLFW_KEY_LEFT_CONTROL: return "L_CTRL";
                case GLFW.GLFW_KEY_RIGHT_CONTROL: return "R_CTRL";
                default: return "KEY_" + keyCode;
            }
        }
        
        private boolean isHovering(double mouseX, double mouseY, int x, int y, int w, int h) {
            return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button, ClickGUI gui) {
            List<Module> modules = ModuleManager.getModulesInCategory(category);
            int moduleY = y + 22 - scrollY;
            
            for (Module module : modules) {
                int btnX = x + 3;
                int btnY = moduleY;
                int btnW = width - 6;
                int btnH = 18;
                
                if (mouseX > btnX && mouseX < btnX + btnW && mouseY > btnY && mouseY < btnY + btnH) {
                    if (button == 0) { // SOL TIK -> Aç/Kapa
                        module.toggle();
                        NotificationManager.add(new Notification("§b" + module.getName() + " §7→ " + (module.isEnabled() ? "§aENABLED" : "§cDISABLED"), 1000));
                        return true;
                    } 
                    else if (button == 1) { // SAĞ TIK -> Ayar menüsünü aç/kapa
                        if (gui.selectedModule == module) {
                            gui.selectedModule = null;
                        } else {
                            gui.selectedModule = module;
                            gui.bindingKey = false;
                        }
                        return true;
                    }
                }
                moduleY += 22;
            }
            
            return false;
        }
    }
}
