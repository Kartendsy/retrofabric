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

package id.retrofabric.api.registry.v1;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

/**
 * Simple registration helpers for 1.7.10.
 *
 * <p>Numeric ids are picked automatically by scanning for free slots, so mods
 * never need to hardcode ids or resolve conflicts by hand. Names must be
 * unique across all mods.
 */
public final class RetroRegistry {
	/** First block id available to mods. Vanilla uses the low range. */
	public static final int BLOCK_ID_MIN = 1;
	/** Last block id available to mods (1.7.10 has 4096 block slots). */
	public static final int BLOCK_ID_MAX = 4095;
	/** First item id available to mods. */
	public static final int ITEM_ID_MIN = 256;
	/** Last item id available to mods. */
	public static final int ITEM_ID_MAX = 31999;

	private RetroRegistry() {
	}

	/**
	 * Registers an item under the given id, picking a free numeric id.
	 *
	 * @param id namespaced id, for example {@code new Identifier("mymod", "thing")}
	 * @param item the item to register
	 * @return the registered item
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Item registerItem(Identifier id, Item item) {
		String name = id.toString();

		if (Item.REGISTRY.containsKey(name)) {
			throw new IllegalArgumentException("Item id '" + name + "' is already registered!");
		}

		// method_7327(int, String, Object) is 1.7.10's addObject(rawId, name, value).
		Item.REGISTRY.method_7327(nextFreeItemId(), name, item);
		return item;
	}

	/**
	 * Registers a block under the given id, picking a free numeric id, and
	 * registers its {@link BlockItem} automatically.
	 *
	 * @param id namespaced id, for example {@code new Identifier("mymod", "thing")}
	 * @param block the block to register
	 * @return the registered block
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Block registerBlock(Identifier id, Block block) {
		return registerBlock(id, block, true);
	}

	/**
	 * Registers a block, optionally skipping the automatic {@link BlockItem}.
	 *
	 * @param id namespaced id
	 * @param block the block to register
	 * @param withItem whether to register a {@link BlockItem} for it
	 * @return the registered block
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Block registerBlock(Identifier id, Block block, boolean withItem) {
		String name = id.toString();

		if (Block.field_7260.containsKey(name)) {
			throw new IllegalArgumentException("Block id '" + name + "' is already registered!");
		}

		Block.field_7260.method_7327(nextFreeBlockId(), name, block);

		if (withItem) {
			registerItem(id, new BlockItem(block));
		}

		return block;
	}

	/**
	 * Finds the first free item id.
	 *
	 * @return a free numeric item id
	 * @throws IllegalStateException if no id is free
	 */
	public static int nextFreeItemId() {
		for (int i = ITEM_ID_MIN; i <= ITEM_ID_MAX; i++) {
			if (Item.REGISTRY.byIndex(i) == null) {
				return i;
			}
		}

		throw new IllegalStateException("No free item ids left!");
	}

	/**
	 * Finds the first free block id.
	 *
	 * @return a free numeric block id
	 * @throws IllegalStateException if no id is free
	 */
	public static int nextFreeBlockId() {
		for (int i = BLOCK_ID_MIN; i <= BLOCK_ID_MAX; i++) {
			if (Block.field_7260.byIndex(i) == null) {
				return i;
			}
		}

		throw new IllegalStateException("No free block ids left!");
	}

	/**
	 * Looks up an item by id.
	 *
	 * @param id the namespaced id
	 * @return the item, or null if not registered
	 */
	public static Item getItem(Identifier id) {
		Object object = Item.REGISTRY.get(id.toString());
		return object instanceof Item ? (Item) object : null;
	}

	/**
	 * Looks up a block by id.
	 *
	 * @param id the namespaced id
	 * @return the block, or null if not registered
	 */
	public static Block getBlock(Identifier id) {
		Object object = Block.field_7260.get(id.toString());
		return object instanceof Block ? (Block) object : null;
	}

	/**
	 * Checks whether an item id is taken.
	 *
	 * @param id the namespaced id
	 * @return true if something is registered under the id
	 */
	public static boolean isItemRegistered(Identifier id) {
		return Item.REGISTRY.containsKey(id.toString());
	}

	/**
	 * Checks whether a block id is taken.
	 *
	 * @param id the namespaced id
	 * @return true if something is registered under the id
	 */
	public static boolean isBlockRegistered(Identifier id) {
		return Block.field_7260.containsKey(id.toString());
	}
}
