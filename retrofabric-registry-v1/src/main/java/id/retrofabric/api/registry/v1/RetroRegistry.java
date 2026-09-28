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
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * Simple registration helpers for 1.7.10.
 *
 * <p>Numeric ids are picked automatically by scanning for free slots, so mods
 * never need to hardcode ids or resolve conflicts by hand. Names must be
 * unique across all mods.
 *
 * <p>Names are plain strings ({@code "namespace:path"}) on purpose: 1.7.10's
 * {@code Identifier} class is client-only and cannot load on dedicated servers.
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
	 * Registers an item under the given name, picking a free numeric id.
	 *
	 * @param namespace the mod namespace, for example {@code "mymod"}
	 * @param path the item path, for example {@code "thing"}
	 * @param item the item to register
	 * @return the registered item
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Item registerItem(String namespace, String path, Item item) {
		return registerItem(namespace + ":" + path, item);
	}

	/**
	 * Registers an item under the given name, picking a free numeric id.
	 *
	 * @param id the full name, for example {@code "mymod:thing"}
	 * @param item the item to register
	 * @return the registered item
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Item registerItem(String id, Item item) {
		if (Item.REGISTRY.containsKey(id)) {
			throw new IllegalArgumentException("Item id '" + id + "' is already registered!");
		}

		// method_7327(int, String, Object) is 1.7.10's addObject(rawId, name, value).
		Item.REGISTRY.method_7327(nextFreeItemId(), id, item);
		return item;
	}

	/**
	 * Registers a block under the given name, picking a free numeric id, and
	 * registers its {@link BlockItem} automatically.
	 *
	 * @param namespace the mod namespace, for example {@code "mymod"}
	 * @param path the block path, for example {@code "thing"}
	 * @param block the block to register
	 * @return the registered block
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Block registerBlock(String namespace, String path, Block block) {
		return registerBlock(namespace + ":" + path, block, true);
	}

	/**
	 * Registers a block, optionally skipping the automatic {@link BlockItem}.
	 *
	 * @param id the full name, for example {@code "mymod:thing"}
	 * @param block the block to register
	 * @param withItem whether to register a {@link BlockItem} for it
	 * @return the registered block
	 * @throws IllegalArgumentException if the name is already registered or no id is free
	 */
	public static Block registerBlock(String id, Block block, boolean withItem) {
		if (Block.field_7260.containsKey(id)) {
			throw new IllegalArgumentException("Block id '" + id + "' is already registered!");
		}

		Block.field_7260.method_7327(nextFreeBlockId(), id, block);

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
	 * Looks up an item by name.
	 *
	 * @param id the full name, for example {@code "mymod:thing"}
	 * @return the item, or null if not registered
	 */
	public static Item getItem(String id) {
		Object object = Item.REGISTRY.get(id);
		return object instanceof Item ? (Item) object : null;
	}

	/**
	 * Looks up a block by name.
	 *
	 * @param id the full name, for example {@code "mymod:thing"}
	 * @return the block, or null if not registered
	 */
	public static Block getBlock(String id) {
		Object object = Block.field_7260.get(id);
		return object instanceof Block ? (Block) object : null;
	}

	/**
	 * Checks whether an item name is taken.
	 *
	 * @param id the full name
	 * @return true if something is registered under the name
	 */
	public static boolean isItemRegistered(String id) {
		return Item.REGISTRY.containsKey(id);
	}

	/**
	 * Checks whether a block name is taken.
	 *
	 * @param id the full name
	 * @return true if something is registered under the name
	 */
	public static boolean isBlockRegistered(String id) {
		return Block.field_7260.containsKey(id);
	}
}
