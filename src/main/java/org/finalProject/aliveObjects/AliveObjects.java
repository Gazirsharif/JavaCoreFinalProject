package org.finalProject.aliveObjects;

import org.finalProject.Island;

public interface AliveObjects extends Cloneable {
    boolean canReproduce();

    boolean isDead();

    void setIsland(Island island);

    AliveObjects reproduction();

    void die();
}
