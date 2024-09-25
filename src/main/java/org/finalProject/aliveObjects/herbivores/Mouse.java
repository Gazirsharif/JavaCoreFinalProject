package org.finalProject.aliveObjects.herbivores;

public class Mouse extends Herbivore {
    {
        weight = 0.05;
        maxSpeed = 1;
        foodWeight = 0.01;
        satiety = foodWeight;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC01"; //🐁
    }
}
