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

import net.minecraft.world.biome.Biome;

/**
 * Helpers for registering custom biomes on 1.7.10.
 *
 * <p>1.7.10 has 256 biome slots. Custom biome classes extend
 * {@link Biome} and are registered here during mod init.
 */
public final class BiomeRegistry {
	/** First biome id available to mods. Vanilla uses the low range. */
	public static final int BIOME_ID_MIN = 40;
	/** Last biome id available to mods. */
	public static final int BIOME_ID_MAX = 255;

	private BiomeRegistry() {
	}

	/**
	 * Registers a biome under the given id.
	 *
	 * @param id the numeric biome id
	 * @param biome the biome to register
	 * @return the registered biome
	 * @throws IllegalArgumentException if the id is out of range or taken
	 */
	public static Biome register(int id, Biome biome) {
		if (id < BIOME_ID_MIN || id > BIOME_ID_MAX) {
			throw new IllegalArgumentException("Biome id " + id + " is out of range [" + BIOME_ID_MIN + ", " + BIOME_ID_MAX + "]!");
		}

		if (!isIdFree(id)) {
			throw new IllegalArgumentException("Biome id " + id + " is already taken!");
		}

		// getBiomes() returns the live array, so writing registers the biome.
		Biome.getBiomes()[id] = biome;
		Biome.BIOMESET.add(biome);
		return biome;
	}

	/**
	 * Registers a biome under the first free id.
	 *
	 * @param biome the biome to register
	 * @return the id it was registered under
	 * @throws IllegalStateException if no id is free
	 */
	public static int register(Biome biome) {
		int id = nextFreeId();
		register(id, biome);
		return id;
	}

	/**
	 * Finds the first free biome id.
	 *
	 * @return a free biome id
	 * @throws IllegalStateException if no id is free
	 */
	public static int nextFreeId() {
		for (int i = BIOME_ID_MIN; i <= BIOME_ID_MAX; i++) {
			if (isIdFree(i)) {
				return i;
			}
		}

		throw new IllegalStateException("No free biome ids left!");
	}

	/**
	 * Checks whether a biome id is free.
	 *
	 * @param id the numeric biome id
	 * @return true if nothing is registered under the id
	 */
	public static boolean isIdFree(int id) {
		return Biome.getBiomes()[id] == null;
	}

	/**
	 * Looks up a biome by id.
	 *
	 * @param id the numeric biome id
	 * @return the biome, or null if the id is free
	 */
	public static Biome get(int id) {
		return Biome.getBiomes()[id];
	}
}
