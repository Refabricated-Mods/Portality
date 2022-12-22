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
package com.buuz135.portality.block.module;

import com.buuz135.portality.tile.EnergyModuleTile;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class CapabilityEnergyModuleBlock extends CapabilityModuleBlock<EnergyStorage, EnergyModuleTile> {

    public CapabilityEnergyModuleBlock() {
        super("module_energy", EnergyModuleTile.class);
    }

    @Override
    public BlockApiLookup<EnergyStorage, Direction> getCapability() {
        return EnergyStorage.SIDED;
    }

    @Override
    void internalWork(Level current, BlockPos myself, Level otherWorld, List<BlockPos> compatibleBlockPos) {
        EnergyStorage storage = getCapability().find(current, myself, Direction.UP);
        if (storage != null){
            for (BlockPos pos : compatibleBlockPos) {
                EnergyStorage otherStorage = getCapability().find(otherWorld, pos, Direction.UP);
                if (otherStorage != null){
                    Transaction transaction = Transaction.openOuter();
                    long energy = otherStorage.insert(Math.min(storage.getAmount(), 5000), transaction);
                    storage.extract(energy, transaction);
                    if (energy > 0) {
                        transaction.commit();
                        return;
                    }
                    transaction.abort();
                }
            }
        }
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return EnergyModuleTile::new;
    }

}
