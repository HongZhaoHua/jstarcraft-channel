package com.jstarcraft.nlp.bloomfilter.local;

import java.util.Random;

import com.jstarcraft.nlp.bloomfilter.StringHashFamily;
import com.jstarcraft.nlp.bloomfilter.bit.ByteMap;

/**
 * 基于ByteMap的布隆过滤器
 * 
 * @author Birdy
 *
 */
public class ByteMapLocalBloomFilter extends BitMapLocalBloomFilter<byte[]> {

    public ByteMapLocalBloomFilter(int bitSize, StringHashFamily hashFamily, int hashSize, Random random) {
        super(bitSize, new ByteMap(bitSize), getFunctions(hashFamily, hashSize, random));
    }

}
