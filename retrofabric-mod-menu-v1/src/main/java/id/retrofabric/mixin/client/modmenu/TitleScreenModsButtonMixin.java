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

package id.retrofabric.mixin.client.modmenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

import id.retrofabric.impl.client.modmenu.ModsScreen;

/**
 * Adds a "Mods" button to the top-right corner of the title screen.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenModsButtonMixin extends Screen {
	/** Button id chosen to avoid vanilla title screen ids (0, 1, 2, 4, 5, 14). */
	private static final int MODS_BUTTON_ID = 871;

	@Inject(method = "initWidgetsNormal(II)V", at = @At("TAIL"))
	private void retrofabric$addModsButton(int mouseX, int mouseY, CallbackInfo ci) {
		this.buttons.add(new ButtonWidget(MODS_BUTTON_ID, this.width - 104, 4, 100, 20, "Mods"));
	}

	@Inject(method = "buttonClicked(Lnet/minecraft/client/gui/widget/ButtonWidget;)V", at = @At("HEAD"))
	private void retrofabric$onModsButton(ButtonWidget button, CallbackInfo ci) {
		if (button.id == MODS_BUTTON_ID) {
			this.client.openScreen(new ModsScreen((TitleScreen) (Object) this));
		}
	}
}
