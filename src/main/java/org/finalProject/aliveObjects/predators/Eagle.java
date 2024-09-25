package org.finalProject.aliveObjects.predators;

public class Eagle extends Predator {
    {
        weight = 6;
        maxSpeed = 3;
        foodWeight = 1;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83E\uDD85"; //🦅
    }
}
