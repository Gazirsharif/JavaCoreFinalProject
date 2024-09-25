package org.finalProject.aliveObjects.herbivores;

public class Rabbit extends Herbivore {
    {
        weight = 2;
        maxSpeed = 2;
        foodWeight = 0.45;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC07"; //🐇
    }
}
