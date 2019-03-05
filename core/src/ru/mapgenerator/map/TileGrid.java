package ru.mapgenerator.map;

import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Type;

public class TileGrid {

    private static int maxZ;
    private final Tile[][] grid;
    private final int height, width;

    public TileGrid(Tile[][] grid) {
        this.grid = grid;
        height = grid.length;
        width = grid[0].length;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                grid[i][j] = new Tile(Type.OCEAN, Elevation.MEDIUM, j, i);
    }

    public static int getMaxZ() {
        return maxZ;
    }

    public Tile getTile(int x, int y) {
        return grid[y][x];
    }

    public Tile getNeighbour(int destination, int x, int y) {
        if (y % 2 == 0) {
            switch (destination) {
                case 0:
                    if (x > 0)
                        return grid[y][x - 1];
                    else
                        return grid[y][width - 1];
                case 1:
                    if (x > 0 && y < height - 1)
                        return grid[y + 1][x - 1];
                    else if (y < height - 1)
                        return grid[y + 1][width - 1];
                case 2:
                    if (y < height - 1)
                        return grid[y + 1][x];
                case 3:
                    if (x < width - 1)
                        return grid[y][x + 1];
                    else if (x == width - 1)
                        return grid[y][0];
                case 4:
                    if (y > 0)
                        return grid[y - 1][x];
                case 5:
                    if (y > 0 && x > 0)
                        return grid[y - 1][x - 1];
                    else if (y > 0)
                        return grid[y - 1][width - 1];
            }
        } else {
            switch (destination) {
                case 0:
                    if (x > 0)
                        return grid[y][x - 1];
                    else
                        return grid[y][width - 1];
                case 1:
                    if (y < height - 1)
                        return grid[y + 1][x];
                case 2:
                    if (y < height - 1 && x < width - 1)
                        return grid[y + 1][x + 1];
                    else if (y < height - 1)
                        return grid[y + 1][0];
                case 3:
                    if (x < width - 1)
                        return grid[y][x + 1];
                    else
                        return grid[y][0];
                case 4:
                    if (y > 0 && x < width - 1)
                        return grid[y - 1][x + 1];
                    else if (y > 0)
                        return grid[y - 1][0];
                case 5:
                    if (y > 0)
                        return grid[y - 1][x];
            }
        }
        return null;
    }

    public void findMaxHeight() {
        maxZ = getTile(0, 0).getZ();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (getTile(j, i).getZ() > maxZ)
                    maxZ = getTile(j, i).getZ();
    }
}
