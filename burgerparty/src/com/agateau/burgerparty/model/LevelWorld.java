package com.agateau.burgerparty.model;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class LevelWorld {
	public LevelWorld(String dirName) {
		mDirName = dirName;

		for (int n=1;; n++) {
			String name = dirName + "/" + n + ".xml";
			FileHandle levelFile = Gdx.files.internal(name);
			if (!levelFile.exists()) {
				break;
			}
			Gdx.app.log("LevelWorld", "levelFile=" + levelFile);
			mLevels.add(Level.fromXml(this, levelFile));
		}
	}

	public void checkNewItems(Set<String> knownItems) {
		for(Level level: mLevels) {
			level.checkNewItems(knownItems);
		}
	}

	public String getDirName() {
		return mDirName;
	}

	public Level getLevel(int index) {
		return mLevels.get(index);
	}

	public int getLevelCount() {
		return mLevels.size;
	}

	private String mDirName;
	private Array<Level> mLevels = new Array<Level>();
}
