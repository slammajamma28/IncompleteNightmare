package org.pst.slamma;

import java.util.List;

/**
 * This class will be the object that stores all the information about a user
 */
public class PSTMember {

    private String pstName;
    private String psnName;
    private boolean edIslandPenalty;
    private int numberOfTrophies;
    private double totalRarity;
    private double deathProbability;
    private boolean killed;
    private List<Trophy> trophies;

    /**
     * This is the constructor for the PST Member object
     * The PST and PSN name will be passed in and then the constructor will continue
     * by calling the pullInfo to grab necessary information.
     * @param pst
     * @param psn
     */
    public PSTMember(String pst, String psn, boolean penalty) {
        setPstName(pst);
        setPsnName(psn);
        setEdIslandPenalty(penalty);
        System.out.println("Pulling info for " + pst + " (" + psn + ")...");
        setTrophies(new ScrapePSNPLog().trophiesFromLog(psn));
        setNumberOfTrophies(trophies.size());
        setDeathProbability();
        rollTheDice(getDeathProbability());
    }

    public void rollTheDice(Double rarity) {
        double deathDistro =  rarity / 100;
        double lifeDistro = 1d - deathDistro;

        DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
        // 1 is dead
        // 0 is live
        drng.addNumber(1, deathDistro);
        drng.addNumber(0, lifeDistro);


        boolean killedChoice = ((drng.getDistributedRandomNumber() == 0) ? false : true);

        setKilled(killedChoice);
    }

    public String getPstName() { return pstName; }

    public void setPstName(String pstName) { this.pstName = pstName; }

    public String getPsnName() { return psnName; }

    public void setPsnName(String psnName) { this.psnName = psnName; }

    public boolean isEdIslandPenalty() { return edIslandPenalty; }

    public void setEdIslandPenalty(boolean edIslandPenalty) { this.edIslandPenalty = edIslandPenalty; }

    public int getNumberOfTrophies() { return numberOfTrophies; }

    public void setNumberOfTrophies(int numberOfTrophies) { this.numberOfTrophies = numberOfTrophies; }

    public double getTotalRarity() { return totalRarity; }

    public void setTotalRarity(double totalRarity) { this.totalRarity = totalRarity; }

    public double getDeathProbability() { return deathProbability; }

    public void setDeathProbability() {
        double probOfDed = 0d;
        for (Trophy trophy : trophies) {
            probOfDed = trophy.getRarity_percentage() + probOfDed;
        }
        if (isEdIslandPenalty()) {
            this.deathProbability = (probOfDed / trophies.size()) + 10d;
        } else {
            this.deathProbability = (probOfDed / trophies.size());
        }

    }

    public boolean isKilled() { return killed; }

    public void setKilled(boolean killed) { this.killed = killed; }

    public List<Trophy> getTrophies() { return trophies; }

    public void setTrophies(List<Trophy> trophies) { this.trophies = trophies; }

    // Testing purposes only
    public static void main(String args[]) {
        // This is to test the actual information pulled from PSNP
//        PSTMember slamz = new PSTMember("Slamma", "slammajamma28", "Test Team", 4332, 67, 7555, 3);
//        PSTMember slamz = new PSTMember("Slamma", "slammajamma28", "Test Team", 4332, 67);
//        List<Integer> testResult = pullInfo("slammajamma28");
//        for (int i : testResult) {
//            System.out.println(i);
//        }
    }
}