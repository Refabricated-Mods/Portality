/**
 * MIT License
 *
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buuz135.portality;

import com.buuz135.portality.proxy.CommonProxy;
import com.buuz135.portality.proxy.client.IPortalColor;
import com.buuz135.portality.proxy.client.render.AuraRender;
import com.buuz135.portality.proxy.client.render.TESRPortal;
import com.buuz135.portality.tile.ControllerTile;
import io.github.fabricators_of_create.porting_lib.event.client.EntityAddedLayerCallback;
import io.github.fabricators_of_create.porting_lib.event.client.TextureStitchCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PortalityClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //BlockEntityRendererRegistry.register((BlockEntityType<ControllerTile>)CommonProxy.BLOCK_CONTROLLER.getRight(), TESRPortal::new);
        EntityAddedLayerCallback.EVENT.register(((map, map1) -> {
            map1.forEach((s, entityRenderer) -> {
                if (entityRenderer instanceof PlayerRenderer renderer){
                    renderer.addLayer(new AuraRender(renderer));
                }
            });
        }));
        TextureStitchCallback.PRE.register((textureAtlas, consumer) -> {
            if (textureAtlas.location().equals(InventoryMenu.BLOCK_ATLAS)){
                //consumer.accept(TESRPortal.TEXTURE);
            }
        });
        BlockRenderLayerMap.INSTANCE.putBlock(CommonProxy.BLOCK_CONTROLLER.getLeft(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonProxy.BLOCK_FRAME.getLeft(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonProxy.BLOCK_CAPABILITY_ENERGY_MODULE.getLeft(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonProxy.BLOCK_CAPABILITY_FLUID_MODULE.getLeft(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonProxy.BLOCK_INTERDIMENSIONAL_MODULE.getLeft(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonProxy.BLOCK_CAPABILITY_ITEM_MODULE.getLeft(), RenderType.cutout());
        ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> {
            if (index == 0 && world != null && pos != null) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof IPortalColor) {
                    return ((IPortalColor) tileEntity).getColor();
                }
            }
            return -16739073;
        }, CommonProxy.BLOCK_FRAME.getLeft(), CommonProxy.BLOCK_CONTROLLER.getLeft(), CommonProxy.BLOCK_CAPABILITY_ENERGY_MODULE.getLeft(), CommonProxy.BLOCK_CAPABILITY_FLUID_MODULE.getLeft(), CommonProxy.BLOCK_CAPABILITY_ITEM_MODULE.getLeft(), CommonProxy.BLOCK_INTERDIMENSIONAL_MODULE.getLeft());
    }
}
