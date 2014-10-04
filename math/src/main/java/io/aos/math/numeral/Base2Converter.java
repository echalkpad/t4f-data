package io.aos.math.numeral;

import io.aos.console.AosConsole;

public class Base2Converter {

    public static void main(String... args) {

        int[] binary = new int[8];

        int decimal = -1;
        while (!((decimal > -1) && (decimal < 256))) {
            decimal = AosConsole.readInt("Please Type the decimal number (between 0 and 255) you want to convert: ");
        }

        int rest = 0;
        int result = decimal;

        int i = 0;

        while (result > 1) {
            rest = result % 2;
            if (rest == 1) {
                binary[i] = 1;
            } else {
                binary[i] = 0;
            }
            result = result / 2;
            i++;
        }
        binary[i] = result;

        System.out.print(decimal + " in binary=");
        for (int j = binary.length - 1; j > -1; j--) {
            System.out.print(binary[j]);
        }
        System.out.println();
        System.out.println("Bye bye...");

    }

}
