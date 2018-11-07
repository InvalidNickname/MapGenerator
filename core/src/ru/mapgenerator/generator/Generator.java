package ru.mapgenerator.generator;

import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.TileGrid;
import ru.mapgenerator.map.objects.River;
import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Megatype;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Type;

import java.util.concurrent.ThreadLocalRandom;

import static ru.mapgenerator.Parameters.*;

public class Generator {

    private final int height, width;
    private TileGrid tileGrid;
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
        setObjects();
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
                if (tileGrid.getTile(j, i).getType().getType() == Type.WATER && tileSurroundedByType(Type.WATER, tileGrid.getTile(j, i)) == 2) {
                    deleteTilePaths(Type.WATER, Type.LAND, tileGrid.getTile(j, i));
                } else if (tileGrid.getTile(j, i).getType().getType() == Type.LAND && tileSurroundedByType(Type.LAND, tileGrid.getTile(j, i)) == 2) {
                    deleteTilePaths(Type.LAND, Type.WATER, tileGrid.getTile(j, i));
                }
        deleteAloneTiles();
        // эрозия
        for (int i = 2; i < height - 3; i++)
            for (int j = 2; j < width - 3; j++) {
                Tile tile = tileGrid.getTile(j, i);
                for (int k = 0; k < 6; k++) {
                    Tile neighbour = tileGrid.getNeighbour(k, j, i);
                    if (neighbour != null)
                        if (tile.getZ() - neighbour.getZ() > 3) {
                            tile.setZ(tile.getZ() - 1);
                            neighbour.setZ(neighbour.getZ() + 1);
                        }
                }
            }
        // подъем тайлов земли
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileGrid.getTile(j, i).getType().getType() == Type.LAND) {
                    tileGrid.getTile(j, i).setZ(tileGrid.getTile(j, i).getZ() + 1);
                }
    }

    private void setTerrainTypes() {
        // установка побережья
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.WATER && tileSurroundedByType(Type.LAND, tile) > 0) {
                    tile.setType(Type.OCEAN, Elevation.SMALL);
                    tile.setZ(0);
                }
            }
        // установка океанов
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.WATER) {
                    tile.setType(Type.OCEAN, Elevation.MEDIUM);
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
                    tile.setType(Type.DESERT, tile.getType().getTerrain());
                }
            }
        // расширение пустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tileSurroundedByMegatype(Megatype.DESERT, tile) > 4 && tile.getType().getType() != Type.OCEAN) {
                    tile.setType(Type.DESERT, tile.getType().getTerrain());
                }
            }
        // установка полупустынь
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tileSurroundedByMegatype(Megatype.PLAINS, tile) > 0 && tile.getType().getType() == Type.DESERT) {
                    tile.setType(Type.SEMI_DESERT, tile.getType().getTerrain());
                }
            }
        // установка джунглей
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() == Megatype.PLAINS && tile.getLatitude() >= 0 && tile.getLatitude() < MathUtils.random(25, 30)) {
                    tile.setType(Type.JUNGLE, tile.getType().getTerrain());
                }
            }
        // установка ледников и тайги
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getMegatype() != Megatype.WATER) {
                    if (tile.getTemperature() <= 0) {
                        tile.setType(Type.ICE, tile.getType().getTerrain());
                    } else if (tile.getTemperature() < 5 && tile.getLatitude() > 60) {
                        tile.setType(Type.TAIGA, tile.getType().getTerrain());
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
                    float factor = 0.125f * (tile.getZ() - 4f) / TileGrid.maxZ;
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
                            if (neighbour.getType().getMegatype() != Megatype.WATER
                                    && neighbour.getType().getMegatype() != Megatype.ICE
                                    && neighbour.getType().getMegatype() != Megatype.DESERT) {
                                continueRiver(minTile, neighbour.getX(), neighbour.getY());
                            }
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
    private void deleteAloneTiles() {
        for (int i = 1; i < height - 2; i++)
            for (int j = 1; j < width - 2; j++) {
                Tile tile = tileGrid.getTile(j, i);
                if (tile.getType().getType() == Type.WATER && tileSurroundedByType(Type.LAND, tile) == 6) {
                    tile.setType(Type.LAND, Elevation.NO);
                } else if (tile.getType().getType() == Type.LAND && tileSurroundedByType(Type.WATER, tileGrid.getTile(j, i)) == 6) {
                    tileGrid.getTile(j, i).setType(Type.WATER, Elevation.NO);
                }
            }
    }

    // рекурсивное удаление гексов, имеющих только 2 соседей того же типа
    private void deleteTilePaths(Type type, Type changeTo, Tile tile) {
        tileGrid.getTile(tile.getX(), tile.getY()).setType(changeTo, tile.getType().getTerrain());
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
            int prevY = ThreadLocalRandom.current().nextInt(LAND_BORDER, height - LAND_BORDER - 1);
            // чтобы в каждой части карты было по центральной точке - убирает огромные океаны
            int prevX = ThreadLocalRandom.current().nextInt((int) (((double) i / size) * MAP_WIDTH), (int) (((double) (i + 1) / size) * MAP_WIDTH));
            tileGrid.getTile(prevX, prevY).setType(Type.LAND, Elevation.NO);
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
