package org.finalProject.aliveObjects.herbivores;

public class Deer extends Herbivore {
    {
        weight = 300;
        maxSpeed = 4;
        foodWeight = 50;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83E\uDD8C"; //🦌
    }
}
