package org.finalProject.aliveObjects.predators;

public class Bear extends Predator {
    {
        weight = 500;
        maxSpeed = 2;
        foodWeight = 80;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC3B"; //🐻
    }
}
