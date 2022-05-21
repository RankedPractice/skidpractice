/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.elo;

public final class EloCalculator {
    private final double kPower;
    private final int minEloGain;
    private final int maxEloGain;
    private final int minEloLoss;
    private final int maxEloLoss;

    public EloCalculator(double kPower, int minEloGain, int maxEloGain, int minEloLoss, int maxEloLoss) {
        this.kPower = kPower;
        this.minEloGain = minEloGain;
        this.maxEloGain = maxEloGain;
        this.minEloLoss = minEloLoss;
        this.maxEloLoss = maxEloLoss;
    }

    public Result calculate(int winnerElo, int loserElo) {
        double winnerQ = Math.pow(10.0, (double)winnerElo / 300.0);
        double loserQ = Math.pow(10.0, (double)loserElo / 300.0);
        double winnerE = winnerQ / (winnerQ + loserQ);
        double loserE = loserQ / (winnerQ + loserQ);
        int winnerGain = (int)(this.kPower * (1.0 - winnerE));
        int loserGain = (int)(this.kPower * (0.0 - loserE));
        winnerGain = Math.min(winnerGain, this.maxEloGain);
        winnerGain = Math.max(winnerGain, this.minEloGain);
        loserGain = Math.min(loserGain, -this.minEloLoss);
        loserGain = Math.max(loserGain, -this.maxEloLoss);
        return new Result(winnerElo, winnerGain, loserElo, loserGain);
    }

    public static class Result {
        private final int winnerOld;
        private final int winnerGain;
        private final int winnerNew;
        private final int loserOld;
        private final int loserGain;
        private final int loserNew;

        Result(int winnerOld, int winnerGain, int loserOld, int loserGain) {
            this.winnerOld = winnerOld;
            this.winnerGain = winnerGain;
            this.winnerNew = winnerOld + winnerGain;
            this.loserOld = loserOld;
            this.loserGain = loserGain;
            this.loserNew = loserOld + loserGain;
        }

        public int getWinnerOld() {
            return this.winnerOld;
        }

        public int getWinnerGain() {
            return this.winnerGain;
        }

        public int getWinnerNew() {
            return this.winnerNew;
        }

        public int getLoserOld() {
            return this.loserOld;
        }

        public int getLoserGain() {
            return this.loserGain;
        }

        public int getLoserNew() {
            return this.loserNew;
        }
    }
}

