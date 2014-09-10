/****************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:  java PercolationStats 
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Percolation quick union exercise with Monte-Carlo simulation; Statistics
 *
 ****************************************************************************/

/**
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *     
 *  @author Nathan Murray
 *  @last update: September 8, 2014
 */

public class PercolationStats {
    
    private double[] fractionOfSites;
    private int experiments;
    private int gridSize;
    
    public PercolationStats(int N, int T) {    // perform T independent computational experiments on an N-by-N grid
        
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException("Initialization out of bounds");
        
        fractionOfSites = new double[T];
        experiments = T;
        gridSize = N*N;
        
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            int j = 0;
            while (!perc.percolates()) {
                j++;
                int k = StdRandom.uniform(N)+1;
                int l = StdRandom.uniform(N)+1;
                while (perc.isOpen(k, l)) {
                    k = StdRandom.uniform(N)+1;
                    l = StdRandom.uniform(N)+1;
                }
                perc.open(k, l);
            }
            fractionOfSites[i] = (double) j/(double) gridSize;
        }                  
    }
    public double mean() {                     
// sample mean of percolation threshold
        return StdStats.mean(fractionOfSites);
    }
    public double stddev() {                   
// sample standard deviation of percolation threshold
        return StdStats.stddev(fractionOfSites);
    }
    public double confidenceLo() {             
// returns lower bound of the 95% confidence interval
        return mean() - 1.96*stddev()/Math.pow(experiments, .5);
    }
    public double confidenceHi() {             
// returns upper bound of the 95% confidence interval
        return mean() + 1.96*stddev()/Math.pow(experiments, .5);
    }
    public static void main(String[] args) {   
// test client, described below
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(N, T);
        
        StdOut.println("mean                    = " 
                           + stats.mean());
        StdOut.println("stddev                  = " 
                           + stats.stddev());
        StdOut.println("95% confidence interval = " 
                           + stats.confidenceLo() 
                           + ", " 
                           + stats.confidenceHi());
    }
}