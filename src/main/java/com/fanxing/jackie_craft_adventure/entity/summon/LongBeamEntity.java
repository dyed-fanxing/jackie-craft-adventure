package com.fanxing.jackie_craft_adventure.entity.summon;

import com.fanxing.jackie_craft_adventure.registry.EntityTypes;
import com.fanxing.lib.entity.summon.Summons;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LongBeamEntity extends Summons {
    // ======================= 同步数据 =======================
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(LongBeamEntity.class, EntityDataSerializers.FLOAT);

    // ======================= 光束参数 =======================
    public static final float BEAM_ENERGY = 100.0F;
    public static final float ENERGY_PER_HARDNESS = 15.0F;
    public static final float EXPLOSION_RADIUS = 2.0F;
    public static final float BEAM_SPEED = 0.05f;       // 每tick移动格数

    private float beamEnergy = BEAM_ENERGY;
    private int lifeTicks = 0;

    // ======================= 构造 =======================
    public LongBeamEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public LongBeamEntity(Level level, Entity owner, float radius) {
        super(EntityTypes.LONG_BEAM.get(), level, owner);
        this.entityData.set(DATA_RADIUS, radius);
    }

    // ======================= 同步数据定义 =======================
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 0.5F);
    }

    // ======================= 公共读取方法 =======================
    public float getBeamRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    // ======================= tick 逻辑 =======================
    @Override
    public void tick() {
        // 客户端：解析拥有者（来自 Summons 的逻辑）
        super.tick();
        lifeTicks++;

        Vec3 velocity = this.getDeltaMovement();

        Vec3 start = this.position();
        float radius = getBeamRadius();

        // —— 服务端：步进推进检测 实体 + 方块 ——
        if (!level().isClientSide) {
            float stepSize = 0.5f;
            int steps = Math.max(1, (int) Math.ceil(BEAM_SPEED / stepSize));
            Vec3 stepVec = velocity.scale(1.0 / steps);
            boolean shouldExplode = false;
            for (int i = 0; i < steps; i++) {
                Vec3 checkPos = start.add(stepVec.scale(i + 1));
                // 实体检测
                AABB entityBox = AABB.ofSize(checkPos, radius * 2, radius * 2, radius * 2);
                List<Entity> hits = level().getEntities(this, entityBox, this::canHitEntity);
                if (!hits.isEmpty()) {
                    shouldExplode = true;
                    break;
                }
                // 方块检测 + 能量衰减
                BlockPos blockPos = BlockPos.containing(checkPos);
                BlockState blockState = level().getBlockState(blockPos);
                if (!blockState.isAir()) {
                    float hardness = blockState.getDestroySpeed(level(), blockPos);
                    if (hardness >= 0) {
                        beamEnergy -= Math.max(1.0F, hardness * ENERGY_PER_HARDNESS);
                        level().destroyBlock(blockPos, true, getOwner());
                        if (beamEnergy <= 0) {
                            shouldExplode = true;
                            break;
                        }
                    }
                }
            }
            if (shouldExplode) {
                explodeAndRemove();
                return;
            }
            // 超时判定
            if (lifeTicks >= 2000) {
                explodeAndRemove();
                return;
            }
        }
        // —— 移动（服务端 + 客户端各自积分） ——
        this.setPos(start.add(velocity));
    }

    // ======================= 爆炸 + 伤害 =======================
    private void explodeAndRemove() {
        if (level().isClientSide) return;

        level().explode(getEffectSource(), this.getX(), this.getY(), this.getZ(),
                EXPLOSION_RADIUS, Level.ExplosionInteraction.NONE);

        Entity owner = getOwner();
        if (owner instanceof LivingEntity livingOwner) {
            float damage = (float) livingOwner.getAttributeValue(Attributes.ATTACK_DAMAGE);
            AABB explosionBox = AABB.ofSize(this.position(), EXPLOSION_RADIUS * 2,
                    EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2);
            level().getEntitiesOfClass(LivingEntity.class, explosionBox,
                            e -> e.isAlive() && e != owner
                                    && !(e instanceof TraceableEntity t && t.getOwner() == owner))
                    .forEach(target -> target.hurt(
                            level().damageSources().indirectMagic(this, getEffectSource()), damage));
        }

        this.discard();
    }

    // ======================= 杂项 =======================
    @Override
    public boolean shouldRenderAtSqrDistance(double r) {
        return true;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("beamEnergy", beamEnergy);
        tag.putInt("lifeTicks", lifeTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("beamEnergy")) beamEnergy = tag.getFloat("beamEnergy");
        if (tag.contains("lifeTicks")) lifeTicks = tag.getInt("lifeTicks");
    }
}
