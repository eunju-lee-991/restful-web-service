package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password"); // 포함시키려는 값

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);


        return mapping;
    }

    // @GetMapping("/v1/users/{id}")
    // @GetMapping(value = "/users/{id}/", params = "version=1") // requestParameter 이용.. uri 뒤에 슬래쉬 추가! 주소 칠 때도 이렇게 해야함 http://localhost:8088/admin/users/1/?version=2
    // @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
    @GetMapping(value = "users/{id}", produces = "application/vnd.company.appv1+json") // 이것도 헤더에.. 키값에 Accept(문서 타입 변경) value는 application/vnd.company.appv1+json
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = service.findOne(id); // return ~~ 에서 cnt+alt+v 하니까 자동으로 변수 생김!

        if(user == null){
            System.out.println("UserNotFoundException 호출");
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn"); // 포함시키려는 값

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    // @GetMapping("/v2/users/{id}")
    // @GetMapping(value = "/users/{id}/", params = "version=2")
    // @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    @GetMapping(value = "users/{id}", produces = "application/vnd.company.appv2+json") //mine time? 마인 타임?
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id); // return ~~ 에서 cnt+alt+v 하니까 자동으로 변수 생김!

        if(user == null){
            System.out.println("UserNotFoundException 호출");
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        UserV2 user2 = new UserV2();
        BeanUtils.copyProperties(user, user2); // bean의 프로퍼티 복사
        user2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade"); // 포함시키려는 값

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user2);
        mapping.setFilters(filters);

        return mapping;
    }
}
