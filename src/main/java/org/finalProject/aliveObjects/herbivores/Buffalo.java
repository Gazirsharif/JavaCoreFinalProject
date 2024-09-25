package org.finalProject.aliveObjects.herbivores;

public class Buffalo extends Herbivore {
    {
        weight = 700;
        maxSpeed = 3;
        foodWeight = 100;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC03"; //🐃
    }
}
