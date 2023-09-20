package com.unseen.db.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public abstract class EntityModBase extends EntityCreature {
    public EntityModBase(World worldIn) {
        super(worldIn);
    }
}
