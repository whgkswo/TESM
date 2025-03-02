package net.whgkswo.tesm.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.whgkswo.tesm.gui.screen.MenuScreen;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_TESM = "key.category.tesm.tesm";
    public static final String KEY_NEXT_LINE = "key.tesm.next_line";
    public static final String KEY_OPEN_JOURNAL = "key.tesm.open_journal";
    public static final String KEY_OPEN_MENU = "key.tesm.open_menu";

    public static KeyBinding testKey;
    public static KeyBinding openJournal;
    public static KeyBinding openMenu;


    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            /*if(convOn){
                if(testKey.wasPressed()){
                    client.player.sendMessage(Text.literal("키 입력 감지됨"));
                }
            }
            if(openJournal.wasPressed()){
                client.setScreen(new JournalScreen());
            }*/
            if(openMenu.wasPressed()){
                client.setScreen(new MenuScreen());
            }
        });
    }
    public static void register(){
        testKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_NEXT_LINE, InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_LEFT, KEY_CATEGORY_TESM));
        openJournal = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_OPEN_JOURNAL, InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J, KEY_CATEGORY_TESM));
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_OPEN_MENU, InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_TAB, KEY_CATEGORY_TESM));

        registerKeyInputs();
    }
}
