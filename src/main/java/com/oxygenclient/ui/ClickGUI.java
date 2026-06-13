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
import net.minecraft.client.font.TextRenderer;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    public static ClickGUI INSTANCE = new ClickGUI();
    
    private List<Panel> panels = new ArrayList<>();
    private Module selectedModule = null;
    private boolean bindingKey = false;
    
    public ClickGUI() {
        super(java.net.URI.create(""));
        
        int x = 10;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, 20, 100, 250));
            x += 110;
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arka plan karartması
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
            context.drawText(textRenderer, msg, width/2 - msgWidth/2, height/2 - 5, 0xFFFFFF, true);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Panel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button, this)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingKey) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                selectedModule.setKey(0);
                NotificationManager.add(new Notification("Key unbound for " + selectedModule.getName(), 1500));
            } else {
                selectedModule.setKey(keyCode);
                NotificationManager.add(new Notification(selectedModule.getName() + " bound to " + GLFW.glfwGetKeyName(keyCode, scanCode), 1500));
            }
            bindingKey = false;
            selectedModule = null;
            return true;
        }
        
        // ESC ile kapat
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
        private boolean dragging;
        private int dragX, dragY;
        private int scrollY = 0;
        
        public Panel(Category category, int x, int y, int width, int height) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, ClickGUI gui) {
            TextRenderer tr = MinecraftClient.getInstance().textRenderer;
            
            // Başlık çubuğu
            context.fill(x, y, x + width, y + 18, 0xFF1a1a1a);
            context.fill(x, y, x + width, y + 1, 0xFFD46BFF); // Pembe üst çizgi
            context.drawText(tr, category.name(), x + 5, y + 5, 0xFFD46BFF, false);
            
            // İçerik arka planı
            context.fill(x, y + 18, x + width, y + height, 0xCC111111);
            
            // Modülleri göster
            List<Module> modules = ModuleManager.getModulesInCategory(category);
            int moduleY = y + 20 - scrollY;
            
            for (Module module : modules) {
                // Modül butonu
                int moduleColor = module.isEnabled() ? 0xFFD46BFF : 0xFF333333;
                context.fill(x + 2, moduleY, x + width - 2, moduleY + 16, moduleColor);
                
                // İsim
                context.drawText(tr, module.getName(), x + 5, moduleY + 4, 0xFFFFFFFF, false);
                
                // Ayarları varsa "..." göster
                if (!module.getSettings().isEmpty()) {
                    context.drawText(tr, "...", x + width - 15, moduleY + 4, 0xFFAAAAAA, false);
                }
                
                // Hover durumunda ve genişletilmiş ayar menüsü
                if (isHovering(mouseX, mouseY, x + 2, moduleY, width - 4, 16) && gui.selectedModule == module) {
                    renderSettingsMenu(context, module, x + width + 5, moduleY, mouseX, mouseY, gui);
                }
                
                moduleY += 18;
            }
            
            // Scroll bölgesi sınırı
            height = Math.min(250, (modules.size() * 18) + 20);
        }
        
        private void renderSettingsMenu(DrawContext context, Module module, int menuX, int menuY, int mouseX, int mouseY, ClickGUI gui) {
            TextRenderer tr = MinecraftClient.getInstance().textRenderer;
            List<Setting> settings = module.getSettings();
            
            int menuWidth = 130;
            int menuHeight = 15 + (settings.size() * 25) + 30; // +30 for keybind
            
            context.fill(menuX, menuY, menuX + menuWidth, menuY + menuHeight, 0xDD111111);
            context.fill(menuX, menuY, menuX + menuWidth, menuY + 1, 0xFFD46BFF);
            
            int currentY = menuY + 5;
            
            // Settings
            for (Setting setting : settings) {
                context.drawText(tr, setting.getName(), menuX + 5, currentY, 0xFFCCCCCC, false);
                
                if (setting instanceof BooleanSetting) {
                    BooleanSetting bool = (BooleanSetting) setting;
                    String status = bool.isEnabled() ? "ON" : "OFF";
                    int color = bool.isEnabled() ? 0xFFD46BFF : 0xFF666666;
                    context.drawText(tr, status, menuX + menuWidth - 25, currentY, color, false);
                    
                    if (isHovering(mouseX, mouseY, menuX + menuWidth - 35, currentY, 30, 10)) {
                        if (mouseClicked) {
                            bool.toggle();
                            NotificationManager.add(new Notification(setting.getName() + " → " + bool.isEnabled(), 1000));
                        }
                    }
                }
                else if (setting instanceof NumberSetting) {
                    NumberSetting num = (NumberSetting) setting;
                    String value = String.valueOf(num.getValue());
                    context.drawText(tr, value, menuX + menuWidth - 35, currentY, 0xFFD46BFF, false);
                    
                    // Slider
                    int sliderX = menuX + 5;
                    int sliderY = currentY + 12;
                    int sliderWidth = menuWidth - 10;
                    int progress = (int)((num.getValue() - num.getMin()) / (num.getMax() - num.getMin()) * sliderWidth);
                    
                    context.fill(sliderX, sliderY, sliderX + sliderWidth, sliderY + 3, 0xFF333333);
                    context.fill(sliderX, sliderY, sliderX + progress, sliderY + 3, 0xFFD46BFF);
                }
                
                currentY += 25;
            }
            
            // Keybind bölümü
            context.fill(menuX, currentY - 2, menuX + menuWidth, currentY + 20, 0xFF222222);
            String keyText = module.getKey() == 0 ? "NONE" : GLFW.glfwGetKeyName(module.getKey(), 0);
            context.drawText(tr, "Keybind: " + keyText, menuX + 5, currentY + 5, 0xFFFFFFFF, false);
            
            if (isHovering(mouseX, mouseY, menuX + menuWidth - 40, currentY + 2, 35, 16)) {
                if (mouseClicked) {
                    gui.bindingKey = true;
                    gui.selectedModule = module;
                }
            }
        }
        
        private boolean isHovering(double mouseX, double mouseY, int x, int y, int w, int h) {
            return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button, ClickGUI gui) {
            List<Module> modules = ModuleManager.getModulesInCategory(category);
            int moduleY = y + 20 - scrollY;
            
            for (Module module : modules) {
                int btnX = x + 2;
                int btnY = moduleY;
                int btnW = width - 4;
                int btnH = 16;
                
                if (mouseX > btnX && mouseX < btnX + btnW && mouseY > btnY && mouseY < btnY + btnH) {
                    if (button == 0) { // SOL TIK -> Activate/Deactivate
                        module.toggle();
                        NotificationManager.add(new Notification(module.getName() + " " + (module.isEnabled() ? "ENABLED" : "DISABLED"), 1000));
                        return true;
                    } 
                    else if (button == 1) { // SAĞ TIK -> Ayar menüsünü aç
                        if (gui.selectedModule == module) {
                            gui.selectedModule = null;
                        } else {
                            gui.selectedModule = module;
                        }
                        return true;
                    }
                }
                moduleY += 18;
            }
            
            // Menü dışına tıklanırsa kapat
            if (gui.selectedModule != null) {
                gui.selectedModule = null;
            }
            return false;
        }
    }
}
