package org.example.pensionatkademina;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalcTest {

    @Test
    public void addTest() {
        Calc calc = new Calc();
        int result = calc.add(2,1);
        assertEquals(result,3);
    }
}
