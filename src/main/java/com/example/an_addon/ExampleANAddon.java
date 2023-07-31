package com.example.an_addon;

import com.example.an_addon.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
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

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting2");
    }



    @SubscribeEvent
    public void playerSleepInBed(PlayerSleepInBedEvent event){
        LOGGER.info("player just slept!");
        Player player = event.getEntity();
        LOGGER.info("player mana before:", CapabilityRegistry.getMana(player));
        IManaCap manaCap = CapabilityRegistry.getMana(player).resolve().get();
        int maxMana = manaCap.getMaxMana();
        manaCap.setMana(manaCap.getMaxMana());
/*
        CapabilityRegistry.getMana(player).ifPresent(oldMaxMana -> CapabilityRegistry.getMana(event.getEntity()).ifPresent(newMaxMana -> {
            newMaxMana.setMaxMana(oldMaxMana.getMaxMana());
            newMaxMana.setMana(oldMaxMana.getCurrentMana());
            newMaxMana.setBookTier(oldMaxMana.getBookTier());
            newMaxMana.setGlyphBonus(oldMaxMana.getGlyphBonus());
        }));

 */
        LOGGER.info("player mana should be regenerated");
        LOGGER.info("player mana after:", CapabilityRegistry.getMana(player));
        // TODO: works anytime bed is clicked

    }

    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent event){
        LOGGER.info("Item picked up!");
    }



}
