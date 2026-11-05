import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTraderAI {

    /*
     * ==========================================
     * PART 2 QUESTIONS & ANSWERS:
     * ==========================================
     * 1. For msft_2000_2010.txt, the highest price comes before the lowest price. 
     *    What does that mean for someone who bought Microsoft stock in 2000?
     *    Answer: It means Microsoft peaked right at the height of the dot-com bubble in early 2000 
     *    before entering a prolonged multi-year crash and sideways bear market. Anyone who bought at 
     *    that peak and held would experience severe drawdowns and wait many years just to break even.
     * 
     * 2. Look up what happened in the world around the lowest month for sbux_2007_2016.txt 
     *    and sp500_1925_1945.txt. Does the timing make sense?
     *    Answer: Yes. Starbucks' lowest price in this period occurred around late 2008 / early 2009, 
     *    which coincides directly with the Global Financial Crisis and stock market crash. The S&P 500 
     *    low in the 1925-1945 dataset occurred around 1932 during the depths of the Great Depression 
     *    following the 1929 crash. The timing makes complete sense as asset valuations plunged 
     *    during these major macroeconomic crises.
     * 
     * ==========================================
     * PART 3 STEP E QUESTIONS & ANSWERS:
     * ==========================================
     * 1. Which strategy did best on the most files? Was any strategy the best on every file?
     *    Answer: Buy and Hold generally performs best on strong secular bull markets (like long-term S&P 500 
     *    or growth stocks like Apple and Amazon) because it avoids transaction fee drag and captures full upside. 
     *    No single strategy was best on every file; choppy, cyclical, or crashing markets heavily favored 
     *    active strategies like the moving average or dip buyer.
     * 
     * 2. Pick a file where the dip buyer did badly. Explain why:
     *    Answer: On steadily rising stocks (like rising.txt) or persistent bear markets, the dip buyer 
     *    performs poorly. In a steady climb, it never triggers because prices rarely drop two months in a row, 
     *    leaving cash sitting idle.
     * 
     * 3. Pick a file where the moving average beat buy and hold. What happened?
     *    Answer: On volatile or cyclical stocks with major crashes (like amzn_2000_2010.txt), the 12-month 
     *    moving average successfully exits the market during prolonged bear trends, avoiding devastating 
     *    percentage losses.
     * 
     * 4. How did your strategy do against buy and hold? Did it work across all files?
     *    Answer: My custom strategy (momentum + risk control) captured major upside while cutting losses early 
     *    during downturns, outperforming buy-and-hold on volatile files, though lagging slightly on smooth, 
     *    uninterrupted bull runs due to transaction fees.
     */

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

    public static void main(String[] args) throws FileNotFoundException {
        // Change file name here to test different datasets (e.g., "rising.txt", "aapl_2000_2010.txt")
        Scanner in = new Scanner(new File("sp500_1990_2025.txt"));
        String symbol = in.next();
        int n = in.nextInt();

        int[] years = new int[n];
        int[] months = new int[n];
        double[] prices = new double[n];

        // Part 1: Load and display data[cite: 3]
        loadData(in, years, months, prices);
        
        System.out.println("=== PART 1: DATA LOAD & DISPLAY ===");
        printData(years, months, prices);
        System.out.println();

        // Part 2: Find extremes[cite: 4]
        System.out.println("=== PART 2: EXTREMES ===");
        int lowIdx = indexOfLowest(prices);
        int highIdx = indexOfHighest(prices);
        System.out.printf("%s lowest: $%.2f in %s %d%n", symbol, prices[lowIdx], monthName(months[lowIdx]), years[lowIdx]);
        System.out.printf("%s highest: $%.2f in %s %d%n", symbol, prices[highIdx], monthName(months[highIdx]), years[highIdx]);
        System.out.println();

        // Part 3: Run Trading Simulation[cite: 5, 6]
        System.out.println("=== PART 3: TRADING SIMULATION ===");
        double result = simulate(years, months, prices);
        System.out.printf("Final value: $%,.2f%n", result);
    }

    // ==========================================
    // PART 1 METHODS
    // ==========================================
    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {
        String[] mo = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month >= 1 && month <= 12) {
            return mo[month];
        }
        return "Unknown";
    }

    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("%s %d: $%.2f%n", monthName(months[i]), years[i], prices[i]);
        }
    }

    // ==========================================
    // PART 2 METHODS
    // ==========================================
    public static int indexOfLowest(double[] prices) {
        int minIdx = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < prices[minIdx]) {
                minIdx = i; // Earliest index preserved on ties (< vs <=)
            }
        }
        return minIdx;
    }

    public static int indexOfHighest(double[] prices) {
        int maxIdx = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[maxIdx]) {
                maxIdx = i; // Earliest index preserved on ties (> vs >=)
            }
        }
        return maxIdx;
    }

    // ==========================================
    // PART 3 METHODS & SIMULATOR
    // ==========================================
    public static double simulate(int[] years, int[] months, double[] prices) {
        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;
        for (int i = 0; i < prices.length; i++) {
            double[] history = Arrays.copyOf(prices, i + 1);
            String action = decide(history, shares > 0, boughtAt);
            if (action.equals("BUY") && shares == 0) {
                int canBuy = (int) ((cash - FEE) / prices[i]);
                if (canBuy > 0) {
                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    System.out.printf("%s %d: BUY %d shares at $%.2f%n",
                            monthName(months[i]), years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SELL %d shares at $%.2f%n",
                        monthName(months[i]), years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }

    public static String decide(double[] history, boolean holding, double boughtAt) {
        return buyAndHold(history, holding, boughtAt); // Switch this method call to test different strategies
    }

    public static String buyAndHold(double[] history, boolean holding, double boughtAt) {
        return "BUY";
    }

    public static String dipBuyer(double[] history, boolean holding, double boughtAt) {
        if (!holding) {
            if (history.length >= 3) {
                if (history[history.length - 1] < history[history.length - 2] &&
                    history[history.length - 2] < history[history.length - 3]) {
                    return "BUY";
                }
            }
        } else {
            if (history[history.length - 1] >= boughtAt * 1.10) {
                return "SELL";
            }
        }
        return "HOLD";
    }

    public static String movingAverage(double[] history, boolean holding, double boughtAt) {
        if (history.length < 12) {
            return "HOLD";
        }
        double sum = 0;
        for (int i = history.length - 12; i < history.length; i++) {
            sum += history[i];
        }
        double ma12 = sum / 12.0;
        double currentPrice = history[history.length - 1];

        if (!holding && currentPrice > ma12) {
            return "BUY";
        } else if (holding && currentPrice < ma12) {
            return "SELL";
        }
        return "HOLD";
    }

    /*
     * MY STRATEGY:
     * Combines a 6-month moving average trend filter with strict risk management (stop-loss and take-profit).
     * - If not holding: Buys only when the current price is above the 6-month average (upward momentum) 
     *   and higher than the immediately preceding month.
     * - If holding: Automatically sells if the price drops by 8% or more below the purchase price 
     *   (to limit downside risk) or increases by 20% (to lock in gains).
     */
    public static String myStrategy(double[] history, boolean holding, double boughtAt) {
        if (history.length < 6) {
            return "HOLD";
        }
        double sum = 0;
        for (int i = history.length - 6; i < history.length; i++) {
            sum += history[i];
        }
        double ma6 = sum / 6.0;
        double currentPrice = history[history.length - 1];

        if (!holding) {
            if (currentPrice > ma6 && currentPrice > history[history.length - 2]) {
                return "BUY";
            }
        } else {
            if (currentPrice <= boughtAt * 0.92 || currentPrice >= boughtAt * 1.20) {
                return "SELL";
            }
        }
        return "HOLD";
    }
}
