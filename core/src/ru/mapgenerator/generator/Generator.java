package ru.mapgenerator.generator;

import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.TileGrid;
import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Megatype;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Type;

import static ru.mapgenerator.Parameters.LAND_BORDER;
import static ru.mapgenerator.Parameters.MAP_HEIGHT;

public class Generator {

    private final int height, width;
    private TileGrid tileGrid;
    private int continentSize;
    private int seed; // для возможности вывода и использования в будущем
    private int oceanLevel;

    public Generator(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void setSeed(int seed) {
        this.seed = seed;
        MathUtils.random.setSeed(seed);
    }

    public TileGrid generate() {
        tileGrid = new TileGrid(new Tile[height][width]);
        // общее количество клеток суши
        int landBudget = height * width;
        // количество начальных точек континентов
        int size = MathUtils.random(50, 70);
        // точек в континенте
        continentSize = landBudget / size;
        raiseTerrain(size);
        findOceanLevel();
        tileGrid.findMaxHeight();
        setTemperature();
        flattenTerrain();
        setTerrainTypes();
        setObjects();
        return tileGrid;
    }

    private void setTemperature() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                tileGrid.getTile(j, i).setTemperature(oceanLevel);
    }

    private void findOceanLevel() {
        long tileHeight = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                tileHeight += tileGrid.getTile(j, i).getZ();
        oceanLevel = (int) (tileHeight / (height * width) * 1.2);
    }

    private void flattenTerrain() {
        // эрозия
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                for (int k = 0; k < 6; k++) {
                    Tile neighbour = tileGrid.getNeighbour(k, j, i);
                    if (neighbour != null)
                        if (tile.getZ() - neighbour.getZ() > 3) {
                            tile.increaseZ(-1);
                            neighbour.increaseZ(1);
                        }
                }
            }
    }

    private void flattenContinentBorders() {
        for (int z = 0; z < 3; z++) {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    if (tileGrid.getTile(j, i).getType().getType() == Type.OCEAN && tileSurroundedByType(Type.OCEAN, tileGrid.getTile(j, i)) <= 2) {
                        deleteTilePaths(Type.OCEAN, Type.LAND, tileGrid.getTile(j, i));
                    } else if (tileGrid.getTile(j, i).getType().getType() == Type.LAND && tileSurroundedByType(Type.LAND, tileGrid.getTile(j, i)) == 2) {
                        deleteTilePaths(Type.LAND, Type.OCEAN, tileGrid.getTile(j, i));
                    }
            deleteAloneTiles(Type.LAND, Type.OCEAN);
            deleteAloneTiles(Type.OCEAN, Type.LAND);
        }
    }

    private void setTerrainTypes() {
        // установка суши по уровню океана
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getZ() >= oceanLevel) {
                    tile.setType(Type.LAND, Elevation.NO);
                }
            }
        flattenContinentBorders();
        // установка побережья
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.OCEAN) {
                    if (tileSurroundedByType(Type.LAND, tile) > 0) {
                        tile.setType(Type.OCEAN, Elevation.SMALL);
                    } else {
                        setOceanDepth(tile);
                    }
                }
            }
        // установка лугов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getZ() == oceanLevel + 2 && tile.getType().getType() == Type.LAND) {
                    setPlains(tile);
                }
            }
        // разглаживание лугов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.LAND && tileSurroundedByType(Type.PLAINS, tile) > 0) {
                    setPlains(tile);
                }
            }
        // установка мелководья
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.LAND) {
                    tile.setType(Type.OCEAN, Elevation.NO);
                }
            }
        // установка пустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() == Megatype.PLAINS && tile.getTemperature() > Parameters.DESERT_TEMPERATURE) {
                    int count = 0;
                    for (int k = 0; k < 6; k++) {
                        Tile neighbour = tileGrid.getNeighbour(k, j, i);
                        if (neighbour.getTemperature() > Parameters.DESERT_TEMPERATURE) {
                            count++;
                        }
                    }
                    if (count >= 5) {
                        tile.setType(Type.DESERT);
                    }
                }
            }
        // расширение пустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tileSurroundedByMegatype(Megatype.DESERT, tile) > 4 && tile.getType().getType() != Type.OCEAN) {
                    tile.setType(Type.DESERT);
                }
            }
        deleteAloneTiles(Type.DESERT, Type.PLAINS);
        // установка полупустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tileSurroundedByMegatype(Megatype.PLAINS, tile) > 0 && tile.getType().getType() == Type.DESERT) {
                    tile.setType(Type.SEMI_DESERT);
                }
            }
        // установка джунглей
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() == Megatype.PLAINS && tile.getLatitude() >= 0 && tile.getLatitude() < MathUtils.random(25, 30)) {
                    tile.setType(Type.JUNGLE);
                }
            }
        // установка ледников и тайги
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() != Megatype.WATER) {
                    if (tile.getTemperature() <= 0) {
                        tile.setType(Type.ICE);
                    } else if (tile.getTemperature() < 5 && tile.getLatitude() > 60) {
                        tile.setType(Type.TAIGA);
                    }
                }
            }
    }

    private void setObjects() {

    }

    private void setPlains(Tile tile) {
        if (tile.getZ() > oceanLevel + 8) {
            tile.setType(Type.PLAINS, Elevation.HIGH);
        } else if (tile.getZ() > oceanLevel + 5) {
            tile.setType(Type.PLAINS, Elevation.MEDIUM);
        } else if (tile.getZ() > oceanLevel + 2) {
            tile.setType(Type.PLAINS, Elevation.SMALL);
        } else {
            tile.setType(Type.PLAINS, Elevation.NO);
        }
    }

    private void setOceanDepth(Tile tile) {
        if (tile.getZ() < oceanLevel - 10) {
            tile.setType(Type.OCEAN, Elevation.HIGH);
        } else {
            tile.setType(Type.OCEAN, Elevation.MEDIUM);
        }
    }

    // удаление гексов, не имеющих соседей того же типа
    private void deleteAloneTiles(Type from, Type to) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == from && tileSurroundedByType(from, tile) == 0)
                    tile.setType(to);
            }
    }

    // рекурсивное удаление гексов, имеющих только 2 соседей того же типа
    private void deleteTilePaths(Type type, Type changeTo, Tile tile) {
        tileGrid.getTile(tile.getX(), tile.getY()).setType(changeTo);
        if (tileSurroundedByType(type, tile) == 2)
            for (int i = 0; i < 6; i++) {
                Tile neighbour = tileGrid.getNeighbour(i, tile.getX(), tile.getY());
                if (neighbour != null)
                    if (neighbour.getType().getType() == type)
                        deleteTilePaths(type, changeTo, neighbour);
            }
    }

    private int tileSurroundedByType(Type type, Tile tile) {
        int count = 0;
        for (int i = 0; i < 6; i++)
            if (tileGrid.getNeighbour(i, tile.getX(), tile.getY()) != null)
                if (tileGrid.getNeighbour(i, tile.getX(), tile.getY()).getType().getType() == type)
                    count++;
        return count;
    }

    private int tileSurroundedByMegatype(Megatype megatype, Tile tile) {
        int count = 0;
        for (int i = 0; i < 6; i++)
            if (tileGrid.getNeighbour(i, tile.getX(), tile.getY()) != null)
                if (tileGrid.getNeighbour(i, tile.getX(), tile.getY()).getType().getMegatype() == megatype)
                    count++;
        return count;
    }

    private void raiseTerrain(int size) {
        for (int i = 0; i < size; i++) {
            // определение центрального гекса континента
            int prevY = MathUtils.random(LAND_BORDER, height - LAND_BORDER - 1);
            // чтобы в каждой части карты было по центральной точке - убирает огромные океаны
            int prevX = MathUtils.random(0, width - 1);
            tileGrid.getTile(prevX, prevY).setType(Type.LAND, Elevation.NO);
            // построение континента вокруг центрального гекса
            for (int j = 0; j < continentSize; j++) {
                int destination = MathUtils.random(0, 6);
                Tile neighbour = tileGrid.getNeighbour(destination, prevX, prevY);
                if (neighbour != null) {
                    double factor = 1;
                    if (neighbour.getY() < LAND_BORDER)
                        factor = Math.min(factor, (double) neighbour.getY() / LAND_BORDER);
                    if (neighbour.getY() > MAP_HEIGHT - LAND_BORDER - 1)
                        factor = Math.min(factor, (double) (MAP_HEIGHT - 1 - neighbour.getY()) / LAND_BORDER);
                    if (MathUtils.random(0.1f, 1) < factor) {
                        neighbour.increaseZ(1);
                        if (neighbour.getZ() > 0) {
                            j--;
                        }
                    }
                    prevX = neighbour.getX();
                    prevY = neighbour.getY();
                } else {
                    j--;
                }
            }
        }
    }
}
