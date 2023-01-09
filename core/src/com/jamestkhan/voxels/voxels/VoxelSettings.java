package com.jamestkhan.voxels.voxels;

/**
 * @author JamesTKhan
 * @version January 09, 2023
 */
public class VoxelSettings {
    private int vertexSize;
    private int chunksX = 20;
    private int chunksY = 4;
    private int chunksZ = 20;
    private VoxelMode voxelMode = VoxelMode.MATERIAL_COLOR;
    private boolean usePackedColors = true;

    public int getVertexSize() {
        return vertexSize;
    }

    public void setVertexSize(int vertexSize) {
        this.vertexSize = vertexSize;
    }

    public int getChunksX() {
        return chunksX;
    }

    public void setChunksX(int chunksX) {
        this.chunksX = chunksX;
    }

    public int getChunksY() {
        return chunksY;
    }

    public void setChunksY(int chunksY) {
        this.chunksY = chunksY;
    }

    public int getChunksZ() {
        return chunksZ;
    }

    public void setChunksZ(int chunksZ) {
        this.chunksZ = chunksZ;
    }

    public VoxelMode getVoxelMode() {
        return voxelMode;
    }

    public void setVoxelMode(VoxelMode voxelMode) {
        this.voxelMode = voxelMode;
    }

    public boolean isUsePackedColors() {
        return usePackedColors;
    }

    public void setUsePackedColors(boolean usePackedColors) {
        this.usePackedColors = usePackedColors;
    }
}

