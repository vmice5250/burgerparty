package com.agateau.burgerparty.model;

import java.io.IOException;
import java.util.MissingResourceException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class Level {
	public static class Definition {
		public Array<String> burgerItems = new Array<String>();
		public String topBurgerItem = "top";
		public String bottomBurgerItem = "bottom";
		public Array<String> extraItems = new Array<String>();
		public int minBurgerSize;
		public int maxBurgerSize;
		public int duration;
		public Array<Objective> objectives = new Array<Objective>();
		public Array<String> customers = new Array<String>();
	}

	public Definition definition = new Definition();
	public int stars = -1;

	public LevelWorld getLevelWorld() {
		return mLevelWorld;
	}

	public static Level fromXml(LevelWorld levelWorld, FileHandle handle) {
		XmlReader reader = new XmlReader();
		XmlReader.Element root = null;
		try {
			root = reader.parse(handle);
		} catch (IOException e) {
			throw new MissingResourceException("Failed to load level from " + handle.path() + ". Exception: " + e.toString() + ".", "Level", handle.path());
		}
		if (root == null) {
			throw new MissingResourceException("Failed to load level from " + handle.path() + ". No root element.", "Level", handle.path());
		}
		Level level = new Level();
		level.mLevelWorld = levelWorld;
		level.definition.minBurgerSize = root.getIntAttribute("minBurgerSize");
		level.definition.maxBurgerSize = root.getIntAttribute("maxBurgerSize");
		level.definition.duration = root.getIntAttribute("duration");

		readObjectives(level, root.getChildByName("objectives"));

		XmlReader.Element elements = root.getChildByName("items");
		assert(elements != null);
		for(int idx = 0; idx < elements.getChildCount(); ++idx) {
			XmlReader.Element element = elements.getChild(idx);
			String name = element.getAttribute("name");
			MealItem item = MealItem.get(name);
			assert(item != null);
			if (item.getType() == MealItem.Type.BURGER) {
				BurgerItem bItem = (BurgerItem)item;
				switch (bItem.getSubType()) {
				case MIDDLE:
					level.definition.burgerItems.add(name);
					break;
				case TOP:
					level.definition.topBurgerItem = name;
					break;
				case BOTTOM:
					level.definition.bottomBurgerItem = name;
					break;
				case TOP_BOTTOM:
					level.definition.topBurgerItem = name;
					level.definition.bottomBurgerItem = name;
					break;
				}
			} else {
				level.definition.extraItems.add(name);
			}
		}

		elements = root.getChildByName("customers");
		assert(elements != null);
		for(int idx = 0; idx < elements.getChildCount(); ++idx) {
			XmlReader.Element element = elements.getChild(idx);
			String name = element.getAttribute("type");
			level.definition.customers.add(name);
		}

		return level;
	}

	private static void readObjectives(Level level, XmlReader.Element objRoot) {
		assert(objRoot != null);
		for(int idx = 0; idx < objRoot.getChildCount(); ++idx) {
			XmlReader.Element element = objRoot.getChild(idx);
			String type = element.getAttribute("type");
			Objective objective = null;
			if (type.equals("maxTrashed")) {
				int value = element.getIntAttribute("value");
				objective = new MaxTrashedObjective(value);
			} else if (type.equals("maxDuration")) {
				int value = element.getIntAttribute("value");
				objective = new MaxDurationObjective(value);
			} else if (type.equals("minHappy")) {
				int value = element.getIntAttribute("value");
				objective = new MinHappyObjective(value);
			} else {
				throw new RuntimeException("Don't know how to read objective from " + element);
			}
			assert(objective != null);
			level.definition.objectives.add(objective);
		}
	}

	private LevelWorld mLevelWorld;
}