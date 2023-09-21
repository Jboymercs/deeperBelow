package com.unseen.db.entity;

import com.unseen.db.entity.ai.MobGroundNavigate;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.PriorityQueue;

public abstract class EntityModBase extends EntityCreature {
    private float regenTimer;

    public EntityModBase(World worldIn) {
        super(worldIn);
        this.experienceValue = 5;
    }

    public EntityModBase (World worldIn, float x, float y, float z) {
        super(worldIn);
        this.setPosition(x, y, z);
    }

    public float getAttack() {
        //Temporary, Hook with Config
        return 6;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }
    private PriorityQueue<TimedEvent> events = new PriorityQueue<TimedEvent>();

    private static float regenStartTimer = 20;

    private Vec3d initialPosition = null;

    public static class TimedEvent implements Comparable<TimedEvent> {
        Runnable callback;
        int ticks;

        public TimedEvent(Runnable callback, int ticks) {
            this.callback = callback;
            this.ticks = ticks;
        }

        @Override
        public int compareTo(TimedEvent event) {
            return event.ticks < ticks ? 1 : -1;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0);

    }
    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new MobGroundNavigate(this, worldIn);
    }

    @SideOnly(Side.CLIENT)
    protected void initAnimation() {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    //Is Not Immovable check
            super.move(type, x, y, z);

    }

    @Override
    public void onLivingUpdate() {

        if (!isDead && this.getHealth() > 0) {
            boolean foundEvent = true;
            while (foundEvent) {
                TimedEvent event = events.peek();
                if (event != null && event.ticks <= this.ticksExisted) {
                    events.remove();
                    event.callback.run();
                } else {
                    foundEvent = false;
                }
            }
        }


        if (!world.isRemote) {
            if (this.getAttackTarget() == null) {
                if (this.regenTimer > this.regenStartTimer) {
                    if (this.ticksExisted % 20 == 0) {
                        this.heal(this.getMaxHealth() * 0.015f);
                    }
                } else {
                    this.regenTimer++;
                }
            } else {
                this.regenTimer = 0;
            }
        }
        super.onLivingUpdate();
    }

    public void doRender(RenderManager renderManager, double x, double y, double z, float entityYaw, float partialTicks) {
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
    }

    /**
     * Adds an event to be executed at a later time. Negative ticks are executed immediately.
     *
     * @param runnable
     * @param ticksFromNow
     */
    public void addEvent(Runnable runnable, int ticksFromNow) {
        events.add(new TimedEvent(runnable, this.ticksExisted + ticksFromNow));
    }

}
