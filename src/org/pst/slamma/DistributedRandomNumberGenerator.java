package org.pst.slamma;

import java.util.HashMap;
import java.util.Map;

public class DistributedRandomNumberGenerator {

    private Map<Integer, Double> distribution;
    private double distSum;

    public DistributedRandomNumberGenerator() {
        distribution = new HashMap<>();
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomNumber() {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }



    public static void main(String args[]) {
        double rarity = Double.parseDouble(args[0]);
        double death_distribution = rarity / 100;
        double life_distribuion = 1d - death_distribution;

        DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
        // 1 is dead
        // 0 is live
        drng.addNumber(1, death_distribution);
        drng.addNumber(0, life_distribuion);

        int testCount = 100000;

        HashMap<Integer, Double> test = new HashMap<>();

        for (int i = 0; i < testCount; i++) {
            int random = drng.getDistributedRandomNumber();
            test.put(random, (test.get(random) == null) ? (1d / testCount) : test.get(random) + 1d / testCount);
        }

        System.out.println(test.toString());

        System.out.println("Did person die? " + ((drng.getDistributedRandomNumber() == 0) ? "No" : "Yes"));

    }
}