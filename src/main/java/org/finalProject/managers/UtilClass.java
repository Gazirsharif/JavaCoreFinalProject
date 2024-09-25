package org.finalProject.managers;

import org.finalProject.aliveObjects.AliveObjects;
import org.finalProject.aliveObjects.AnimalObjects;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class UtilClass {
    private static int animalFertility = 3;
    private static int entityFertility = 10;
    private static int gameTic = 100;

    public static void setGameTic(int gameTic) {
        if (gameTic > 0 && gameTic < 60_000) {
            UtilClass.gameTic = gameTic;
        }

        System.out.println(UtilClass.gameTic);
    }

    public static void setAnimalFertility(int animalFertility) {
        if (animalFertility > 2 && animalFertility < 50) {
            UtilClass.animalFertility = animalFertility;
        }

        System.out.println("animalFertility = " + UtilClass.animalFertility);
    }

    public static void setEntityFertility(int entityFertility) {
        if (entityFertility > 0 && entityFertility < 100) {
            UtilClass.entityFertility = entityFertility;
            System.out.println("Значение entityFertility = " + entityFertility);

        } else {
            UtilClass.entityFertility = 10;
            System.out.println("Значение entityFertility = " + 10);
        }
    }

    public static int getGameTic() {
        return gameTic;
    }

    public static int getAnimalFertility() {
        return animalFertility;
    }

    public static int getEntityFertility() {
        return entityFertility;
    }

    /**
     * Шансы охоты
     */
    private static final Map<String, Integer> huntingTable = Map.ofEntries(
            Map.entry("🐺 🐎", 10),
            Map.entry("🐺 🦌", 15),
            Map.entry("🐺 🐇", 60),
            Map.entry("🐺 🐁", 80),
            Map.entry("🐺 🐐", 60),
            Map.entry("🐺 🐑", 70),
            Map.entry("🐺 🐗", 15),
            Map.entry("🐺 🐃", 10),
            Map.entry("🐺 🦆", 40),

            Map.entry("🐍 🦊", 15),
            Map.entry("🐍 🐇", 20),
            Map.entry("🐍 🐁", 40),
            Map.entry("🐍 🦆", 10),

            Map.entry("🦊 🐇", 70),
            Map.entry("🦊 🐁", 90),
            Map.entry("🦊 🦆", 60),
            Map.entry("🦊 🐛", 40),

            Map.entry("🐻 🐍", 80),
            Map.entry("🐻 🐎", 40),
            Map.entry("🐻 🦌", 80),
            Map.entry("🐻 🐇", 80),
            Map.entry("🐻 🐁", 90),
            Map.entry("🐻 🐐", 70),
            Map.entry("🐻 🐑", 70),
            Map.entry("🐻 🐗", 50),
            Map.entry("🐻 🐃", 20),

            Map.entry("🦅 🦊", 10),
            Map.entry("🦅 🐇", 90),
            Map.entry("🦅 🐁", 90),
            Map.entry("🦅 🦆", 80),

            Map.entry("🐁 🐛", 90),

            Map.entry("🐗 🐁", 50),
            Map.entry("🐗 🐛", 90),

            Map.entry("🦆 🐛", 90)
    );

    /**
     * Все животные
     */
    public static final List<String> allEntity = List.of(
            "🐺",
            "🐍",
            "🦊",
            "🐻",
            "🦅",

            "🐎",
            "🦌",
            "🐇",
            "🐁",
            "🐐",
            "🐑",
            "🐗",
            "🐃",
            "🦆",
            "🐛",

            "🌺"
    );

    /**
     * Охота и его шансы
     */
    public static int hunting(AnimalObjects hunter, AnimalObjects victim) {
        Integer integer = huntingTable.get(hunter + " " + victim);
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    /**
     * Вместимость островов
     */
    private static final Map<String, Integer> islandCapacity = Map.ofEntries(
            Map.entry("🐺", 30),
            Map.entry("🐍", 30),
            Map.entry("🦊", 30),
            Map.entry("🐻", 5),
            Map.entry("🦅", 20),
            Map.entry("🐎", 20),
            Map.entry("🦌", 20),
            Map.entry("🐇", 150),
            Map.entry("🐁", 500),
            Map.entry("🐐", 140),
            Map.entry("🐑", 140),
            Map.entry("🐗", 50),
            Map.entry("🐃", 10),
            Map.entry("🦆", 200),
            Map.entry("🐛", 1000),
            Map.entry("🌺", 200)
    );

    //TODO: Что же лучше? Использовать интерфейс или класс?
    public static Integer maxCapacity(Class<? extends AliveObjects> alive) {
        try {
            return islandCapacity.get(alive.getDeclaredConstructor().newInstance().toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
