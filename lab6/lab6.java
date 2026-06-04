package lab6;
import java.io.*;
import java.util.*;

public class lab6 {
    
}

class Cache {
    final int numSets;
    final int ways;
    final int offset;
    final int indexBits;
    final int indexMask;
    final int[][] tags;
    final int[][] lruCounter;
 
    long hits = 0;
    long accesses = 0;

    Cache(int cacheSize, int ways, int blockSize) {
        this.ways = ways;
        this.offset = Integer.numberOfTrailingZeros(blockSize);
        this.numSets = cacheSize / (blockSize * ways);
        this.indexBits = Integer.numberOfTrailingZeros(numSets);
        this.indexMask = numSets - 1;
        this.tags = new int[numSets][ways];
        this.lruCounter = new int[numSets][ways];

        for (int[] row : tags) {
            Arrays.fill(row, -1);
        }
    }
}
