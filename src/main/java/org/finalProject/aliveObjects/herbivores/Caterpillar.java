package org.finalProject.aliveObjects.herbivores;

public class Caterpillar extends Herbivore {
    {
        weight = 0.01;
        maxSpeed = 0;
        foodWeight = 0;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC1B"; //🐛
    }
}
