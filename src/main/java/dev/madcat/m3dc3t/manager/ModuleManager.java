package dev.madcat.m3dc3t.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.event.events.Render3DEvent;
import dev.madcat.m3dc3t.features.Feature;
import dev.madcat.m3dc3t.features.gui.M3dC3tGui;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.modules.client.*;
import dev.madcat.m3dc3t.features.modules.combat.*;
import dev.madcat.m3dc3t.features.modules.misc.*;
import dev.madcat.m3dc3t.features.modules.movement.*;
import dev.madcat.m3dc3t.features.modules.player.*;
import dev.madcat.m3dc3t.features.modules.render.*;
import dev.madcat.m3dc3t.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        modules.add(new ClickGui());
        modules.add(new FontMod());
        modules.add(new PopCounter());
        modules.add(new GUIBlur());
        modules.add(new HUD());
        modules.add(new Criticals());
        modules.add(new WorldModify());
        modules.add(new AutoRejoin());
        modules.add(new Message());
        modules.add(new KillAura());
        modules.add(new Crasher());
        modules.add(new Peek());
        modules.add(new PhaseTrace());
        modules.add(new SmartTrap());
        modules.add(new Notify());
        modules.add(new CameraClip());
        modules.add(new NoRotate());
        modules.add(new Trajectories());
        modules.add(new TargetHUD());
        modules.add(new FullBright());
        modules.add(new Offhand());
        modules.add(new ArrowESP());
        modules.add(new NickHider());
        modules.add(new Respawn());
        modules.add(new CityESP());
        modules.add(new Shaders());
        modules.add(new Nuker());
        modules.add(new Timers());
        modules.add(new NameTags());
        modules.add(new AntiVoid());
        modules.add(new CrystalScale());
        modules.add(new NoSlow());
        modules.add(new BreakESP());
        modules.add(new Velocity());
        modules.add(new AntiChestGui());
        modules.add(new Sprint());
        modules.add(new AntiCrystal());
        modules.add(new ArmorWarner());
        modules.add(new Quiver());
        modules.add(new BlockFly());
        modules.add(new Animations());
        modules.add(new HoleSnap());
        modules.add(new ReverseStep());
        modules.add(new AntiLevitate());
        modules.add(new LogESP());
        modules.add(new HudComponents());
        modules.add(new BetterPortal());
        modules.add(new AutoDupe());
        modules.add(new ViewModel());
        modules.add(new Tracer());
        modules.add(new Speedmine());
        modules.add(new AutoLog());
        modules.add(new FastPlace());
        modules.add(new InventoryMove());
        modules.add(new MultiTask());
        modules.add(new Surround());
        modules.add(new NoFall());
        modules.add(new Blink());
        modules.add(new Anchor());
        modules.add(new Interact());
        modules.add(new StorageESP());
        modules.add(new Flight());
        modules.add(new IQBoost());
        modules.add(new Title());
        modules.add(new TestPhase());
        modules.add(new AntiPackets());
        modules.add(new PistonKicker());
        modules.add(new Strafe());
        modules.add(new Chat());
        modules.add(new Burrow());
        modules.add(new AutoArmor());
        modules.add(new AutoCrystal());
        modules.add(new ItemStats());
        modules.add(new AutoCity());
        modules.add(new BoatFly());
        modules.add(new Chams());
        modules.add(new ChestStealer());
        modules.add(new BreakCheck());
        modules.add(new FeetPad());
        modules.add(new HitMarkers());
        modules.add(new HoleESP());
        modules.add(new Ranges());
        modules.add(new PopChams());
        modules.add(new NoRender());
        modules.add(new AntiCity());
        modules.add(new InstantMine());
        modules.add(new AntiBurrow());
        modules.add(new AntiCev());
        modules.add(new AutoCiv());
        modules.add(new TrapSelf());
        modules.add(new AntiPistonKick());
        modules.add(new HoleFiller());
        modules.add(new Flatten());
        modules.add(new Replenish());
        modules.add(new FakePlayer());
        modules.add(new Reach());
        modules.add(new FastElytra());
        modules.add(new MCP());
        modules.add(new PacketEat());
        modules.add(new MCF());
        modules.add(new ChatQueue());
        modules.add(new PacketXP());
        modules.add(new XCarry());
        modules.add(new Step());
        modules.add(new ChatSuffix());
    }

    public static Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public static  <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public <T extends Module> T getModuleT(Class<T> clazz) {
        return modules.stream().filter(module -> module.getClass() == clazz).map(module -> (T) module).findFirst().orElse(null);
    }

    public void sortModules(boolean reverse) {
        sortedModules = getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        sortedModulesABC = new ArrayList<String>(getEnabledModulesName());
        sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof M3dC3tGui) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text);
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight();
                    if (module.isEnabled()) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled()) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = M3dC3t.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text);
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight();
                    if (module.isEnabled()) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled()) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }

    public static ArrayList<Module> nigger;

    public static ArrayList<Module> getModules() {
        return nigger;
    }

    public static boolean isModuleEnablednigger(String name) {
        Module modulenigger = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return modulenigger.isEnabled();
    }

    public static boolean isModuleEnablednigger(Module modulenigger) {
        return modulenigger.isEnabled();
    }

}

