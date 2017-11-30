package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.AbsoluteIncognito;
import com.superRainbowNinja.aincog.common.capabilites.ICoreContainer;
import com.superRainbowNinja.aincog.common.containers.CoreContainer;
import com.superRainbowNinja.aincog.common.network.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by SuperRainbowNinja on 27/11/2017.
 */
public class CoreContainerItem extends AIItemBase{

    public CoreContainerItem(String name) {
        super(name);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    /*----------------------------
    |  Public Methods            |
    ----------------------------*/

    public static ItemStack getCore(ItemStack weapon) {
        return ICoreContainer.getCap(weapon).getCoreItemStack();
    }

    public boolean trySetCore(ItemStack item, ItemStack core) {
        ICoreContainer cap = ICoreContainer.getCap(item);
        if (cap.trySetCore(core, false)) {
            item.setItemDamage(cap.getCoreDamage());
            //System.out.println("set core");
            return true;
        }
        return false;
    }

    public void loseCore(ItemStack weapon) {
        weapon.setItemDamage(0);
        ICoreContainer.getCap(weapon).loseCore();
    }

    public boolean hasCore(ItemStack stack) {
        return ICoreContainer.hasCore(stack);
    }

    public void updateCoreFromContainer(EntityPlayer player, ItemStack weapon, CoreContainer container) {
        if (container.isDirty() && !player.getEntityWorld().isRemote) {
            ICoreContainer cap = ICoreContainer.getCap(weapon);
            ItemStack core = container.getSlot(0).getStack();
            if (core == null) {
                if (cap.hasCore()) {
                    loseCore(weapon);
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
                            //PacketHandler.sendTo((EntityPlayerMP) player, new InventoryUpdate(weapon, container.getItemSlot()));
                        }
                    }
                } else {
                    if (!trySetCore(weapon, core)) {
                        //not a valid core in slot
                        container.closeInv(true);
                    } else {
                        //System.out.println("Send packet");
                        //PacketHandler.sendTo((EntityPlayerMP) player, new InventoryUpdate(weapon, container.getItemSlot()));
                    }
                }
            }
        }
        container.itemUpdated();
    }

    //set for both core and sword, maybe make defualt imp do this as well??
    public void damage(ItemStack stack, int dmg, ICoreContainer cap) {
        super.setDamage(stack, cap.getCoreDamage() + dmg);
        if (cap.setCoreDamage(cap.getCoreDamage() + dmg)) {
            loseCore(stack);
        }
    }

    /*----------------------------
    |  Internal Methods          |
    ----------------------------*/

    //set for both core and sword, maybe make defualt imp do this as well??
    protected void setDamage(ItemStack stack, int dmg, ICoreContainer cap) {
        super.setDamage(stack, dmg);
        if (cap.setCoreDamage(dmg)) {
            loseCore(stack);
        }
    }

    protected void openGui(EntityPlayer playerIn, World worldIn) {
        playerIn.openGui(AbsoluteIncognito.instance, GuiHandler.WEAPON_GUI, worldIn, 0, 0, 0);
    }

    private void setCore(ItemStack weapon, ItemStack core) {
        setCore(weapon, core, ICoreContainer.getCap(weapon));
    }

    private void setCore(ItemStack weapon, ItemStack core, ICoreContainer cap) {
        cap.setCore(core);
        weapon.setItemDamage(cap.getCoreDamage());
    }

    /*----------------------------
    |  Override Methods          |
    ----------------------------*/

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ICoreContainer.getCap(stack).getCoreMaxDamage();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)  {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) entityIn);
            if (player.openContainer instanceof CoreContainer && ((CoreContainer) player.openContainer).getItemSlot() == itemSlot) {
                updateCoreFromContainer(player, stack, ((CoreContainer) player.openContainer));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                openGui(playerIn, worldIn);
            }

        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }
}
