package org.finalProject.aliveObjects.predators;

public class Fox extends Predator {
    {
        weight = 8;
        maxSpeed = 2;
        foodWeight = 2;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83E\uDD8A"; //🦊
    }
}
