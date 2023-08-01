package com.example.an_addon.capability;

import com.example.an_addon.ExampleANAddon;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class SleepRegenCapabilityRegistry {

    public static final Capability<ISleepRegenCap> SLEEP_REGEN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});


    // Get ISleepRegenCap capability from the specified entity
    public static LazyOptional<ISleepRegenCap> getSleepRegenCap(final LivingEntity entity){
        if (entity == null)
            return LazyOptional.empty();
        return entity.getCapability(SLEEP_REGEN_CAPABILITY);
    }

    // Event handler for the ISleepCap capability
    @Mod.EventBusSubscriber(modid = ExampleANAddon.MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event){
            if (event.getObject() instanceof Player){
                SleepRegenCapAttacher.attach(event);
            }
        }
        @SubscribeEvent
        public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
            event.register(ISleepRegenCap.class);
        }

        // on player death and respawn from end, set player mana to pre-clone value
        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            Player oldPlayer = event.getOriginal();
            oldPlayer.revive();
            getSleepRegenCap(oldPlayer).ifPresent(oldSleepRegenCap -> getSleepRegenCap(event.getEntity()).ifPresent(newSleepRegenCap -> {
                newSleepRegenCap.setTimeLastSlept(oldSleepRegenCap.getTimeLastSlept());
            }));

            event.getOriginal().invalidateCaps();
        }

        // TODO: CapabilityRegistry - maintain on dimension change
        // TODO: On player login event
        // TODO: On player respawn event
    }
}
