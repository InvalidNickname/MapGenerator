package ru.mapgenerator.generator;

import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.TileGrid;
import ru.mapgenerator.map.objects.River;
import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Megatype;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Type;

import static ru.mapgenerator.Parameters.*;

public class Generator {

    private final int height, width;
    private TileGrid tileGrid;
    private int continentSize;
    private int seed; // для возможности вывода и использования в будущем

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
        int landBudget = (int) (height * width * MathUtils.random(Parameters.LAND_PERCENT_MIN, Parameters.LAND_PERCENT_MAX));
        // количество начальных точек континентов
        int size = MathUtils.random(5, 7);
        // точек в континенте
        continentSize = landBudget / size;
        raiseTerrain(size);
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
                tileGrid.getTile(j, i).setTemperature();
    }

    private void flattenTerrain() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType().getType() == Type.OCEAN && tileSurroundedByType(Type.OCEAN, tileGrid.getTile(j, i)) <= 2) {
                    deleteTilePaths(Type.OCEAN, Type.LAND, tileGrid.getTile(j, i));
                } else if (tileGrid.getTile(j, i).getType().getType() == Type.LAND && tileSurroundedByType(Type.LAND, tileGrid.getTile(j, i)) == 2) {
                    deleteTilePaths(Type.LAND, Type.OCEAN, tileGrid.getTile(j, i));
                }
        deleteAloneTiles(Type.LAND, Type.OCEAN);
        deleteAloneTiles(Type.OCEAN, Type.LAND);
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
        // подъем тайлов земли
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType().getType() == Type.LAND) {
                    tileGrid.getTile(j, i).increaseZ(1);
                }
    }

    private void setTerrainTypes() {
        // установка побережья и океанов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.OCEAN) {
                    if (tileSurroundedByType(Type.LAND, tile) > 0) {
                        tile.setType(Type.OCEAN, Elevation.SMALL);
                    } else {
                        tile.setType(Type.OCEAN, Elevation.MEDIUM);
                    }
                    tile.setZ(0);
                }
            }
        // установка лугов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getZ() == 2 && tile.getType().getType() == Type.LAND) {
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
                    tile.setZ(1);
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
        // расстановка рек
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() != Megatype.WATER
                        && tile.getType().getMegatype() != Megatype.ICE
                        && tile.getType().getMegatype() != Megatype.DESERT) {
                    float factor = 0.0625f * (tile.getZ() - 5f) / TileGrid.getMaxZ();
                    if (MathUtils.random(0, 1) < factor) {
                        int minZ = tile.getZ() + 1, minTile = 0;
                        for (int k = 0; k < 6; k++) {
                            Tile neighbour = tileGrid.getNeighbour(k, j, i);
                            if (neighbour.getRiver() == null) {
                                if (neighbour.getZ() < minZ
                                        && neighbour.getType().getMegatype() != Megatype.WATER
                                        && neighbour.getType().getMegatype() != Megatype.ICE
                                        && neighbour.getType().getMegatype() != Megatype.DESERT) {
                                    minZ = neighbour.getZ();
                                    minTile = k;
                                }
                            } else {
                                minZ = -1;
                                break;
                            }
                        }
                        if (minZ != -1) {
                            Tile neighbour = tileGrid.getNeighbour(minTile, j, i);
                            tile.setRiver(new River(minTile));
                            continueRiver(minTile, neighbour.getX(), neighbour.getY());
                        }
                    }
                }
            }
        // удаление рек длиной 1
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getRiver() != null) {
                    int riverCount = 0;
                    for (int k = 0; k < 6; k++) {
                        if (tileGrid.getNeighbour(k, j, i).getRiver() != null) {
                            riverCount++;
                        }
                    }
                    if (riverCount == 0) {
                        tile.setRiver(null);
                    }
                }
            }
        // расстановка побережий
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() != Megatype.WATER) {
                    for (int k = 0; k < 6; k++) {
                        Tile neighbour = tileGrid.getNeighbour(k, j, i);
                        if (neighbour != null && neighbour.getType().getMegatype() == Megatype.WATER) tile.addCoast(k);
                    }
                } else if (tile.getType().getMegatype() == Megatype.WATER) {
                    for (int k = 0; k < 6; k++) {
                        Tile neighbour = tileGrid.getNeighbour(k, j, i);
                        if (neighbour != null && neighbour.getType().getMegatype() != Megatype.WATER) tile.addCoast(k);
                    }
                }
            }
    }

    private void continueRiver(int fromDst, int x, int y) {
        Tile tile = tileGrid.getTile(x, y);
        fromDst += 3;
        if (fromDst > 5) fromDst -= 6;
        int k = getRandomNeighbourWithMinimumZ(fromDst, x, y);
        if (k != -1) {
            Tile neighbour = tileGrid.getNeighbour(k, x, y);
            tile.setRiver(new River(fromDst, k));
            if (neighbour.getType().getMegatype() != Megatype.WATER
                    && neighbour.getType().getMegatype() != Megatype.ICE
                    && neighbour.getType().getMegatype() != Megatype.DESERT) {
                continueRiver(k, neighbour.getX(), neighbour.getY());
            }
        } else {
            //tile.setType(TILE_TYPE_OCEAN, Type.SMALL_ELEVATION);
        }
    }

    private int getRandomNeighbourWithMinimumZ(int fromDst, int x, int y) {
        Tile neighbour;
        int minZ = tileGrid.getTile(x, y).getZ() + 1, minTile = 0;
        for (int k = 0; k < 6; k++) {
            neighbour = tileGrid.getNeighbour(k, x, y);
            if (neighbour.getRiver() == null
                    && neighbour.getType().getMegatype() != Megatype.ICE
                    && neighbour.getType().getMegatype() != Megatype.DESERT) {
                if (neighbour.getZ() < minZ) {
                    minZ = neighbour.getZ();
                    minTile = 0;
                } else if (neighbour.getZ() == minZ) {
                    minTile++;
                }
            } else if (k != fromDst) {
                return -1;
            }
        }
        int random = MathUtils.random(1, minTile + 1);
        minTile = 0;
        for (int k = 0; k < 6; k++) {
            neighbour = tileGrid.getNeighbour(k, x, y);
            if (neighbour.getRiver() == null
                    && neighbour.getType().getMegatype() != Megatype.ICE
                    && neighbour.getType().getMegatype() != Megatype.DESERT) {
                if (neighbour.getZ() == minZ) {
                    minZ = neighbour.getZ();
                    minTile++;
                }
                if (minTile == random) {
                    return k;
                }
            }
        }
        return -1;
    }

    private void setPlains(Tile tile) {
        if (tile.getZ() > 8) {
            tile.setType(Type.PLAINS, Elevation.HIGH);
        } else if (tile.getZ() > 5) {
            tile.setType(Type.PLAINS, Elevation.MEDIUM);
        } else if (tile.getZ() > 2) {
            tile.setType(Type.PLAINS, Elevation.SMALL);
        } else {
            tile.setType(Type.PLAINS, Elevation.NO);
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
            int prevX = MathUtils.random((int) (((double) i / size) * MAP_WIDTH), (int) (((i + 1f) / size) * MAP_WIDTH));
            tileGrid.getTile(prevX, prevY).setType(Type.LAND, Elevation.NO);
            // построение континента вокруг центрального гекса
            for (int j = 0; j < continentSize; j++) {
                int destination = MathUtils.random(0, 6);
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
                    if (MathUtils.random(0.1f, 1) < factor) {
                        neighbour.increaseZ(1);
                        if (neighbour.getType().getType() != Type.LAND) {
                            neighbour.setType(Type.LAND, Elevation.NO);
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
