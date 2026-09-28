/*
 * Copyright (c) 2026 RetroFabric
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

package com.example.retrexample

import net.fabricmc.api.ModInitializer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import id.retrofabric.api.block.loot.v1.BlockDropCountEvents
import id.retrofabric.api.entity.loot.v1.LivingEntityDropEvents
import id.retrofabric.api.recipe.v1.RecipeHelper
import id.retrofabric.api.registry.v1.RetroRegistry

/**
 * Example mod showing the RetroFabric 1.7.10 APIs from Kotlin.
 *
 * <p>Copy this module as a starting point for your own Kotlin mod.
 * Players need fabric-language-kotlin 1.7.x installed (see fabric.mod.json).
 */
class RetroExampleModKotlin : ModInitializer {
	companion object {
		lateinit var ruby: Item
	}

	override fun onInitialize() {
		// 1. Registry: numeric ids are picked automatically.
		ruby = RetroRegistry.registerItem("retrexample", "ruby_k", Item().setTranslationKey("ruby_k"))

		// 2. Recipes.
		RecipeHelper.addShapedRecipe(
			ItemStack(ruby),
			" D ",
			"D D",
			" D ",
			'D', Items.DIAMOND
		)
		RecipeHelper.addShapelessRecipe(ItemStack(Items.APPLE), ruby, ruby)

		// 3. Entity loot: mobs sometimes drop an extra apple.
		LivingEntityDropEvents.AFTER_DROP.register { entity, _, looting ->
			if (entity.getRandom().nextFloat() < 0.05f + looting * 0.02f) {
				entity.dropItem(Items.APPLE, 1)
			}
		}

		// 4. Block loot: double every block drop (silly, but shows the hook).
		BlockDropCountEvents.MODIFY_COUNT.register { _, _, count -> count * 2 }
	}
}
