package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor // 유저를 상속받은 클래스의 인스턴스를 생성할 때 부모 클래스(유저)의 인스턴스를 참고(?)해야하기 때문에 디폴트 생성자 필요
//@JsonIgnoreProperties(value = {"password"})
@Entity // class 이름과 필드들로 DB에 Table 생성해줌
//@JsonFilter("UserInfo")
public class User {

    @Id // 기본키 설정
    @GeneratedValue // 자동생성되는 키값
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.")
    private String name;

    @Past
    private Date joinDate;

   // @JsonIgnore
    private String password;
   // @JsonIgnore
    private String ssn;

    @OneToMany(mappedBy = "user") // user 테이블의 데이터와 맵핑
    private List<Post> posts;

    public User(int id, String name, Date joinDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
