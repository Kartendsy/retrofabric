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

package id.retrofabric.impl.client.modmenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.fabricmc.loader.impl.FabricLoaderImpl;

/**
 * Built-in mod list screen for RetroFabric on 1.7.10.
 *
 * <p>Lists every loaded mod (id, name, version, authors, description) with
 * mouse wheel / keyboard scrolling and click selection. Client only.
 */
public class ModsScreen extends Screen {
	private static final int ROW_HEIGHT = 14;
	private static final int LIST_TOP = 40;

	private final Screen parent;
	private final List<ModEntry> mods = new ArrayList<>();
	private int selectedIndex;
	private int scrollOffset;

	public ModsScreen(Screen parent) {
		this.parent = parent;

		Collection<ModContainer> containers = FabricLoaderImpl.INSTANCE.getAllMods();

		for (ModContainer container : containers) {
			this.mods.add(new ModEntry(container.getMetadata()));
		}

		Collections.sort(this.mods, (a, b) -> a.name.toLowerCase(Locale.ROOT).compareTo(b.name.toLowerCase(Locale.ROOT)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		this.buttons.add(new ButtonWidget(0, this.width / 2 - 100, this.height - 28, 200, 20, I18n.translate("gui.done")));
	}

	@Override
	public void buttonClicked(ButtonWidget button) {
		if (button.id == 0) {
			this.client.openScreen(this.parent);
			return;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		int listBottom = this.height - 40;
		int listLeft = 40;
		int listRight = this.width - 40;

		if (mouseX >= listLeft && mouseX <= listRight && mouseY >= LIST_TOP && mouseY <= listBottom) {
			int index = this.scrollOffset + (mouseY - LIST_TOP) / ROW_HEIGHT;

			if (index >= 0 && index < this.mods.size()) {
				this.selectedIndex = index;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			this.client.openScreen(this.parent);
			return;
		}

		int maxScroll = Math.max(0, this.mods.size() * ROW_HEIGHT - (this.height - 40 - LIST_TOP));

		if (keyCode == Keyboard.KEY_UP) {
			this.selectedIndex = Math.max(0, this.selectedIndex - 1);
			this.ensureVisible();
		} else if (keyCode == Keyboard.KEY_DOWN) {
			this.selectedIndex = Math.min(this.mods.size() - 1, this.selectedIndex + 1);
			this.ensureVisible();
		} else if (keyCode == Keyboard.KEY_PRIOR) {
			this.scrollOffset = Math.max(0, this.scrollOffset - (this.height - 40 - LIST_TOP));
			this.selectedIndex = Math.min(this.mods.size() - 1, Math.max(0, this.scrollOffset / ROW_HEIGHT));
		} else if (keyCode == Keyboard.KEY_NEXT) {
			this.scrollOffset = Math.min(maxScroll, this.scrollOffset + (this.height - 40 - LIST_TOP));
			this.selectedIndex = Math.min(this.mods.size() - 1, Math.max(0, this.scrollOffset / ROW_HEIGHT));
		}
	}

	private void ensureVisible() {
		int listHeight = this.height - 40 - LIST_TOP;
		int top = this.scrollOffset / ROW_HEIGHT;
		int visibleRows = Math.max(1, listHeight / ROW_HEIGHT);

		if (this.selectedIndex < top) {
			this.scrollOffset = this.selectedIndex * ROW_HEIGHT;
		} else if (this.selectedIndex >= top + visibleRows) {
			this.scrollOffset = (this.selectedIndex - visibleRows + 1) * ROW_HEIGHT;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void render(int mouseX, int mouseY, float delta) {
		this.renderBackground();

		int wheel = Mouse.getDWheel();

		if (wheel != 0) {
			int listHeight = this.height - 40 - LIST_TOP;
			int maxScroll = Math.max(0, this.mods.size() * ROW_HEIGHT - listHeight);
			this.scrollOffset = Math.min(maxScroll, Math.max(0, this.scrollOffset - wheel / 12));
		}

		String title = "Mods (" + this.mods.size() + ")";
		this.drawCenteredString(this.textRenderer, title, this.width / 2, 12, 0xFFFFFF);

		int listBottom = this.height - 40;
		int listLeft = 40;
		int listRight = this.width - 40;
		int firstRow = this.scrollOffset / ROW_HEIGHT;
		int yOffset = -(this.scrollOffset % ROW_HEIGHT);

		for (int i = firstRow; i < this.mods.size(); i++) {
			int rowY = LIST_TOP + yOffset + (i - firstRow) * ROW_HEIGHT;

			if (rowY + ROW_HEIGHT < LIST_TOP || rowY > listBottom) {
				continue;
			}

			ModEntry entry = this.mods.get(i);
			boolean selected = i == this.selectedIndex;
			int nameColor = selected ? 0xFFFF55 : 0xFFFFFF;
			String prefix = selected ? "> " : "  ";
			String name = this.textRenderer.trimToWidth(prefix + entry.name, listRight - listLeft - 90);
			this.textRenderer.draw(name, listLeft, rowY + 2, nameColor, true);

			String version = this.textRenderer.trimToWidth(entry.version, 84);
			this.textRenderer.draw(version, listRight - this.textRenderer.getStringWidth(version), rowY + 2, 0x808080, true);
		}

		if (!this.mods.isEmpty() && this.selectedIndex >= 0 && this.selectedIndex < this.mods.size()) {
			ModEntry entry = this.mods.get(this.selectedIndex);
			String idLine = entry.id + "  |  " + entry.authors;
			this.drawCenteredString(this.textRenderer, this.textRenderer.trimToWidth(idLine, this.width - 80), this.width / 2, listBottom + 4, 0xA0A0A0);
			this.drawCenteredString(this.textRenderer, this.textRenderer.trimToWidth(entry.description, this.width - 80), this.width / 2, listBottom + 14, 0xA0A0A0);
		}

		for (Object button : this.buttons) {
			((ButtonWidget) button).render(this.client, mouseX, mouseY);
		}
	}

	private static final class ModEntry {
		final String id;
		final String name;
		final String version;
		final String description;
		final String authors;

		ModEntry(ModMetadata metadata) {
			this.id = metadata.getId();
			this.name = metadata.getName() != null ? metadata.getName() : metadata.getId();
			this.version = metadata.getVersion() != null ? metadata.getVersion().getFriendlyString() : "?";
			this.description = metadata.getDescription() != null ? metadata.getDescription().replace('\n', ' ') : "";

			StringBuilder builder = new StringBuilder();

			for (Person person : metadata.getAuthors()) {
				if (builder.length() > 0) {
					builder.append(", ");
				}

				builder.append(person.getName());
			}

			this.authors = builder.length() > 0 ? builder.toString() : "-";
		}
	}
}
