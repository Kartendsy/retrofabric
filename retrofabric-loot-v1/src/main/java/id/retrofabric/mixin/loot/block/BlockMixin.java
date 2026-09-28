/*
 * Copyright (c) 2026 RetroFabric
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.retrofabric.mixin.loot.block;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;

import id.retrofabric.api.block.loot.v1.BlockDropCountEvents;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "getDropCount(Ljava/util/Random;)I", at = @At("TAIL"), cancellable = true)
	private void retrofabric$modifyDropCount(Random random, CallbackInfoReturnable<Integer> cir) {
		int modified = BlockDropCountEvents.MODIFY_COUNT.invoker().modify((Block) (Object) this, random, cir.getReturnValue());
		cir.setReturnValue(Math.max(0, modified));
	}
}
