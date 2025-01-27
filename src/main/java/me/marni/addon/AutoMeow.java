package me.marni.addon;

import me.marni.addon.modules.AutoMeowModule;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AutoMeow extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Initializing AutoMeow");
        // Modules
        Modules.get().add(new AutoMeowModule());
    }

    @Override
    public String getPackage() {
        return "me.marni.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("ItzMarni", "auto-meow-meteor");
    }
}
