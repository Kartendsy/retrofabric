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

package id.retrofabric.api.entity.loot.v1;

import net.minecraft.entity.LivingEntity;

import id.retrofabric.api.event.Event;
import id.retrofabric.api.event.EventFactory;

/**
 * Events for adding custom drops to living entities on 1.7.10.
 *
 * <p>Listeners run after vanilla drops and can spawn extra drops with
 * {@link LivingEntity#dropItem(net.minecraft.item.Item, int)}.
 */
public final class LivingEntityDropEvents {
	private LivingEntityDropEvents() {
	}

	/**
	 * Called after an entity drops its vanilla loot.
	 */
	public static final Event<AfterDrop> AFTER_DROP = EventFactory.createArrayBacked(AfterDrop.class,
			listeners -> (entity, recentlyHit, lootingLevel) -> {
				for (AfterDrop listener : listeners) {
					listener.onDrop(entity, recentlyHit, lootingLevel);
				}
			});

	@FunctionalInterface
	public interface AfterDrop {
		/**
		 * Adds extra drops for a dying entity.
		 *
		 * @param entity the dying entity
		 * @param recentlyHit whether a player hit it recently
		 * @param lootingLevel the looting level used
		 */
		void onDrop(LivingEntity entity, boolean recentlyHit, int lootingLevel);
	}
}
