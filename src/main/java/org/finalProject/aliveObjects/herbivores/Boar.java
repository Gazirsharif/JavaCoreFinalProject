package org.finalProject.aliveObjects.herbivores;

public class Boar extends Herbivore {
    {
        weight = 400;
        maxSpeed = 2;
        foodWeight = 50;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC17"; //🐗
    }
}
