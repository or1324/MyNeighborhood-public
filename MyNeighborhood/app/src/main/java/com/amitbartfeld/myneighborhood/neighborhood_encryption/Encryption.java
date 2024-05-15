package com.amitbartfeld.myneighborhood.neighborhood_encryption;

import com.google.common.io.BaseEncoding;

public class Encryption {
    public static final int keysNum = 3;
    public static int decodeCode(String code, String codeMechanism) {
        //the string of the code mechanism will tell the app how to decode the code. it will have secret values that only the app knows.
        //I am also need to create the file in the cloud.
        //the method returns the neighborhood num.
        try {
            String[] strings = codeMechanism.split(" ");
            int[] keys = new int[keysNum];
            for (int i = 0; i < keysNum; i++) {
                keys[i] = Integer.parseInt(strings[i]);
            }
            String codeNumString = Long.toString(Long.parseLong(code, 32), 10);
            long codeNum = Long.parseLong(codeNumString);
            codeNum = (long) Math.round(Math.pow(codeNum, 1d / ((double) keys[2])));
            codeNum = (long) (codeNum - (long) keys[1]);
            codeNum = (long) (codeNum / (long) keys[0]);
            return (int) codeNum;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String encodeCode(int neighborhoodNum, String codeMechanism) {
        //the string of the code mechanism will tell the app how to create the code. it will have secret values that only the app knows.
        //I am also need to create the file in the cloud.
        //The method will return the code.
        String[] strings = codeMechanism.split(" ");
        int[] keys = new int[keysNum];
        for (int i = 0; i < keysNum; i++) {
            keys[i] = Integer.parseInt(strings[i]);
        }
        long codeNum = neighborhoodNum;
        codeNum = (long) (codeNum * (long)keys[0]);
        codeNum = (long) (codeNum + (long)keys[1]);
        codeNum = (long) Math.pow(codeNum, (long)keys[2]);
        return Long.toString(Long.parseLong(String.valueOf(codeNum), 10), 32);
    }
}
