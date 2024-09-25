package org.finalProject.aliveObjects.herbivores;

public class Horse extends Herbivore {
    {
        weight = 400;
        maxSpeed = 4;
        foodWeight = 60;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC0E"; //🐎
    }
}
