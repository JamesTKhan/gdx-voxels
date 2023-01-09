/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.jamestkhan.voxels.voxels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class VoxelChunk {
	public final byte[] voxels;
	public final int width;
	public final int height;
	public final int depth;
	public final Vector3 offset = new Vector3();
	private final int widthTimesHeight;
	private final int topOffset;
	private final int bottomOffset;
	private final int leftOffset;
	private final int rightOffset;
	private final int frontOffset;
	private final int backOffset;
	private final VoxelSettings settings;
	private Color currentColor;
	private final Color[] colors = {Color.GREEN, Color.BROWN, Color.FOREST};

	public VoxelChunk (int width, int height, int depth, VoxelSettings settings) {
		this.voxels = new byte[width * height * depth];
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.settings = settings;
		this.topOffset = width * depth;
		this.bottomOffset = -width * depth;
		this.leftOffset = -1;
		this.rightOffset = 1;
		this.frontOffset = -width;
		this.backOffset = width;
		this.widthTimesHeight = width * height;
	}

	public byte get (int x, int y, int z) {
		if (x < 0 || x >= width) return 0;
		if (y < 0 || y >= height) return 0;
		if (z < 0 || z >= depth) return 0;
		return getFast(x, y, z);
	}

	public byte getFast (int x, int y, int z) {
		return voxels[x + z * width + y * widthTimesHeight];
	}

	public void set (int x, int y, int z, byte voxel) {
		if (x < 0 || x >= width) return;
		if (y < 0 || y >= height) return;
		if (z < 0 || z >= depth) return;
		setFast(x, y, z, voxel);
	}

	public void setFast (int x, int y, int z, byte voxel) {
		voxels[x + z * width + y * widthTimesHeight] = voxel;
	}

	/** Creates a mesh out of the chunk, returning the number of indices produced
	 * @return the number of vertices produced */
	public int calculateVertices (float[] vertices) {
		int i = 0;
		int vertexOffset = 0;
		boolean useVertexColors = settings.getVoxelMode() == VoxelMode.VERTEX_COLOR;
		for (int y = 0; y < height; y++) {
			for (int z = 0; z < depth; z++) {
				for (int x = 0; x < width; x++, i++) {
					byte voxel = voxels[i];
					if (voxel == 0) continue;

					if (useVertexColors) {
						currentColor = colors[MathUtils.random(0, colors.length - 1)];
					}

					if (y < height - 1) {
						if (voxels[i + topOffset] == 0) vertexOffset = createTop(offset, x, y, z, vertices, vertexOffset);
					} else {
						vertexOffset = createTop(offset, x, y, z, vertices, vertexOffset);
					}
					if (y > 0) {
						if (voxels[i + bottomOffset] == 0) vertexOffset = createBottom(offset, x, y, z, vertices, vertexOffset);
					} else {
						vertexOffset = createBottom(offset, x, y, z, vertices, vertexOffset);
					}
					if (x > 0) {
						if (voxels[i + leftOffset] == 0) vertexOffset = createLeft(offset, x, y, z, vertices, vertexOffset);
					} else {
						vertexOffset = createLeft(offset, x, y, z, vertices, vertexOffset);
					}
					if (x < width - 1) {
						if (voxels[i + rightOffset] == 0) vertexOffset = createRight(offset, x, y, z, vertices, vertexOffset);
					} else {
						vertexOffset = createRight(offset, x, y, z, vertices, vertexOffset);
					}
					if (z > 0) {
						if (voxels[i + frontOffset] == 0) vertexOffset = createFront(offset, x, y, z, vertices, vertexOffset);
					} else {
						vertexOffset = createFront(offset, x, y, z, vertices, vertexOffset);
					}
					if (z < depth - 1) {
						if (voxels[i + backOffset] == 0) vertexOffset = createBack(offset, x, y, z, vertices, vertexOffset);
					} else {
						vertexOffset = createBack(offset, x, y, z, vertices, vertexOffset);
					}
				}
			}
		}
		return vertexOffset / settings.getVertexSize();
	}

	public int createTop (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset) {
		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);
		return vertexOffset;
	}

	public int createBottom (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset) {
		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);
		return vertexOffset;
	}

	public int createLeft (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset) {
		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = -1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);
		return vertexOffset;
	}

	public int createRight (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset) {
		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertexOffset += addColorToVertices(vertices, vertexOffset);
		return vertexOffset;
	}

	public int createFront (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset) {
		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);
		return vertexOffset;
	}

	public int createBack (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset) {
		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y + 1;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);

		vertices[vertexOffset++] = offset.x + x + 1;
		vertices[vertexOffset++] = offset.y + y;
		vertices[vertexOffset++] = offset.z + z + 1;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = 0;
		vertices[vertexOffset++] = -1;
		vertexOffset += addColorToVertices(vertices, vertexOffset);
		return vertexOffset;
	}

	private int addColorToVertices(float[] vertices, int vertexOffset) {
		if (currentColor == null) return 0;

		if (settings.isUsePackedColors()) {
			vertices[vertexOffset] = currentColor.toFloatBits();
			return 1;
		} else {
			vertices[vertexOffset++] = currentColor.r;
			vertices[vertexOffset++] = currentColor.g;
			vertices[vertexOffset++] = currentColor.b;
			vertices[vertexOffset] = currentColor.a;
			return 4;
		}
	}
}
