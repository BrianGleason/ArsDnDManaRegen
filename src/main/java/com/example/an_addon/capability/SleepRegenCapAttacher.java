package com.example.an_addon.capability;

import com.example.an_addon.ExampleANAddon;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SleepRegenCapAttacher {
    private static class SleepRegenCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(ExampleANAddon.MODID, "sleepregen");

        private final ISleepRegenCap backend = new SleepRegenCap(null);
        private final LazyOptional<ISleepRegenCap> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return SleepRegenCapabilityRegistry.SLEEP_REGEN_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final SleepRegenCapAttacher.SleepRegenCapProvider provider = new SleepRegenCapProvider();
        event.addCapability(SleepRegenCapAttacher.SleepRegenCapProvider.IDENTIFIER, provider);
    }
}
