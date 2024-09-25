package org.finalProject.aliveObjects.herbivores;

public class Sheep extends Herbivore {
    {
        weight = 70;
        maxSpeed = 3;
        foodWeight = 15;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC11"; //🐑
    }
}
