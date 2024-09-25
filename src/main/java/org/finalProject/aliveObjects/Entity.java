package org.finalProject.aliveObjects;

import lombok.Getter;
import org.finalProject.Island;
import org.finalProject.managers.UtilClass;
import org.finalProject.managers.Zookeeper;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Сущности. Честно сказать нужна только для растений
 */
@Getter
public abstract class Entity implements AliveObjects {
    protected double weight;
    protected Island island;
    protected boolean isDead;
    protected int age = 0;
    protected final String id = UUID.randomUUID().toString();
    protected int fertility = 5;
    public final Lock entityLock = new ReentrantLock();
    private static final AtomicInteger babiesCount = new AtomicInteger();

    public static int getBabiesCount(){
        return babiesCount.get();
    }

    public static void increment(){
        babiesCount.incrementAndGet();
    }

    @Override
    public boolean canReproduce() {
        return fertility++ >= UtilClass.getEntityFertility();
    }

    @Override
    public void setIsland(Island island) {
        this.island = island;
    }

    /**
     * Растения размножаются без партнера, животные переопределяют метод.
     * @return Клон объекта
     */
    @Override
    public AliveObjects reproduction() {
        if (age++ > 20) {
            System.out.println("⏳   " + this + " из острова " + island.getId() + " умирает от старости");
            Zookeeper.killEntities(this);
            return null;
        }

        synchronized (island.lock) {
            if (island.isReproductionPossible(this.getClass()) && canReproduce()) {
                try {
                    AliveObjects baby = (AliveObjects) clone();
                    island.enterTheIsland(baby);
                    fertility = 0;
                    babiesCount.incrementAndGet();

                    System.out.println("👶   " + this + " из острова " + island.getId() + " родила");
                    return baby;
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException("Стал бесплодным");
                }
            }

            return null;
        }
    }

    /**
     * Умирая животное покидает остров
     */
    @Override
    public void die() {
        synchronized (island.lock) {
            System.out.println("⚰️   " + this + " из острова " + island.getId() + " умер");

            island.leaveTheIsland(this);
            isDead = true;
        }
    }
}
