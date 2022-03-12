package com.walter.toby.user.dao;

import com.walter.toby.template.LineReadTemplateCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalcSumTest {

    LineReadTemplateCalculator calculator;
    String numFilePath;

    @BeforeEach
    public void setUp(){
        this.calculator = new LineReadTemplateCalculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {

        int sum = calculator.calcSum(this.numFilePath);

        assertEquals(sum, 10);

    }

    @Test
    public void multiplyOfNumbers() throws IOException{
        int multiply = calculator.calcMultiply(this.numFilePath);

        assertEquals(multiply, 24);
    }

    @Test
    public void concatenateStrings() throws IOException{
        assertEquals(calculator.concatenate(this.numFilePath), "1234");
    }
}
