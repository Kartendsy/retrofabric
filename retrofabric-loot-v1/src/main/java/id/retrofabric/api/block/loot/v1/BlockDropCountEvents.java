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

package id.retrofabric.api.block.loot.v1;

import java.util.Random;

import net.minecraft.block.Block;

import id.retrofabric.api.event.Event;
import id.retrofabric.api.event.EventFactory;

/**
 * Events for modifying how many items a block drops on 1.7.10.
 */
public final class BlockDropCountEvents {
	private BlockDropCountEvents() {
	}

	/**
	 * Chains every listener: each receives the current count and returns the new one.
	 */
	public static final Event<ModifyCount> MODIFY_COUNT = EventFactory.createArrayBacked(ModifyCount.class,
			listeners -> (block, random, currentCount) -> {
				int value = currentCount;

				for (ModifyCount listener : listeners) {
					value = listener.modify(block, random, value);
				}

				return value;
			});

	@FunctionalInterface
	public interface ModifyCount {
		/**
		 * Adjusts the drop count of a block.
		 *
		 * @param block the broken block
		 * @param random the drop random
		 * @param currentCount the count so far
		 * @return the new count (must not be negative)
		 */
		int modify(Block block, Random random, int currentCount);
	}
}
