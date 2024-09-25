package org.finalProject.aliveObjects.herbivores;

public class Goat extends Herbivore {
    {
        weight = 60;
        maxSpeed = 3;
        foodWeight = 10;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC10"; //🐐
    }
}
