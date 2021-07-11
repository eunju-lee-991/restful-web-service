package com.example.restfulwebservice.helloworld;

//lombok

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// AllArgsConstructor가 자동으로 message값을 매개변수로 받아서 변수 message에 할당하는
// 생성자를 만들어 줌
// NoArgsConstructor 은 디폴트 생성자
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldBean {
    private String message;
}
