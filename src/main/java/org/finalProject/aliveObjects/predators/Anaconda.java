package org.finalProject.aliveObjects.predators;

public class Anaconda extends Predator {
    {
        weight = 15;
        maxSpeed = 1;
        foodWeight = 3;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC0D"; //🐍
    }
}
