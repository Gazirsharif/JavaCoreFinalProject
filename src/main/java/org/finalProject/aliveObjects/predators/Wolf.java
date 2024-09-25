package org.finalProject.aliveObjects.predators;

public class Wolf extends Predator {
    {
        weight = 50;
        maxSpeed = 3;
        foodWeight = 8;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC3A"; //🐺
    }
}
