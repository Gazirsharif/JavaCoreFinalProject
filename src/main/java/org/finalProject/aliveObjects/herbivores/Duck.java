package org.finalProject.aliveObjects.herbivores;

public class Duck extends Herbivore {
    {
        weight = 1;
        maxSpeed = 4;
        foodWeight = 0.15;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83E\uDD86"; //🦆
    }
}
