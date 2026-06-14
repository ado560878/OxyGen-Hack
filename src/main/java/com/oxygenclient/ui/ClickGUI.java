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
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    public static ClickGUI INSTANCE = new ClickGUI();
    
    private List<Panel> panels = new ArrayList<>();
    private Module selectedModule = null;
    private boolean bindingKey = false;
    private NumberSetting slidingSetting = null;
    private int sliderX, sliderWidth;
    
    public ClickGUI() {
        super(Text.literal("ClickGUI"));
        
        int x = 10;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, 20, 110, 260));
            x += 120;
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Net arka plan - bulanıklık yok
        context.fill(0, 0, width, height, 0xDD000000);
        
        for (Panel panel : panels) {
            panel.render(context, mouseX, mouseY);
        }
        
        if (bindingKey && selectedModule != null) {
            String msg = "Press a key for " + selectedModule.getName() + "... (ESC to clear)";
            int msgWidth = textRenderer.getWidth(msg);
            context.fill(width/2 - msgWidth/2 - 5, height/2 - 12, width/2 + msgWidth/2 + 5, height/2 + 8, 0xCC000000);
            context.drawText(textRenderer, msg, width/2 - msgWidth/2, height/2 - 5, 0xFFD46BFF, false);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Panel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        
        if (selectedModule != null && !bindingKey) {
            selectedModule = null;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (slidingSetting != null) {
            double percent = (mouseX - sliderX) / (double) sliderWidth;
            percent = Math.max(0, Math.min(1, percent));
            double value = slidingSetting.getMin() + (slidingSetting.getMax() - slidingSetting.getMin()) * percent;
            value = Math.round(value / slidingSetting.getIncrement()) * slidingSetting.getIncrement();
            value = Math.max(slidingSetting.getMin(), Math.min(slidingSetting.getMax(), value));
            slidingSetting.setValue(value);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        slidingSetting = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingKey) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                selectedModule.setKey(0);
                NotificationManager.add(selectedModule.getName() + " → Key Unbound", 1500);
            } else {
                selectedModule.setKey(keyCode);
                String keyName = GLFW.glfwGetKeyName(keyCode, scanCode);
                if (keyName == null) keyName = "KEY_" + keyCode;
                NotificationManager.add(selectedModule.getName() + " → " + keyName, 1500);
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
    
    class Panel {
        private Category category;
        private int x, y, width, height;
        
        public Panel(Category category, int x, int y, int width, int height) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY) {
            var tr = MinecraftClient.getInstance().textRenderer;
            
            // Başlık - Net çizim
            context.fill(x, y, x + width, y + 18, 0xFF1a1a1a);
            context.fill(x, y, x + width, y + 2, 0xFFD46BFF);
            context.drawText(tr, category.name(), x + 5, y + 5, 0xFFD46BFF, false);
            
            // İçerik
            context.fill(x, y + 18, x + width, y + height, 0xCC111111);
            
            List<Module> modules = ModuleManager.getModulesInCategory(category);
            int moduleY = y + 22;
            
            for (Module module : modules) {
                boolean isExpanded = (selectedModule == module);
                int btnColor = module.isEnabled() ? 0xFFD46BFF : 0xFF333333;
                context.fill(x + 3, moduleY, x + width - 3, moduleY + 18, btnColor);
                
                context.drawText(tr, module.getName(), x + 7, moduleY + 5, 0xFFFFFFFF, false);
                
                String displayValue = module.getDisplayValue();
                if (displayValue != null) {
                    context.drawText(tr, displayValue, x + width - tr.getWidth(displayValue) - 10, moduleY + 5, 0xFFAAAAAA, false);
                }
                
                if (!module.getSettings().isEmpty()) {
                    context.drawText(tr, "▼", x + width - 15, moduleY + 5, 0xFFD46BFF, false);
                }
                
                if (isExpanded && !bindingKey) {
                    renderSettingsMenu(context, module, x + width + 5, moduleY, mouseX, mouseY);
                }
                
                moduleY += 22;
            }
        }
        
        private void renderSettingsMenu(DrawContext context, Module module, int menuX, int menuY, int mouseX, int mouseY) {
            var tr = MinecraftClient.getInstance().textRenderer;
            List<Setting> settings = module.getSettings();
            
            int menuWidth = 140;
            int menuHeight = 20 + (settings.size() * 32) + 35;
            
            context.fill(menuX, menuY, menuX + menuWidth, menuY + menuHeight, 0xDD111111);
            context.fill(menuX, menuY, menuX + menuWidth, menuY + 2, 0xFFD46BFF);
            context.drawText(tr, "§l" + module.getName() + " Settings", menuX + 5, menuY + 6, 0xFFD46BFF, false);
            
            int currentY = menuY + 22;
            
            for (Setting setting : settings) {
                context.drawText(tr, setting.getName(), menuX + 8, currentY + 2, 0xFFCCCCCC, false);
                
                if (setting instanceof BooleanSetting) {
                    BooleanSetting bool = (BooleanSetting) setting;
                    String status = bool.isEnabled() ? "ON" : "OFF";
                    int color = bool.isEnabled() ? 0xFFD46BFF : 0xFF666666;
                    int statusX = menuX + menuWidth - tr.getWidth(status) - 10;
                    context.drawText(tr, status, statusX, currentY + 2, color, false);
                    
                    if (MinecraftClient.getInstance().mouse.wasLeftButtonClicked() &&
                        mouseX > statusX - 5 && mouseX < statusX + tr.getWidth(status) + 5 && 
                        mouseY > currentY && mouseY < currentY + 12) {
                        bool.toggle();
                        NotificationManager.add(setting.getName() + " → " + (bool.isEnabled() ? "ON" : "OFF"), 1000);
                    }
                }
                else if (setting instanceof NumberSetting) {
                    NumberSetting num = (NumberSetting) setting;
                    String valueStr = String.valueOf(num.getValue());
                    context.drawText(tr, "§d" + valueStr, menuX + menuWidth - tr.getWidth(valueStr) - 10, currentY + 2, 0xFFD46BFF, false);
                    
                    int sliderXPos = menuX + 8;
                    int sliderYPos = currentY + 14;
                    int sliderW = menuWidth - 16;
                    int progress = (int)((num.getValue() - num.getMin()) / (num.getMax() - num.getMin()) * sliderW);
                    
                    context.fill(sliderXPos, sliderYPos, sliderXPos + sliderW, sliderYPos + 4, 0xFF333333);
                    context.fill(sliderXPos, sliderYPos, sliderXPos + progress, sliderYPos + 4, 0xFFD46BFF);
                    context.fill(sliderXPos + progress - 2, sliderYPos - 2, sliderXPos + progress + 2, sliderYPos + 6, 0xFFFFFFFF);
                    
                    if (MinecraftClient.getInstance().mouse.wasLeftButtonClicked() &&
                        mouseX > sliderXPos && mouseX < sliderXPos + sliderW && 
                        mouseY > sliderYPos - 2 && mouseY < sliderYPos + 6) {
                        slidingSetting = num;
                        sliderX = sliderXPos;
                        sliderWidth = sliderW;
                    }
                }
                else if (setting instanceof ModeSetting) {
                    ModeSetting mode = (ModeSetting) setting;
                    String modeText = mode.getMode();
                    
                    context.drawText(tr, "<", menuX + menuWidth - 35, currentY + 2, 0xFFD46BFF, false);
                    context.drawText(tr, modeText, menuX + menuWidth - tr.getWidth(modeText) - 15, currentY + 2, 0xFFFFFFFF, false);
                    context.drawText(tr, ">", menuX + menuWidth - 12, currentY + 2, 0xFFD46BFF, false);
                    
                    if (MinecraftClient.getInstance().mouse.wasLeftButtonClicked()) {
                        if (mouseX > menuX + menuWidth - 35 && mouseX < menuX + menuWidth - 25 && 
                            mouseY > currentY && mouseY < currentY + 12) {
                            mode.prevMode();
                            NotificationManager.add(setting.getName() + " → " + mode.getMode(), 1000);
                        }
                        if (mouseX > menuX + menuWidth - 12 && mouseX < menuX + menuWidth - 2 && 
                            mouseY > currentY && mouseY < currentY + 12) {
                            mode.nextMode();
                            NotificationManager.add(setting.getName() + " → " + mode.getMode(), 1000);
                        }
                    }
                }
                
                currentY += 32;
            }
            
            context.fill(menuX, currentY - 2, menuX + menuWidth, currentY + 28, 0xFF222222);
            String keyText = module.getKey() == 0 ? "NONE" : getKeyName(module.getKey());
            context.drawText(tr, "§7Keybind:", menuX + 8, currentY + 5, 0xFFAAAAAA, false);
            context.drawText(tr, "§d" + keyText, menuX + menuWidth - tr.getWidth(keyText) - 10, currentY + 5, 0xFFD46BFF, false);
            
            int btnW = 50;
            int btnH = 16;
            int btnX = menuX + menuWidth / 2 - btnW / 2;
            int btnY = currentY + 8;
            context.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0xFFD46BFF);
            context.drawText(tr, "CHANGE", btnX + 8, btnY + 4, 0xFF000000, false);
            
            if (MinecraftClient.getInstance().mouse.wasLeftButtonClicked() &&
                mouseX > btnX && mouseX < btnX + btnW && mouseY > btnY && mouseY < btnY + btnH) {
                bindingKey = true;
                selectedModule = module;
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
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            List<Module> modules = ModuleManager.getModulesInCategory(category);
            int moduleY = y + 22;
            
            for (Module module : modules) {
                int btnX = x + 3;
                int btnY = moduleY;
                int btnW = width - 6;
                int btnH = 18;
                
                if (mouseX > btnX && mouseX < btnX + btnW && mouseY > btnY && mouseY < btnY + btnH) {
                    if (button == 0) {
                        module.toggle();
                        NotificationManager.add(module.getName() + " " + (module.isEnabled() ? "ENABLED" : "DISABLED"), 1000);
                        return true;
                    } 
                    else if (button == 1) {
                        if (selectedModule == module) {
                            selectedModule = null;
                        } else {
                            selectedModule = module;
                            bindingKey = false;
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
