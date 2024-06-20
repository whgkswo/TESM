package net.whgkswo.tesm.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.OnServerTicks;

import static net.minecraft.server.command.CommandManager.literal;

public class ToggleTimeflow {
    public static void register(){
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("toggleTimeflow")
                .executes(context -> {
                    if(OnServerTicks.timeFlowOn){
                        //context.getSource().sendFeedback(() -> Text.literal("시간 흐름 정지"),false);
                        context.getSource().getPlayer().sendMessage(Text.literal("시간 흐름 정지"));
                        OnServerTicks.timeFlowOn = false;
                    } else{
                        //context.getSource().sendFeedback(() -> Text.literal("시간 흐름 재개"),false);
                        context.getSource().getPlayer().sendMessage(Text.literal("시간 흐름 재개"));
                        OnServerTicks.timeFlowOn = true;
                    }
                    return 1;
                }))));
    }
}
