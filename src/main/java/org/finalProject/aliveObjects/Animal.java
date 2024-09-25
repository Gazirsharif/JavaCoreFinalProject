package org.finalProject.aliveObjects;

import lombok.Getter;
import org.finalProject.Island;
import org.finalProject.LiveGame;
import org.finalProject.aliveObjects.herbivores.Herbivore;
import org.finalProject.aliveObjects.plants.Plant;
import org.finalProject.managers.UtilClass;
import org.finalProject.managers.Zookeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public abstract class Animal extends Entity implements AnimalObjects {
    protected int maxSpeed;
    protected double foodWeight;
    protected double satiety;
    private static final AtomicInteger moveCount = new AtomicInteger(0);
    private static final AtomicInteger eatCount = new AtomicInteger(0);

    public static int getMoveCount() {
        return moveCount.get();
    }

    public static int getEatCount() {
        return eatCount.get();
    }

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * Уровень сытости определяет что:
     * Животное умрет;
     * Сможет родить;
     * Сможет двигаться
     *
     * @return уровень сытости
     */
    private int satietyLevel() {
        if (satiety > 1.5 * foodWeight) {
            return 2;
        } else if (satiety > foodWeight) {
            return 1;
        } else if (satiety < 0) {
            System.out.println("🍽️   " + this + " из острова " + island.getId() + " умирает с голода");
            Zookeeper.killEntities(this);
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * Способность к размножению. В идеале 3-5
     */
    @Override
    public boolean canReproduce() {
        return fertility++ >= UtilClass.getAnimalFertility() && satietyLevel() == 2;
    }

    /**
     * Животные размножаются попарно
     */
    @Override
    public AliveObjects reproduction() {
        if (age++ > 20) {
            System.out.println("⏳   " + this + " из острова " + island.getId() + " умирает от старости");
            Zookeeper.killEntities(this);  // Животное умирает от старости
            return null;
        }

        // Синхронизируем на острове, чтобы избежать конфликтов при доступе к общим ресурсам
        synchronized (island.lock) {
            if (island.isReproductionPossible(this.getClass()) && canReproduce()) {
                try {
                    // Поиск партнера для размножения
                    Optional<Animal> partner = island.getAliveObjectsList()
                            .stream()
                            .filter(obj -> getClass().isInstance(obj) && obj != this)  // Фильтр по типу и исключение самого себя
                            .map(obj -> (Animal) obj)
                            .filter(obj -> obj.satietyLevel() == 2 && obj.canReproduce())  // Проверка сытости и способности к размножению
                            .findFirst();

                    // Если партнер найден
                    if (partner.isPresent()) {
                        // Обновляем состояния текущего объекта и партнера
                        this.fertility = 0;
                        this.satiety = foodWeight * 0.5;
                        partner.get().fertility = 0;
                        partner.get().satiety = foodWeight * 0.5;

                        // Печатаем сообщение о рождении
                        Entity.increment();
                        System.out.println("👶   " + this + " из острова " + island.getId() + " родила");

                        // Клонирование текущего объекта
                        return (AliveObjects) this.clone();
                    }
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;  // Если размножение невозможно, возвращаем null
    }

    @Override
    public void eat() {
        synchronized (island.lock) {
            //Те кого можно съесть
            List<AnimalObjects> victims = island.getAliveObjectsList()
                    .stream()
                    .filter(obj -> obj instanceof AnimalObjects animal && animal != this && UtilClass.hunting(this, animal) != 0)
                    .map(obj -> (AnimalObjects) obj)
                    .toList();

            //Нет никого, кого можно съесть и травоядные
            if (victims.isEmpty() && this instanceof Herbivore) {
                island.getAliveObjectsList()
                        .stream()
                        .filter(obj -> obj instanceof Plant)
                        .map(obj -> (Plant) obj)
                        .findFirst()
                        .ifPresent(plant -> {
                            satiety = Math.min(satiety + plant.weight, foodWeight * 2);
                            eatCount.incrementAndGet();

                            System.out.println("💀   " + this + " из острова " + island.getId() + " съел " + plant);
                            Zookeeper.killEntities(plant);
                        });

                return; // Завершаем метод, если травоядное съело или не съело растение
            }

            //Охота хищников
            for (AnimalObjects victim : victims) { // Уже отфильтрованы те, у кого шанс > 0
                int hunting = UtilClass.hunting(this, victim);
                if (random.nextInt(100) < hunting) {
                    satiety = Math.min(satiety + ((Entity) victim).getWeight(), foodWeight * 2);
                    eatCount.incrementAndGet();

                    System.out.println("💀   " + this + " из острова " + island.getId() + " съел " + victim);
                    Zookeeper.killEntities((AliveObjects) victim);
                    break; // Завершаем охоту после поедания
                }
            }
        }
    }

    @Override
    public void move() {
        if ((LiveGame.WIDTH == 1 && LiveGame.HEIGHT == 1)) {
            return;
        }

        int oldId = island.getId();
        int newId = getNewId(oldId);

        Island newIsland = LiveGame.getIslandById(newId);

        // Используем блок synchronized для внешней блокировки
        // Блокируем острова в порядке их ID, чтобы избежать deadlock
        Island firstLock = oldId < newId ? island : newIsland;
        Island secondLock = oldId < newId ? newIsland : island;

        synchronized (firstLock.lock) {
            synchronized (secondLock.lock) {
                island.leaveTheIsland(this);
                newIsland.enterTheIsland(this);
                setIsland(newIsland);

                moveCount.incrementAndGet();
                System.out.println("🏃‍♂️   " + this + " из острова " + oldId + " переехал в " + newId);
            }
        }

        //Движение отнимает сытость
        satiety -= foodWeight * 0.1;
        satietyLevel();
    }

    /**
     * Новый остров куда животное придет
     *
     * @param oldId ID старого острова
     * @return ID нового острова
     */
    private int getNewId(int oldId) {
        int width = LiveGame.WIDTH;
        int height = LiveGame.HEIGHT;
        int i = oldId / width;
        int j = oldId % width;

        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));

        //Пока не найдем направление света
        while (!directions.isEmpty()) {
            int randomInt = random.nextInt(directions.size());
            Direction direction = directions.remove(randomInt); // Удаляем выбранное направление

            int newId = switch (direction) {
                case UP -> width * (i - 1) + j;
                case DOWN -> width * (i + 1) + j;
                case LEFT -> width * i + (j - 1);
                case RIGHT -> width * i + (j + 1);
            };

            // Проверяем на легальные значения
            if (newId >= 0 && newId < width * height) {
                return newId;
            }
        }

        //TODO: Возможно никогда не настанет, так как если остров только один то метод не будет вызван
        return oldId; // Если не удалось найти новое направление, остаёмся на месте
    }
}
