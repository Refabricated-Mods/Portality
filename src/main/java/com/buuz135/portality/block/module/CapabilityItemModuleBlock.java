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

import com.buuz135.portality.tile.ItemModuleTile;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;
import java.util.List;

public class CapabilityItemModuleBlock extends CapabilityModuleBlock<Storage<ItemVariant>, ItemModuleTile> {

    public CapabilityItemModuleBlock() {
        super("module_items", ItemModuleTile.class);
    }

    @Override
    public BlockApiLookup<Storage<ItemVariant>, Direction> getCapability() {
        return ItemStorage.SIDED;
    }

    @Override
    void internalWork(Level current, BlockPos myself, Level otherWorld, List<BlockPos> compatibleBlockPos) {
        Storage<ItemVariant> handlerSelf = this.getCapability().find(current, myself, Direction.UP);
        if (handlerSelf != null){
            outer:
            for (BlockPos otherPos : compatibleBlockPos) {
                Storage<ItemVariant> handlerOther = this.getCapability().find(otherWorld, otherPos, Direction.UP);
                if (handlerOther != null){
                    Transaction transaction = Transaction.openOuter();
                    var it = handlerSelf.iterable(transaction);
                    for (StorageView<ItemVariant> s : it){
                        if (!s.isResourceBlank()){
                            if (handlerOther.supportsInsertion()){
                                long insert = handlerOther.simulateInsert(s.getResource(), s.getAmount(), transaction);
                                if (insert > 0){
                                    handlerOther.insert(s.getResource(), insert, transaction);
                                    handlerSelf.extract(s.getResource(), insert, transaction);
                                    transaction.commit();
                                    continue outer;
                                }
                            }
                        }
                    }
                    transaction.abort();
                }
            }
        }
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return ItemModuleTile::new;
    }

}
