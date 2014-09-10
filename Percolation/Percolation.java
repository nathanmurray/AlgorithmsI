/****************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:  java Percolation 
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Percolation quick union exercise with Monte-Carlo simulation
 *
 ****************************************************************************/

/**
 *  For additional documentation, see 
 *  <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *     
 *  @author Nathan Murray
 *  @last update: September 9, 2014
 */

public class Percolation {
   private boolean[] gridBlocked;
   private int count;
   private int length;  
   private int topNode = 0;
   private int bottomNode;
   private boolean percolates;
   
   private WeightedQuickUnionUF quickUnionBottom;
   private WeightedQuickUnionUF quickUnionTop;
   
   public Percolation(int N) {             
// create N-by-N grid, with all sites blocked 
       if (N <= 0)
           throw new java.lang.IllegalArgumentException("N is too low");
              
       length = N;
       count = N*N;
       percolates = false;    
       bottomNode = count+1;
       gridBlocked = new boolean[count+2];      
       quickUnionTop = new WeightedQuickUnionUF(count+2);
       quickUnionBottom = new WeightedQuickUnionUF(count+2);
       
       for (int i = 1; i <= count; i++) {
               gridBlocked[i] = true;
       }    

   }          
       
   public void open(int i, int j) {           
// open site (row i, column j) if it is not already
       int up = -1;
       int right = -1;
       int down = -1;
       int left = -1;
       
       testOutOfBounds(i, j);
       
       gridBlocked[conv2Dto1D(i, j)] = false;
       
       if (i > 1)
           up = (length*(i-2) + j);
       else
           up = topNode;
       if (j > 1)
           left = (length*(i-1) + (j-1));
       if (j < length)
           right = (length*(i-1) + (j+1));
       if (i < length)
           down = (length*(i) + j);
       else
           down = bottomNode;
       
       unionGrid(up, i, j);
       unionGrid(left, i , j);
       unionGrid(down, i,  j);
       unionGrid(right, i, j); 
       
       if (quickUnionTop.connected(topNode, conv2Dto1D(i, j))
              && quickUnionBottom.connected(bottomNode, conv2Dto1D(i, j)))
           percolates = true;
       
   }
   
   public boolean isOpen(int i, int j) {      // is site (row i, column j) open?
       testOutOfBounds(i, j);
       
       return !gridBlocked[conv2Dto1D(i, j)];
   }
   
   public boolean isFull(int i, int j) {      // is site (row i, column j) full?
       testOutOfBounds(i, j);
       
       return quickUnionTop.connected(topNode, conv2Dto1D(i, j));
   }
   
   public boolean percolates() {              // does the system percolate?
       return percolates;
   }
   
   private void testOutOfBounds(int i, int j) {
       if (i <= 0 || i > length) 
           throw new java.lang.IndexOutOfBoundsException(
           "row index i out of bounds");
       if (j <= 0 || j > length) 
           throw new java.lang.IndexOutOfBoundsException(
           "row index j out of bounds");
   }
   
   private void unionGrid(int toJoin, int i, int j) {
       if (toJoin != -1 && !gridBlocked[toJoin]) {
           if (toJoin < (count+1))
                quickUnionTop.union(toJoin, conv2Dto1D(i, j));
           if (toJoin > 0)
               quickUnionBottom.union(toJoin, conv2Dto1D(i, j));
       }
   }
   
   private int conv2Dto1D(int i, int j) {
       testOutOfBounds(i, j);
       return (length*(i-1) + j);
   }
   
   public static void main(String[] args) {   // test client, optional
       int N = StdIn.readInt();
       Percolation per = new Percolation(N);
       per.open(1, 1);
       per.open(1, 2);
       StdOut.println(per.quickUnionTop.connected(1, 2));
       StdOut.println(per.quickUnionTop.connected(1, 0));
       StdOut.println(per.quickUnionTop.connected(2, 0));
       StdOut.println(per.isFull(1, 2));
       StdOut.println(per.conv2Dto1D(1, 1));
       StdOut.println(per.conv2Dto1D(N, N));
   }
}

