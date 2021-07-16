package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Integer id; // 왜 int 아니고 인티저?

    private String description;

    // User : Post -> 1:(0~N), Main : Sub ->, parent : Child
    @ManyToOne(fetch = FetchType.LAZY) // Post 여러개에 User 하나 매칭
    @JsonIgnore                     //LAZY -> 지연로딩방식 : post 데이터가 로딩되는 시점에 필요한 사용자 데이터를 가지고 온다
    private User user;
}
