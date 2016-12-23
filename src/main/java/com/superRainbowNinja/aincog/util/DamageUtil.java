package com.superRainbowNinja.aincog.util;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

/**
 * Created by SuperRainbowNinja on 5/12/2016.
 */
public class DamageUtil {
    private DamageUtil() {}

    public static void swingSwordParticlesDamage(EntityPlayer player, EntityLivingBase primaryEntity, float mainDmg, float dmgMod) {
        /* stolen strait from mc source code :P
        * line 1410 of EntityPlayer (everything containted within the if (flag3) {})
        * flag3 is set 2 true when the item is a sword
        */

        for (EntityLivingBase entitylivingbase : player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, primaryEntity.getEntityBoundingBox().expand(1.0D, 0.25D, 1.0D))) {
            if (entitylivingbase != player && !player.isOnSameTeam(entitylivingbase) && (player.getDistanceSqToEntity(entitylivingbase) < 9.0D || entitylivingbase == primaryEntity)) {
                DamageUtil.playerAttack(player, entitylivingbase, mainDmg * (entitylivingbase == primaryEntity ? 1f : dmgMod));
            }
        }
        doSwordSwingParticlesSound(player);
    }

    public static void doSwordSwingParticlesSound(EntityPlayer player) {
        player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
        player.spawnSweepParticles();
    }

    public static void playerAttack(EntityPlayer player, EntityLivingBase entityLivingBase, float damage) {
        entityLivingBase.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
        entityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
    }
}
