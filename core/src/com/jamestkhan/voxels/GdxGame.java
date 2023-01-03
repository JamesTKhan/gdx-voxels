package com.jamestkhan.voxels;

import com.badlogic.gdx.Game;
import com.jamestkhan.voxels.screen.VoxelGame;

public class GdxGame extends Game {
	
	@Override
	public void create () {
		setScreen(new VoxelGame());
	}

	@Override
	public void render() {
		super.render();
	}
}
