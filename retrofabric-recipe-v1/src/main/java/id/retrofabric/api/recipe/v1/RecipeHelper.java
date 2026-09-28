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

package id.retrofabric.api.recipe.v1;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipeType;
import net.minecraft.recipe.SmeltingRecipeRegistry;

import id.retrofabric.mixin.recipe.RecipeDispatcherAccessor;

/**
 * Helpers for adding and removing crafting and smelting recipes on 1.7.10.
 *
 * <p>Call these during mod init, after vanilla recipes exist.
 */
public final class RecipeHelper {
	private RecipeHelper() {
	}

	/**
	 * Adds a shaped crafting recipe.
	 *
	 * @param output the recipe result
	 * @param pattern rows and ingredient mappings, same as vanilla
	 * @return the created recipe
	 */
	public static ShapedRecipeType addShapedRecipe(ItemStack output, Object... pattern) {
		return ((RecipeDispatcherAccessor) RecipeDispatcher.getInstance()).callRegisterShapedRecipe(output, pattern);
	}

	/**
	 * Adds a shapeless crafting recipe.
	 *
	 * @param output the recipe result
	 * @param ingredients items, blocks or stacks
	 */
	public static void addShapelessRecipe(ItemStack output, Object... ingredients) {
		((RecipeDispatcherAccessor) RecipeDispatcher.getInstance()).callRegisterShapelessRecipe(output, ingredients);
	}

	/**
	 * Removes every crafting recipe that outputs the given item (any damage value).
	 *
	 * @param item the output item to remove recipes for
	 * @return how many recipes were removed
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int removeRecipesByItem(Item item) {
		int removed = 0;
		Iterator iterator = RecipeDispatcher.getInstance().getAllRecipes().iterator();

		while (iterator.hasNext()) {
			Object recipe = iterator.next();

			if (recipe instanceof RecipeType && ((RecipeType) recipe).getOutput().getItem() == item) {
				iterator.remove();
				removed++;
			}
		}

		return removed;
	}

	/**
	 * Removes every crafting recipe with exactly the given output stack
	 * (same item and damage value).
	 *
	 * @param output the exact output stack to remove recipes for
	 * @return how many recipes were removed
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int removeRecipesByOutput(ItemStack output) {
		int removed = 0;
		Iterator iterator = RecipeDispatcher.getInstance().getAllRecipes().iterator();

		while (iterator.hasNext()) {
			Object recipe = iterator.next();

			if (recipe instanceof RecipeType) {
				ItemStack result = ((RecipeType) recipe).getOutput();

				if (result.getItem() == output.getItem() && result.getDamage() == output.getDamage()) {
					iterator.remove();
					removed++;
				}
			}
		}

		return removed;
	}

	/**
	 * Adds a furnace recipe.
	 *
	 * @param input the smelted item
	 * @param output the result
	 * @param experience experience granted
	 */
	public static void addSmelting(Item input, ItemStack output, float experience) {
		SmeltingRecipeRegistry.getInstance().addItem(input, output, experience);
	}

	/**
	 * Adds a furnace recipe.
	 *
	 * @param input the smelted block
	 * @param output the result
	 * @param experience experience granted
	 */
	public static void addSmelting(Block input, ItemStack output, float experience) {
		SmeltingRecipeRegistry.getInstance().addBlock(input, output, experience);
	}

	/**
	 * Adds a furnace recipe.
	 *
	 * @param input the exact smelted stack
	 * @param output the result
	 * @param experience experience granted
	 */
	public static void addSmelting(ItemStack input, ItemStack output, float experience) {
		SmeltingRecipeRegistry.getInstance().addItemStack(input, output, experience);
	}

	/**
	 * Removes every furnace recipe whose input stack holds the given item.
	 *
	 * @param input the input item to remove recipes for
	 * @return how many recipes were removed
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int removeSmeltingByItem(Item input) {
		int removed = 0;
		Map recipes = SmeltingRecipeRegistry.getInstance().getRecipeMap();
		Iterator iterator = recipes.keySet().iterator();

		while (iterator.hasNext()) {
			Object key = iterator.next();

			if (key instanceof ItemStack && ((ItemStack) key).getItem() == input) {
				iterator.remove();
				removed++;
			}
		}

		return removed;
	}
}
