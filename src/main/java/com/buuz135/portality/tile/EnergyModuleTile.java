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
package com.buuz135.portality.tile;


import com.buuz135.portality.proxy.CommonProxy;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class EnergyModuleTile extends ModuleTile<EnergyModuleTile> {

    @Save
    private final EnergyStorageComponent<EnergyModuleTile> energyStorage;

    public EnergyModuleTile(BlockPos pos, BlockState state) {
        super((BasicTileBlock<EnergyModuleTile>) CommonProxy.BLOCK_CAPABILITY_ENERGY_MODULE.getLeft(), CommonProxy.BLOCK_CAPABILITY_ENERGY_MODULE.getRight(), pos, state);
        this.energyStorage = new EnergyStorageComponent<>(10000, 10000, 10000, 10, 20);
        this.energyStorage.setComponentHarness(this.getSelf());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void initClient() {
        super.initClient();
        this.addGuiAddonFactory(() -> new EnergyBarScreenAddon(10, 20, energyStorage));
    }

    @Nonnull
    public EnergyStorageComponent<EnergyModuleTile> getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, EnergyModuleTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        if (!isInput()) {
            for (Direction facing : Direction.values()) {
                BlockPos checking = this.worldPosition.relative(facing);
                EnergyStorage storage = EnergyStorage.SIDED.find(level, checking, facing.getOpposite());
                Transaction transaction = Transaction.openOuter();
                if (storage != null) {
                    int energy = storage.insert(Math.min(this.energyStorage.getEnergyStored(), 1000), transaction);
                    if (energy > 0) {
                        this.energyStorage.extractEnergy(energy, false);
                        transaction.commit();
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public EnergyModuleTile getSelf() {
        return this;
    }
}
