package org.finalProject;

import lombok.Getter;
import lombok.Setter;
import org.finalProject.aliveObjects.AliveObjects;
import org.finalProject.managers.UtilClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Острова.
 * Содержат в себе животных, главный класс с требованием к потокобезопасности.
 * Животные могут заходить, покидать и запрашивать разные данные у острова.
 * Случайно могут рождаться растения или гусеницы.
 */
@Getter
@Setter
public class Island {
    private int id;

    //Главный мьютекс
    public final Lock lock = new ReentrantLock();

    public Island(int id) {
        this.id = id;
    }

    private List<AliveObjects> aliveObjectsList = Collections.synchronizedList(new ArrayList<>());

    /**
     * Животное пришло на остров или родилось
     */
    public void enterTheIsland(AliveObjects aliveObject) {
        aliveObjectsList.add(aliveObject);
        aliveObject.setIsland(this);
    }

    /**
     * Существо умерло или покинуло остров
     */
    public void leaveTheIsland(AliveObjects aliveObject) {
        aliveObjectsList.remove(aliveObject);
        aliveObject.setIsland(null);
    }

    /**
     * Количество особей одного вида.
     * Скорее всего остров уже заблокирован, так что синхронизация не нужна
     */
    private int countOfOneType(@NotNull Class<? extends AliveObjects> aliveClass) {
        return (int) aliveObjectsList.stream()
                .filter(aliveClass::isInstance)
                .count();
    }

    /**
     * Возможно ли размножение?
     */
    public boolean isReproductionPossible(@NotNull Class<? extends AliveObjects> alive) {
        Integer integer = UtilClass.maxCapacity(alive);
        if (integer == null) return false;
        return countOfOneType(alive) < integer;
    }
}
