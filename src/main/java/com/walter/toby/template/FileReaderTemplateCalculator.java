package com.walter.toby.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderTemplateCalculator {

    // 템플릿/콜백을 적용한 calSum() 메소드
    public Integer calcSum(String filePath) throws IOException {
        BufferedReaderCallback sumCallBack = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum = 0;
                String line = null;
                while ((line = br.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        };
        return fileReadTemplate(filePath, sumCallBack);
    }

    public Integer calcMultiply(String filePath) throws IOException {
        BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer multiply = 1;
                String line = null;
                while ((line = br.readLine()) != null) {
                    multiply *= Integer.valueOf(line);
                }
                return multiply;

            }
        };

        return fileReadTemplate(filePath, multiplyCallback);
    }

    // BufferedReaderCallback을 사용하는 템플릿 메소드
    public Integer fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            int ret = callback.doSomethingWithReader(br); // 콜백 오브젝트 호출. 템플릿에서 만든 컨텍스트 정보인 BufferedReader를 전달해 주고 콜백의 작업 결과를 받아둔다.
            return ret;
        } catch (IOException e) {
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
}
