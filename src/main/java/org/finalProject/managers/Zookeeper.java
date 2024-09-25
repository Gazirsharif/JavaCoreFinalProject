package org.finalProject.managers;

import org.finalProject.Island;
import org.finalProject.LiveGame;
import org.finalProject.aliveObjects.AliveObjects;
import org.finalProject.aliveObjects.AnimalObjects;
import org.finalProject.aliveObjects.Entity;
import org.finalProject.aliveObjects.herbivores.*;
import org.finalProject.aliveObjects.plants.Plant;
import org.finalProject.aliveObjects.predators.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Управляет всеми животными в рамках всех остовов.
 * Создает, управляет движением, и уничтожением животных. Работа с потоками выполняется в этом классе.
 * При перемещениях, размножении и питании блокируют работу островам.
 */
public class Zookeeper implements Runnable {
    private static final List<AliveObjects> aliveObjectsList = Collections.synchronizedList(new ArrayList<>());
    private static final List<AliveObjects> deadEntitiesList = Collections.synchronizedList(new ArrayList<>());

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static final Thread mainThread = Thread.currentThread();  // Ссылка на основной поток
    private static final Random random = new Random();

    public static List<AliveObjects> getAliveObjectsList(){
        return aliveObjectsList;
    }

    public static List<AliveObjects> getDeadEntitiesList(){
        return deadEntitiesList;
    }

    /**
     * Создать животное в случайном месте
     */
    private static void createAlive(Class<? extends AliveObjects> aliveClass) {
        try {
            AliveObjects newAlive = aliveClass.getDeclaredConstructor().newInstance();
            aliveObjectsList.add(newAlive);

            int id = random.nextInt(0, LiveGame.WIDTH * LiveGame.HEIGHT);
//            id = 10;
            Island islandById = LiveGame.getIslandById(id);
            islandById.enterTheIsland(newAlive);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Заполняет случайными сущностями острова. Делегирует задачу createAlive
     *
     * @param count количество живых
     */
    public static void createRandomEntity(int count) {
        for (int i = 0; i < count; i++) {

            String entity = UtilClass.allEntity.get(random.nextInt(0, UtilClass.allEntity.size()));
            switch (entity) {
                case "🐺" -> createAlive(Wolf.class);
                case "🐍" -> createAlive(Anaconda.class);
                case "🦊" -> createAlive(Fox.class);
                case "🐻" -> createAlive(Bear.class);
                case "🦅" -> createAlive(Eagle.class);

                case "🐎" -> createAlive(Horse.class);
                case "🦌" -> createAlive(Deer.class);
                case "🐇" -> createAlive(Rabbit.class);
                case "🐁" -> createAlive(Mouse.class);
                case "🐐" -> createAlive(Goat.class);
                case "🐑" -> createAlive(Sheep.class);
                case "🐗" -> createAlive(Boar.class);
                case "🐃" -> createAlive(Buffalo.class);
                case "🦆" -> createAlive(Duck.class);
                case "🐛" -> createAlive(Caterpillar.class);

                case "🌺" -> createAlive(Plant.class);
            }

            // Растения будут всегда, вне зависимости от рандома
            if (i % 2 == 0) {
                createAlive(Plant.class);
            }
        }
    }

    /**
     * Запускает единственный не static метод run делая из него поток.
     * Управляет животными, количеством потоков и длительностью игры
     */
    public static void startGame(int sec, int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            threadPool.submit(new Zookeeper());
        }

        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Конец света ☣️️");
        } finally {
            endGame();
        }
    }

    /**
     * Конец игры наступает когда все умерли или из-за апокалипсиса
     */
    public static void endGame() {
        threadPool.shutdown();

        try {
            // Ждем завершения всех потоков
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }

        System.out.println("🏁🏁🏁🏁🏁🏁🏁🏁🏁");
        System.out.println("Game OVER!");
    }

    /**
     * Поток заставляет случайного животного делать случайное действие.
     * Возможно существует редкая ошибка, при попаданиях мертвых животных в методы, исправляется легко, но после переработки не встречалась
     */
    @Override
    public void run() {
        //TODO: Возможно стоило сделать отдельным классом
        while (!Thread.currentThread().isInterrupted()) { // Цикл, пока поток не прерван
            try {
                if (!aliveObjectsList.isEmpty()) {
                    // Выбор случайного объекта
                    Entity entity = (Entity) aliveObjectsList.get(random.nextInt(aliveObjectsList.size()));
                    // Выбор случайного действия
                    int action = random.nextInt(1, 4); // 1 - feast, 2 - lovePeriod, 3 - migration

//                    String message = entity + " " + entity.getId() + " действие " + action;
//                    System.out.println("🤤 Я " + Thread.currentThread().getName() + " хотел сделать с " + message);
//                    System.out.println("🔒 Я " + Thread.currentThread().getName() + " захватил " + message);
//                    System.out.println("✅ Я " + Thread.currentThread().getName() + " завершил с " + message);

                    //TODO: кажется блокировка по животному лишняя, но это гарантия того что потоки не будут делить одно животное
                    synchronized (entity.entityLock) {
                        if (entity instanceof Plant && action == 2) {
                            lovePeriod(entity);
                        } else if (entity instanceof AnimalObjects) {
                            switch (action) {
                                case 1 -> feast(entity);
                                case 2 -> lovePeriod(entity);
                                case 3 -> migration(entity);
                            }
                        }
                    }

                }
                // Задержка между действиями
                Thread.sleep(UtilClass.getGameTic()); // 100 миллисекунда
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Завершаем поток, если прерван
            }
        }
    }

    //Двигает животное
    public static void migration(AliveObjects aliveObject) {
        if (aliveObject instanceof AnimalObjects) {
            ((AnimalObjects) aliveObject).move();
        }
    }

    //Размножает животное
    public static void lovePeriod(AliveObjects aliveObject) {
        AliveObjects baby = aliveObject.reproduction();
        if (baby != null) {
            aliveObjectsList.add(baby);
        }
    }

    //Бадди, фас!
    public static void feast(AliveObjects aliveObject) {
        if (aliveObject instanceof AnimalObjects && !aliveObject.isDead()) {
            ((AnimalObjects) aliveObject).eat();
        }
    }

    //Убить животное
    public static void killEntities(AliveObjects aliveObject) {
        aliveObject.die();
        aliveObjectsList.remove(aliveObject);
        deadEntitiesList.add(aliveObject);

        //Если все мертвы
        if (aliveObjectsList.isEmpty()) {
            mainThread.interrupt();
        }
    }
}
