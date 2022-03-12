package com.walter.toby.template;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LineReadTemplateCalculator {

    // 템플릿/콜백을 적용한 calSum() 메소드
    public Integer calcSum(String filePath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filePath, sumCallback, 0);

        /* 람다식 표현
        LineCallback<Integer> sumCallback = (line, value) -> {
            return value + Integer.valueOf(line);
        };

        return lineReadTemplate(filePath, sumCallback, 0);
        */
    }

    public Integer calcMultiply(String filePath) throws IOException {
        LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };

        return lineReadTemplate(filePath, multiplyCallback, 1);
    }

    // LineCallback을 사용하는 템플릿

    /**
     *
     * @param filePath
     * @param callback
     * @param intVal 계산 결과를 저장할 변수의 초기 값
     * @return
     * @throws IOException
     */
    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T intVal) throws IOException{
        BufferedReader br = null;

        try{
            br = new BufferedReader(new FileReader(filePath));
            T res = intVal;
            String line = null;
            while((line = br.readLine()) != null){
                res = callback.doSomethingWithLine(line, res); // 각 라인의 내용을 가지고 계산하는 작업만 콜백에게 맡긴다.
            }
            return res;
        }catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;

        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    // 문자열 연결 기능 콜백을이용해 만든 concatenate()
    public String concatenate(String filePath) throws IOException{
        LineCallback<String> concatenateCallback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };

        return lineReadTemplate(filePath, concatenateCallback, "");
    }

}
