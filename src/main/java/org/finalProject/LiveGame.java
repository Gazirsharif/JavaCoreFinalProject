package org.finalProject;

import org.finalProject.aliveObjects.AliveObjects;
import org.finalProject.aliveObjects.Animal;
import org.finalProject.aliveObjects.Entity;
import org.finalProject.managers.UtilClass;
import org.finalProject.managers.Zookeeper;

import java.util.List;
import java.util.Scanner;

/**
 * Головной класс игры, содержит в себе все острова, просит заполнить острова животными, и начинает игру.
 * Возможно будет расширенно для работы с графикой.
 */
public class LiveGame {
    public static int WIDTH = 3;
    public static int HEIGHT = 3;
    private static int fertilityAnimal = 3;
    private static int fertilityEntity = 10;
    private static int gameTic = 100;
    private static int animalCount = 10;
    private static int timer = 60;
    private static int threadCount = 10;
    private static Scanner sc = new Scanner(System.in);

    private static final Island[][] gameField = new Island[HEIGHT][WIDTH];

    static {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                gameField[i][j] = new Island(WIDTH * i + j);
            }
        }
    }

    public static Island getIslandById(int id) {
        if (id < 0 || id > HEIGHT * WIDTH - 1) {
            throw new IllegalArgumentException("Id: " + id + " is not found");
        }

        int i = id / WIDTH;
        int j = id % WIDTH;
        return gameField[i][j];
    }

    public static void main(String[] args) {
        System.out.println("Расположение островов");
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (WIDTH * i + j < 10) {
                    System.out.print(" " + gameField[i][j].getId() + " ");
                } else {
                    System.out.print(gameField[i][j].getId() + " ");
                }
            }
            System.out.println();
        }
        System.out.println();

        //Инициализация
//        init();

        UtilClass.setAnimalFertility(fertilityAnimal);
        UtilClass.setEntityFertility(fertilityEntity);
        UtilClass.setGameTic(gameTic);

        Zookeeper.createRandomEntity(animalCount);
        Zookeeper.startGame(timer, threadCount);

        showStatistic();
    }

    private static void showStatistic() {
        System.out.println("Все живые");
        printEntitiesInRows(Zookeeper.getAliveObjectsList(), 50);
        System.out.println();
        System.out.println("Все мертвые");
        printEntitiesInRows(Zookeeper.getDeadEntitiesList(), 50);
        System.out.println();

        System.out.println("Родились " + Entity.getBabiesCount() + " раз");
        System.out.println("Мигрировали " + Animal.getMoveCount() + " раз");
        System.out.println("Покушали " + Animal.getEatCount() + " раз");
    }

    /**
     * Метод для вывода объектов в ряды по заданному количеству в строке.
     *
     * @param entities  Список сущностей для вывода
     * @param rowLength Количество объектов в одной строке
     */
    private static void printEntitiesInRows(List<AliveObjects> entities, int rowLength) {
        int count = 0;
        for (AliveObjects entity : entities) {
            System.out.print(entity + " ");
            count++;
            if (count % rowLength == 0) {
                System.out.println(); // Переход на новую строку после каждых rowLength объектов
            }
        }
        if (count % rowLength != 0) {
            System.out.println(); // Перевод строки после последней неполной строки
        }
    }

    /**
     * Инициализация параметров игры
     */
    private static void init() {
        WIDTH = getInputValue("Введите ширину поля", 1, 100);
        HEIGHT = getInputValue("Введите высоту поля", 1, 100);
        fertilityAnimal = getInputValue("Введите фертильность животных в пределах значений 2 и 50. Рекомендую от 3 до 5", 2, 50);
        fertilityEntity = getInputValue("Введите фертильность растений в пределах значений 1 и 100. Рекомендую от 10 до 25", 1, 100);
        gameTic = getInputValue("Введите число, тик игры (потоков) в миллисекундах в пределах значений 1 и 60000. Рекомендую от 100 до 1000", 1, 60000);
        animalCount = getInputValue("Введите число, количество животных", 1, 20000);
        timer = getInputValue("Введите время игры в секундах. Рекомендую 60", 1, 3600);
        threadCount = getInputValue("Введите число потоков от 2 до 1000. Рекомендую 10", 2, 1000);
    }

    /**
     * Метод для получения корректного числового значения с валидацией.
     *
     * @param message  Сообщение для пользователя
     * @param minValue Минимальное допустимое значение
     * @param maxValue Максимальное допустимое значение
     * @return Введенное пользователем корректное значение
     */
    private static int getInputValue(String message, int minValue, int maxValue) {
        int value = 0;
        boolean isValid = false;

        System.out.println(message);
        while (!isValid) {
            try {
                value = sc.nextInt();
                if (value >= minValue && value <= maxValue) {
                    isValid = true;
                } else {
                    System.out.println("Значение должно быть в пределах " + minValue + " и " + maxValue);
                }
            } catch (Exception e) {
                System.out.println("Введите корректное число");
                sc.next(); // очищаем некорректный ввод
            }
        }

        return value;
    }
}
