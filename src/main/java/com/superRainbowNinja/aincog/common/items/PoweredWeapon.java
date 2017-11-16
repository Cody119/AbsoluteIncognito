package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.AbsoluteIncognito;
import com.superRainbowNinja.aincog.common.containers.WeaponContainer;
import com.superRainbowNinja.aincog.common.network.GuiHandler;
import com.superRainbowNinja.aincog.common.capabilites.IPoweredWeaponCap;
import com.superRainbowNinja.aincog.common.network.InventoryUpdate;
import com.superRainbowNinja.aincog.common.network.PacketHandler;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.util.DamageUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by SuperRainbowNinja on 4/10/2016.
 *
 * Super class for powered weapons, which are weapons that:
 *  - Must be turned on to "work" (deal damage or watever they do)
 *  - Have a slot for a core which determins some of there characteristics
 *  - Drain energy while on (energy is defined by the core, may just be the cores durability)
 *
 * model change durp if not careful with "shouldCauseReequipAnimation"
 * TODO update weapon while its in a container, just add a tick handle, and listen for the container open event
 *          -This resualts in the core not updating, sometimes the stack size goes down 2 0 as well
 * TODO when reloading check if the loaded core is still a valid core (i.e. it still exsists ingame)
 *
 * TODO particles when core breaks
 *    -refer to EntityLivingBase.renderBrokenItemStack for the math, may need 2 use the sserver version of spawn particles (done in machineframetile)
 */
public class PoweredWeapon extends AIItemBase {

    private static final float MAX_DAMAGE = 7f;

    public PoweredWeapon(String name, int maxDmg) {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(maxDmg);
        setNoRepair();

        /* sorrta wanna store this somewhere as refrense, but i also dont like it here :P
        addPropertyOverride(new ResourceLocation("on"), (stack, worldIn, entityIn) -> {
            return swordIsOn(stack) ? 1.0f : 0.0f;
        });
        addPropertyOverride(new ResourceLocation("core"), (stack, worldIn, entityIn) -> {
            return hasCore(stack) ? 1.0f : 0.0f;
        });
        */
    }

    /*---------------------------
    |  Public Methods           |
    ----------------------------*/

    @Override
    public boolean getShareTag() {
        return true;
    }

    public static ItemStack getCore(ItemStack weapon) {
        return IPoweredWeaponCap.getCap(weapon).getCoreItemStack();
    }

    public boolean trySetCore(ItemStack weapon, ItemStack core) {
        IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(weapon);
        if (cap.trySetCore(core)) {
            weapon.setItemDamage(cap.getCoreDamage());
            System.out.println("set core");
            return true;
        }
        return false;
    }

    public void loseCore(ItemStack weapon) {
        weapon.setItemDamage(0);
        IPoweredWeaponCap.getCap(weapon).loseCore();
    }

    public boolean hasCore(ItemStack stack) {
        return IPoweredWeaponCap.getCap(stack).hasCore();
    }

    public boolean weaponCanActivate(ItemStack stack) {

        return getMaxDamage(stack) != 0 && getDamage(stack) < getMaxDamage(stack) && hasCore(stack);
    }

    public void updateWeaponInfo(ItemStack stack, World world) {
        IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(stack);
        if (cap.weaponIsOn()) {
            long curTime;
            int dimId = cap.getDimensionId();
            long oldTime = cap.getRecentTimeStamp();
            if (world.provider.getDimension() != dimId) { //world change
                curTime = DimensionManager.getWorld(dimId).getTotalWorldTime();
                timeDamage(stack, curTime - oldTime, cap); //possible that this isnt the time lapsed
                cap.setDimensionId(world.provider.getDimension());
                cap.setRecentTimeStamp(world.getTotalWorldTime());
            } else {
                //TODO crashes on multiplayer, getWorld returns null, nbt might be acting funny?
                //anyways probs just null test, u will lose time though
                World curDim = DimensionManager.getWorld(dimId);
                if (curDim != null) {
                    curTime = curDim.getTotalWorldTime();
                    timeDamage(stack, curTime - oldTime, cap);
                    cap.setRecentTimeStamp(curTime);
                } else {
                    System.out.println("dim id failed");
                    cap.setDimensionId(world.provider.getDimension());
                    cap.setRecentTimeStamp(world.getTotalWorldTime());
                }

            }
        }
    }

    public void updateCoreFromContainer(EntityPlayer player, ItemStack weapon, WeaponContainer container) {
        if (container.isDirty() && !player.getEntityWorld().isRemote) {
            IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(weapon);
            ItemStack core = container.getSlot(0).getStack();
            if (core == null) {
                if (cap.hasCore()) {
                    loseCore(weapon);
                    //System.out.println("Send packet");
                    //PacketHandler.sendTo((EntityPlayerMP) player, new InventoryUpdate(weapon, container.getWeaponSlot()));
                }
            } else {
                if (cap.hasCore()) {
                    ItemStack cCore = cap.getCoreItemStack();
                    if (!(ItemStack.areItemStacksEqual(cCore, core) && cCore.areCapsCompatible(core))) {
                        loseCore(weapon);
                        if (!trySetCore(weapon, core)) {
                            //setCore(weapon, cCore, cap);
                            container.closeInv(true);
                        } else {
                            //this should be safe as this is only called on the server side
                            //System.out.println("Send packet");
                            //PacketHandler.sendTo((EntityPlayerMP) player, new InventoryUpdate(weapon, container.getWeaponSlot()));
                        }
                    }
                } else {
                    if (!trySetCore(weapon, core)) {
                        //not a valid core in slot
                        container.closeInv(true);
                    } else {
                        //System.out.println("Send packet");
                        //PacketHandler.sendTo((EntityPlayerMP) player, new InventoryUpdate(weapon, container.getWeaponSlot()));
                    }
                }
            }
        }
        container.swordUpdated();
    }

    /*---------------------------
    |  Internal Methods          |
    ----------------------------*/

    private void setCore(ItemStack weapon, ItemStack core) {
        setCore(weapon, core, IPoweredWeaponCap.getCap(weapon));
    }

    private void setCore(ItemStack weapon, ItemStack core, IPoweredWeaponCap cap) {
        cap.setCore(core);
        weapon.setItemDamage(cap.getCoreDamage());
    }

    private void timeDamage(ItemStack stack, long timeLapse, IPoweredWeaponCap cap) {
        int maxDmg = getMaxDamage(stack);
        int curDmg = getDamage(stack);
        int dmg = (int)Math.ceil(((double)timeLapse / (60 * 20)) * maxDmg);
        if (curDmg + dmg >= maxDmg) {
            setDamage(stack, maxDmg, cap);
            cap.turnWeaponOff();
        } else {
            setDamage(stack, curDmg + dmg, cap);
        }
    }

    private void timeStamp(ItemStack stack, World world) {
        IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(stack);
        cap.setDimensionId(world.provider.getDimension());
        cap.setRecentTimeStamp(world.getTotalWorldTime());
    }

    //set for both core and sword, maybe make defualt imp do this as well??
    private void setDamage(ItemStack stack, int dmg, IPoweredWeaponCap cap) {
        super.setDamage(stack, dmg);
        if (cap.setCoreDamage(dmg)) {
            loseCore(stack);
        }
    }
/*
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        System.out.println("test");
        return stack.getTagCompound();
    }
*/
    /*---------------------------
    |  Override Methods          |
    ----------------------------*/

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {

        if (!player.getEntityWorld().isRemote && entity instanceof EntityLivingBase) {
            IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(stack);
            if (cap.weaponIsOn()) {
                ICore core = cap.getCoreItem();
                ItemStack coreStack = cap.getCoreItemStack();
                if (player.getCooledAttackStrength(0.5F) > 0.95) {
                    DamageUtil.swingSwordParticlesDamage(player, (EntityLivingBase) entity, MAX_DAMAGE * core.getEfficiency(coreStack), 0.5f);
                } else {
                    DamageUtil.playerAttack(player, (EntityLivingBase) entity, MAX_DAMAGE * core.getEfficiency(coreStack));
                }
                return true;
            } /*else {

                //DamageUtil.playerAttack(player, (EntityLivingBase) entity, 1f);
                return false;
            }*/

        }
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return IPoweredWeaponCap.getCap(stack).getCoreMaxDamage();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)  {
        if (!worldIn.isRemote) updateWeaponInfo(stack, worldIn);
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) entityIn);
            if (player.openContainer instanceof WeaponContainer && ((WeaponContainer) player.openContainer).getWeaponSlot() == itemSlot) {
                updateCoreFromContainer(player, stack, ((WeaponContainer) player.openContainer));
            }
        }
    }

    @Override
    public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem) {
        if (!entityItem.getEntityWorld().isRemote) {
            updateWeaponInfo(entityItem.getEntityItem(), entityItem.getEntityWorld());
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(itemStackIn);
        //the gui will not be activated when the word is in the offhand, could change but i dont think its worth it?
        if (playerIn.isSneaking() && hand == EnumHand.MAIN_HAND && !cap.weaponIsOn()) {
            if (!worldIn.isRemote) {
                updateWeaponInfo(itemStackIn, worldIn);
                //PacketHandler.sendTo((EntityPlayerMP) playerIn, new InventoryUpdate(itemStackIn, playerIn.inventory.currentItem));
                playerIn.openGui(AbsoluteIncognito.instance, GuiHandler.WEAPON_GUI, worldIn, 0, 0, 0);
            }
        } else if (weaponCanActivate(itemStackIn)) {
            if (cap.weaponIsOn()) {
                cap.turnWeaponOff();
            } else {
                timeStamp(itemStackIn, worldIn);
                cap.setState(true);
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add("Power left: " + (getMaxDamage(stack) - getDamage(stack)) + "/" + getMaxDamage(stack));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }

    //TODO sword wont reequip if core is changed (i.e. differnt type of core is equiped)
    //TODO bassically slotChanged isnt doing what it supose 2 be doing, or i got my logic wrong?
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        IPoweredWeaponCap oldCap = IPoweredWeaponCap.getCap(oldStack);
        //this cause a crash is newStack isnt a sword... which shouldnt really be suprising
        //for now the slot changed should short circute the whole thing and fix that
        IPoweredWeaponCap newCap = IPoweredWeaponCap.getCap(newStack);
        return slotChanged || oldStack.getItem() != newStack.getItem() || oldCap.hasCore() != newCap.hasCore() || oldCap.weaponIsOn() != newCap.weaponIsOn();
        //return slotChanged || !(oldStack.getItemDamage() != newStack.getItemDamage() || oldStack.equals(newStack)) || oldCap.weaponIsOn() != newCap.weaponIsOn();
    }

    @SideOnly(Side.CLIENT)
    public static class CoreColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            //should be assured that the sword has a core as the rest of the model has no tint index
            return tintIndex == 1 ? IPoweredWeaponCap.getCap(stack).getCoreItem().getPrimaryColor(stack) :
                    (tintIndex == 2) ? IPoweredWeaponCap.getCap(stack).getCoreItem().getSecondaryColor(stack) :
                            0;
        }
    }

    public static ModelResourceLocation HANDLE = new ModelResourceLocation(Reference.MOD_ID + ":" + "laser_sword", "inventory");
    public static ModelResourceLocation BLADE = new ModelResourceLocation(Reference.MOD_ID + ":" + "laser_sword_blade", "inventory");
    public static ModelResourceLocation CORE = new ModelResourceLocation(Reference.MOD_ID + ":" + "laser_sword_core", "inventory");

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        super.registerModels();
        ModelBakery.registerItemVariants(AIncogData.LASER_SWORD, BLADE, CORE);
    }

}
