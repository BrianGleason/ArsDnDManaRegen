package com.example.an_addon.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISleepRegenCap  extends INBTSerializable<CompoundTag> {

    long getTimeLastSlept();

    long setTimeLastSlept(final long timeSinceSlept);
}
