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

package id.retrofabric.api.biome.v1;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.SpawnEntry;
import net.minecraft.world.biome.Biome;

/**
 * Helpers for tweaking mob spawns of any biome on 1.7.10.
 */
public final class BiomeSpawns {
	private BiomeSpawns() {
	}

	/**
	 * Adds a mob spawn entry to a biome.
	 *
	 * @param biome the biome to modify
	 * @param category which spawn list to add to
	 * @param entityClass the entity class to spawn
	 * @param weight spawn weight
	 * @param minGroupSize minimum group size
	 * @param maxGroupSize maximum group size
	 */
	@SuppressWarnings("unchecked")
	public static void addSpawn(Biome biome, EntityCategory category, Class<?> entityClass, int weight, int minGroupSize, int maxGroupSize) {
		List<SpawnEntry> entries = (List<SpawnEntry>) (List<?>) biome.getSpawnEntries(category);
		entries.add(new SpawnEntry(entityClass, weight, minGroupSize, maxGroupSize));
	}

	/**
	 * Removes every spawn entry of the given entity class from a biome.
	 *
	 * @param biome the biome to modify
	 * @param category which spawn list to clean
	 * @param entityClass the entity class to remove
	 * @return how many entries were removed
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int removeSpawns(Biome biome, EntityCategory category, Class<?> entityClass) {
		int removed = 0;
		List entries = biome.getSpawnEntries(category);
		Iterator iterator = entries.iterator();

		while (iterator.hasNext()) {
			Object entry = iterator.next();

			if (entry instanceof SpawnEntry && ((SpawnEntry) entry).type == entityClass) {
				iterator.remove();
				removed++;
			}
		}

		return removed;
	}
}
