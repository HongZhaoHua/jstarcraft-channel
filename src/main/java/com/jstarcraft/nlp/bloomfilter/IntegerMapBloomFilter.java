package com.jstarcraft.nlp.bloomfilter;

import java.util.Random;

import com.jstarcraft.nlp.bloomfilter.bit.IntegerMap;

/**
 * 基于IntegerMap的布隆过滤器
 * 
 * @author Birdy
 *
 */
public class IntegerMapBloomFilter extends BitMapBloomFilter<int[]> {

    public IntegerMapBloomFilter(int bitSize, StringHashFamily hashFamily, int hashSize, Random random) {
        super(bitSize, new IntegerMap(bitSize), getFunctions(hashFamily, hashSize, random));
    }

}
