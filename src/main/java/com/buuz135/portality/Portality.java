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

import com.buuz135.portality.block.ControllerBlock;
import com.buuz135.portality.block.FrameBlock;
import com.buuz135.portality.block.GeneratorBlock;
import com.buuz135.portality.block.InterdimensionalModuleBlock;
import com.buuz135.portality.block.module.CapabilityEnergyModuleBlock;
import com.buuz135.portality.block.module.CapabilityFluidModuleBlock;
import com.buuz135.portality.block.module.CapabilityItemModuleBlock;
import com.buuz135.portality.item.TeleportationTokenItem;
import com.buuz135.portality.network.*;
import com.buuz135.portality.proxy.CommonProxy;
import com.buuz135.portality.proxy.PortalityConfig;
import com.buuz135.portality.proxy.PortalitySoundHandler;
import com.buuz135.portality.tile.BasicFrameTile;
import com.buuz135.portality.tile.ControllerTile;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardGiver;
import com.hrznstudio.titanium.reward.RewardManager;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class Portality extends ModuleController {

    public static final String MOD_ID = "portality";
    public static NetworkHandler NETWORK = new NetworkHandler(MOD_ID);
    public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "main"), () -> new ItemStack(CommonProxy.BLOCK_CONTROLLER.getLeft()));

    public static CommonProxy proxy;

    public Portality() {
        super(MOD_ID);
        proxy = new CommonProxy();

    }

    private static InteractionResult onRightClick(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if (world.isClientSide() || !player.isCrouching()) return InteractionResult.PASS;
        BlockEntity entity = world.getBlockEntity(hitResult.getBlockPos());
        if (entity instanceof ControllerTile controllerTile) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty()) {
                if (!stack.sameItem(controllerTile.getDisplay())) {
                    if (stack.getItem() instanceof TeleportationTokenItem) {
                        if (stack.hasTag()) {
                            controllerTile.addTeleportationToken(stack);
                            player.displayClientMessage(new TranslatableComponent("portility.controller.info.added_token").withStyle(ChatFormatting.GREEN), true);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    player.displayClientMessage(new TranslatableComponent("portility.controller.info.icon_changed").withStyle(ChatFormatting.GREEN), true);
                    controllerTile.setDisplayNameEnabled(stack);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onPreInit() {
        NETWORK.registerMessage(PortalPrivacyToggleMessage.class);
        NETWORK.registerMessage(PortalPrivacyToggleMessage.class);
        NETWORK.registerMessage(PortalRenameMessage.class);
        NETWORK.registerMessage(PortalNetworkMessage.Response.class);
        NETWORK.registerMessage(PortalLinkMessage.class);
        NETWORK.registerMessage(PortalCloseMessage.class);
        NETWORK.registerMessage(PortalTeleportMessage.class);
        NETWORK.registerMessage(PortalDisplayToggleMessage.class);
        NETWORK.registerMessage(PortalChangeColorMessage.class);
        UseBlockCallback.EVENT.register(Portality::onRightClick);
        PortalityConfig.init();
        RewardGiver giver = RewardManager.get().getGiver(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a6"), "Buuz135");
        try {
            giver.addReward(new Reward(new ResourceLocation(Portality.MOD_ID, "aura"), new URL("https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json"), () -> dist -> {
            }, Arrays.stream(AuraType.values()).map(Enum::toString).collect(Collectors.toList()).toArray(new String[]{})));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initModules() {
        CommonProxy.BLOCK_CONTROLLER = getRegistries().registerBlockWithTile("controller", ControllerBlock::new);
        CommonProxy.BLOCK_FRAME = getRegistries().registerBlockWithTile( "frame", () -> new FrameBlock<BasicFrameTile>("frame", BasicFrameTile.class));
        CommonProxy.BLOCK_CAPABILITY_ENERGY_MODULE = getRegistries().registerBlockWithTile("module_energy", CapabilityEnergyModuleBlock::new);
        CommonProxy.BLOCK_CAPABILITY_FLUID_MODULE = getRegistries().registerBlockWithTile("module_fluids", CapabilityFluidModuleBlock::new);
        CommonProxy.BLOCK_CAPABILITY_ITEM_MODULE = getRegistries().registerBlockWithTile("module_items", CapabilityItemModuleBlock::new);
        CommonProxy.BLOCK_INTERDIMENSIONAL_MODULE = getRegistries().registerBlockWithTile("module_interdimensional", InterdimensionalModuleBlock::new);
        CommonProxy.BLOCK_GENERATOR = getRegistries().registerBlockWithTile("generator", GeneratorBlock::new);
        CommonProxy.TELEPORTATION_TOKEN_ITEM = getRegistries().registerGeneric(Registry.ITEM, "teleportation_token", TeleportationTokenItem::new);
        PortalitySoundHandler.init();
        proxy.onCommon();
    }

    public enum AuraType {
        PORTAL(new ResourceLocation(Portality.MOD_ID, "textures/blocks/player_render.png"), true),
        FORCE_FIELD(new ResourceLocation("textures/misc/forcefield.png"), true),
        UNDERWATER(new ResourceLocation("textures/misc/underwater.png"), true),
        SPOOK(new ResourceLocation("textures/misc/pumpkinblur.png"), false),
        END(new ResourceLocation("textures/environment/end_sky.png"), true),
        CLOUDS(new ResourceLocation("textures/environment/clouds.png"), true),
        RAIN(new ResourceLocation("textures/environment/rain.png"), true),
        SGA(new ResourceLocation("textures/font/ascii_sga.png"), true),
        ENCHANTED(new ResourceLocation("textures/misc/enchanted_item_glint.png"), true),
        BARS(new ResourceLocation("textures/gui/bars.png"), true),
        RECIPE_BOOK(new ResourceLocation("textures/gui/recipe_book.png"), true),
        END_PORTAL(new ResourceLocation("textures/entity/end_portal.png"), true),
        MOON(new ResourceLocation("textures/environment/moon_phases.png"), true);

        private final ResourceLocation resourceLocation;
        private final boolean enableBlend;

        AuraType(ResourceLocation resourceLocation, boolean enableBlend) {
            this.resourceLocation = resourceLocation;
            this.enableBlend = enableBlend;
        }

        public ResourceLocation getResourceLocation() {
            return resourceLocation;
        }

        public boolean isEnableBlend() {
            return enableBlend;
        }
    }

}
