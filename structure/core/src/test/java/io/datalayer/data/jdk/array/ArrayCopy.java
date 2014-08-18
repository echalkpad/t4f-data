package io.datalayer.data.jdk.array;

import java.util.Arrays;

import org.junit.Test;

public class ArrayCopy {
    
    @Test
    public void test() {
        String[] strings = new String[]{"1", "2"};
        Arrays.copyOfRange(strings, 0, 1);
        System.arraycopy(strings, 0, new String[]{}, 0, 1); 
    }

}
