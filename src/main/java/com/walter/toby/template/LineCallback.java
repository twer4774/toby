package com.walter.toby.template;

/**
 * 라인별 작업을 정의한 콜백 인터페이스
 */
public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
