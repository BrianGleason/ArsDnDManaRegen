package com.example.an_addon;

import com.example.an_addon.capability.SleepRegenCapabilityRegistry;
import com.example.an_addon.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleANAddon.MODID)
public class ExampleANAddon
{
    public static final String MODID = "an_addon";

    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleANAddon() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistry.registerRegistries(modbus);
        ArsNouveauRegistry.registerGlyphs();
        modbus.addListener(this::setup);
        modbus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation prefix(String path){
        return new ResourceLocation(MODID, path);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ArsNouveauRegistry.registerSounds();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void playerSleepInBed(PlayerSleepInBedEvent event){
        LOGGER.info("player just slept!");
        Player player = event.getEntity();

        SleepRegenCapabilityRegistry.getSleepRegenCap(player).ifPresent(sleepRegenCap -> {
            LOGGER.info(sleepRegenCap);
            long currentTime = player.level.getDayTime();
            long lastSleepTime = sleepRegenCap.getTimeLastSlept();
            CapabilityRegistry.getMana(player).ifPresent(manaCap ->{
                double currentMana = manaCap.getCurrentMana();
                double maxMana = manaCap.getMaxMana();
                if (currentMana == maxMana) { return; }
                if (currentTime < lastSleepTime) {
                    manaCap.setMana(maxMana);
                    sleepRegenCap.setTimeLastSlept(currentTime);
                    player.sendSystemMessage(Component.translatable("The currents of time have been manipulated. After some rest your energy feels renewed."))
                }
                else if (currentTime - lastSleepTime > 20000){
                    manaCap.setMana(maxMana);
                    sleepRegenCap.setTimeLastSlept(currentTime);
                    player.sendSystemMessage(Component.translatable("After some rest your energy feels renewed. You can rest again once most of a day has passed"));
                }
                else{
                    player.sendSystemMessage(Component.translatable("You have already renewed your energy by resting today, so your energy cannot be renewed"));
                }
            });
        });
    }
}
