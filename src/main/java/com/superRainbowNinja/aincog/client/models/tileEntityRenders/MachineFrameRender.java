package com.superRainbowNinja.aincog.client.models.tileEntityRenders;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.EnumPosition;
import com.superRainbowNinja.aincog.util.ExactPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 *
 * TODO massive clean up, simplyfy drawing in the 9 cells, only draw glass when locked
 * current rendering means components should follow these guid lines
 *  - from the center (8, 8, 8) they have 2 spaces on top and 3 bellow
 *  - i.e. should be bound bettween 5 and 10 (note these numbers are cartesian, so 0 referse to the axis line, not the first line of cubes)
 */
public class MachineFrameRender extends TileEntitySpecialRenderer<MachineFrameTile> {

    private RenderEntityItem itemRender;

    public MachineFrameRender() {
        itemRender = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
            @Override
            public boolean shouldBob() {
                return false;
            }
            @Override
            public boolean shouldSpreadItems() {
                return false;
            }
        };

    }

    @Override
    public void renderTileEntityAt(MachineFrameTile teIn, double x, double y, double z, float partialTicks, int destroyStage) {

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            Minecraft minecraft = Minecraft.getMinecraft();
            RenderItem iRen = minecraft.getRenderItem();
            minecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            //render block model
            {
                World world = teIn.getWorld();
                BlockPos pos = teIn.getPos();
                BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
                IBakedModel model = dispatcher.getModelForState(world.getBlockState(pos).getActualState(world, pos));

                float energyPerc = teIn.getEnergyRatio();
                dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1, 1f, 1f - energyPerc * 0.65f, 1f - energyPerc * 0.65f);
            }

            //render componenets
            ArrayList<ItemStack> components = teIn.getComponentItems();
            ArrayList<ExactPosition> componentPositions = teIn.getComponentPositions();

            for (int i = 0; i < components.size(); i++) {
                GlStateManager.pushMatrix();
                ExactPosition pos = componentPositions.get(i);
                GlStateManager.translate(pos.getXOffset(), pos.getYOffset(), pos.getZOffset());
                int rot = pos.getRotation();
                if (rot != 0) {
                    GlStateManager.rotate(rot, 0, 1, 0);
                }
                iRen.renderItem(components.get(i), ItemCameraTransforms.TransformType.NONE);
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();

            //temp lock indicator
        /*
        if (teIn.isLocked()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            //GlStateManager.scale(1f/16f,1f/16f,1f/16f);
            /*
            VertexBuffer buf = Tessellator.getInstance().getBuffer();
            //can probs chuck a lot of this stuff in render util
            this.bindTexture(new ResourceLocation("aincog:textures/gui/guiElements/Bars.png"));
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            buf.pos(-0.05, 14D, 0D).tex(2.0 / 8, 0).color(0, 255, 0, 255).endVertex();
            buf.pos(-0.05, 14D, 2D).tex(3.0 / 8, 0).color(0, 255, 0, 255).endVertex();
            buf.pos(-0.05, 16D, 2D).tex(3.0 / 8, 1).color(0, 255, 0, 255).endVertex();
            buf.pos(-0.05, 16D, 0D).tex(2.0 / 8, 1).color(0, 255, 0, 255).endVertex();

            Tessellator.getInstance().draw();
            *
            iRen.renderItem(new ItemStack(AIncogData.MACHINE_LOCK), ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
        */

            //render core
            ItemStack coreStack = teIn.getCoreStack();
            if (coreStack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5, y + 1, z + 0.5);
                GlStateManager.scale(1f / 4, 1f / 4, 1f / 4);
                GlStateManager.rotate((teIn.getCoreAngle() + (teIn.getCoreSpeed() * partialTicks)), 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                iRen.renderItem(coreStack, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.popMatrix();
            }
            //logic render
            if (teIn.isLocked()) {
                teIn.getLogic().renderTileEntityAt(this, teIn, x, y, z, partialTicks, destroyStage);
            }

    }
}
