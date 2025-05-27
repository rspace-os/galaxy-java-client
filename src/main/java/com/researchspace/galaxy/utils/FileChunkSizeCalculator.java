package com.researchspace.galaxy.utils;

import java.io.File;

public class FileChunkSizeCalculator {
    public static final int DEFAULT_CHUNK_SIZE = 10485760;
    /**
     *
     * @param toChunk
     * @return the minimum of either the size of the file in bytes or 10485760
     */
    public long calculateFileChunkSize(File toChunk){
        long test = toChunk.length();
        return test < DEFAULT_CHUNK_SIZE ? test : DEFAULT_CHUNK_SIZE;
    }
    /**
     * @param fileToUpload
     * @param offset       offset in reading the file
     * @return the minimum of either the size of the file minus the offest in bytes or 10485760
     */
    public long calculateLengthWithOffset(File fileToUpload, long offset) {
        long testLength = fileToUpload.length() - offset;
        return testLength < DEFAULT_CHUNK_SIZE ? testLength : DEFAULT_CHUNK_SIZE;
    }
}
