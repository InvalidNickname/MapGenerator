package ru.mapgenerator.generator;

import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.map.objects.TileGrid;

import java.util.concurrent.ThreadLocalRandom;

import static ru.mapgenerator.Parameters.*;

public class Generator {

    private TileGrid tileGrid;
    private int height, width;
    private int continentSize;

    public Generator(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public TileGrid generate() {
        tileGrid = new TileGrid(new Tile[height][width]);
        // общее количество клеток суши
        int landBudget = (int) (height * width * ThreadLocalRandom.current().nextDouble(Parameters.LAND_PERCENT_MIN, Parameters.LAND_PERCENT_MAX));
        // количество начальных точек континентов
        int size = MathUtils.random(5, 7);
        // точек в континенте
        continentSize = landBudget / size;
        raiseTerrain(size);
        tileGrid.setMaxHeight();
        setTemperature();
        flattenTerrain();
        setTerrainTypes();
        return tileGrid;
    }

    private void setTemperature() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                tileGrid.getTile(j, i).setTemperature();
    }

    private void flattenTerrain() {
        deleteAloneTiles();
        for (int i = 2; i < height - 3; i++)
            for (int j = 2; j < width - 3; j++)
                if (tileGrid.getTile(j, i).getType() == TILE_TYPE_WATER && tileSurroundedByType(TILE_TYPE_WATER, tileGrid.getTile(j, i)) == 2) {
                    deleteTilePaths(TILE_TYPE_WATER, TILE_TYPE_LAND, tileGrid.getTile(j, i));
                } else if (tileGrid.getTile(j, i).getType() == TILE_TYPE_LAND && tileSurroundedByType(TILE_TYPE_LAND, tileGrid.getTile(j, i)) == 2) {
                    deleteTilePaths(TILE_TYPE_LAND, TILE_TYPE_WATER, tileGrid.getTile(j, i));
                }
        deleteAloneTiles();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_LAND) {
                    tileGrid.getTile(j, i).setZ(tileGrid.getTile(j, i).getZ() + 1);
                }
    }

    private void setTerrainTypes() {
        // установка побережья
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == TILE_TYPE_WATER && tileSurroundedByType(TILE_TYPE_LAND, tileGrid.getTile(j, i)) > 0) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_COAST);
                    tileGrid.getTile(j, i).setZ(0);
                }
        // установка океанов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == TILE_TYPE_WATER) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_OCEAN);
                    tileGrid.getTile(j, i).setZ(0);
                }
        // установка лугов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getZ() == 2 && tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_LAND) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_PLAINS);
                }
        // разглаживание лугов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_LAND
                        && tileSurroundedByType(TILE_TYPE_PLAINS, tileGrid.getTile(j, i)) > 0) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_PLAINS);
                }
        // установка холмов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getZ() > 2 && tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_PLAINS) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_HILLS);
                }
        // установка гор
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getZ() > 5 && tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_HILLS) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_HIGH_HILLS);
                }
        // установка мелководья
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_LAND) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_SHALLOW_WATER);
                    tileGrid.getTile(j, i).setZ(1);
                }
        // установка пустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_PLAINS && tileGrid.getTile(j, i).getTemperature() > 28) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_DESERT);
                }
        // установка холмов в пустынях
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_HILLS && tileGrid.getTile(j, i).getTemperature() > 28) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_DESERT_HILLS);
                }
        // установка гор в пустынях
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType() == Parameters.TILE_TYPE_HIGH_HILLS && tileGrid.getTile(j, i).getTemperature() > 28) {
                    tileGrid.getTile(j, i).setType(Parameters.TILE_TYPE_DESERT_HIGH_HILLS);
                }
        // расширение пустынь
        for (int i = 1; i < height - 2; i++)
            for (int j = 1; j < width - 2; j++)
                if (tileSurroundedByType(TILE_TYPE_DESERT, tileGrid.getTile(j, i))
                        + tileSurroundedByType(TILE_TYPE_DESERT_HILLS, tileGrid.getTile(j, i))
                        + tileSurroundedByType(TILE_TYPE_DESERT_HIGH_HILLS, tileGrid.getTile(j, i)) > 4) {
                    if (tileGrid.getTile(j, i).getType() == TILE_TYPE_PLAINS)
                        tileGrid.getTile(j, i).setType(TILE_TYPE_DESERT);
                    else if (tileGrid.getTile(j, i).getType() == TILE_TYPE_HILLS)
                        tileGrid.getTile(j, i).setType(TILE_TYPE_DESERT_HILLS);
                    else if (tileGrid.getTile(j, i).getType() == TILE_TYPE_HIGH_HILLS)
                        tileGrid.getTile(j, i).setType(TILE_TYPE_DESERT_HIGH_HILLS);
                }
        // установка полупустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType() == Parameters.TILE_TYPE_DESERT &&
                        tileSurroundedByType(Parameters.TILE_TYPE_DESERT, tile) + tileSurroundedByType(Parameters.TILE_TYPE_SEMI_DESERT, tile) < 6) {
                    tile.setType(Parameters.TILE_TYPE_SEMI_DESERT);
                }
            }
    }

    // удаление гексов, не имеющих соседей того же типа
    private void deleteAloneTiles() {
        for (int i = 1; i < height - 2; i++)
            for (int j = 1; j < width - 2; j++)
                if (tileGrid.getTile(j, i).getType() == TILE_TYPE_WATER && tileSurroundedByType(TILE_TYPE_LAND, tileGrid.getTile(j, i)) == 6) {
                    tileGrid.getTile(j, i).setType(TILE_TYPE_LAND);
                } else if (tileGrid.getTile(j, i).getType() == TILE_TYPE_LAND && tileSurroundedByType(TILE_TYPE_WATER, tileGrid.getTile(j, i)) == 6) {
                    tileGrid.getTile(j, i).setType(TILE_TYPE_WATER);
                }
    }

    // рекурсивное удаление гексов, имеющих только 2 соседей того же типа
    private void deleteTilePaths(int type, int changeTo, Tile tile) {
        tileGrid.getTile(tile.getX(), tile.getY()).setType(changeTo);
        if (tileSurroundedByType(type, tile) == 2)
            for (int i = 0; i < 6; i++) {
                Tile neighbour = tileGrid.getNeighbour(i, tile.getX(), tile.getY());
                if (neighbour != null)
                    if (neighbour.getType() == type)
                        deleteTilePaths(type, changeTo, neighbour);
            }
    }

    private int tileSurroundedByType(int type, Tile tile) {
        int count = 0;
        for (int i = 0; i < 6; i++)
            if (tileGrid.getNeighbour(i, tile.getX(), tile.getY()) != null)
                if (tileGrid.getNeighbour(i, tile.getX(), tile.getY()).getType() == type)
                    count++;
        return count;
    }

    private void raiseTerrain(int size) {
        for (int i = 0; i < size; i++) {
            // определение центрального гекса континента
            int prevY = ThreadLocalRandom.current().nextInt(LAND_BORDER, height - LAND_BORDER - 1);
            // чтобы в каждой части карты было по центральной точке - убирает огромные океаны
            int prevX = ThreadLocalRandom.current().nextInt((int) (((double) i / size) * MAP_WIDTH), (int) (((double) (i + 1) / size) * MAP_WIDTH));
            tileGrid.getTile(prevX, prevY).setType(TILE_TYPE_LAND);
            // построение континента вокруг центрального гекса
            for (int j = 0; j < continentSize; j++) {
                int destination = ThreadLocalRandom.current().nextInt(0, 6);
                Tile neighbour = tileGrid.getNeighbour(destination, prevX, prevY);
                if (neighbour != null) {
                    double factor = 1;
                    if (neighbour.getX() < LAND_BORDER)
                        factor = (double) (neighbour.getX() - OBLIGATORY_LAND_BORDER) / LAND_BORDER;
                    if (neighbour.getY() < LAND_BORDER)
                        factor = Math.min(factor, (double) (neighbour.getY() - OBLIGATORY_LAND_BORDER) / LAND_BORDER);
                    if (neighbour.getX() > MAP_WIDTH - LAND_BORDER - 1)
                        factor = Math.min(factor, (double) (MAP_WIDTH - 1 - neighbour.getX() - OBLIGATORY_LAND_BORDER) / LAND_BORDER);
                    if (neighbour.getY() > MAP_HEIGHT - LAND_BORDER - 1)
                        factor = Math.min(factor, (double) (MAP_HEIGHT - 1 - neighbour.getY() - OBLIGATORY_LAND_BORDER) / LAND_BORDER);
                    if (ThreadLocalRandom.current().nextDouble(0.1, 1) < factor) {
                        neighbour.setZ(neighbour.getZ() + 1);
                        if (neighbour.getType() != TILE_TYPE_LAND) {
                            neighbour.setType(TILE_TYPE_LAND);
                        } else {
                            j--;
                        }
                    }
                    prevX = neighbour.getX();
                    prevY = neighbour.getY();
                }
            }
        }
    }
}
