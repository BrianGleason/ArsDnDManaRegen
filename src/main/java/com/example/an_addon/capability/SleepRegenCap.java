package com.example.an_addon.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class SleepRegenCap implements ISleepRegenCap {
    private final LivingEntity livingEntity;

    private long timeSinceSlept;

    public SleepRegenCap(@Nullable final LivingEntity entity) {
        this.livingEntity = entity;
    }

    @Override
    public long getTimeLastSlept() {
        return timeSinceSlept;
    }

    @Override
    public long setTimeLastSlept(long newTimeSinceSlept) {
        timeSinceSlept = newTimeSinceSlept;
        return timeSinceSlept;
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("tss", getTimeLastSlept());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        setTimeLastSlept(tag.getLong("tss"));
    }
}
