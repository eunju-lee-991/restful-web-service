package com.example.restfulwebservice.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        User user = service.findOne(id); // return ~~ 에서 cnt+alt+v 하니까 자동으로 변수 생김!

        if(user == null){
            System.out.println("UserNotFoundException 호출");
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){ // FORM 데이터가 아니라 JSON과 같은 오브젝트 데이터 타입을 받으려면 RequestBody
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()) // 저 윗줄 가변변수에 새로 만든 id값을 지정. 오... 신기
                .toUri();

        return ResponseEntity.created(location).build();
    } // 서버로부터 요청 결과값의 적절한 상태코드를 반환시켜주려는...?
    // 응답코드값을 저장하기 위해 서버에서 반환시켜주려는 값을 responseEntitiy에 저장?
    // ResponseEntity는 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스

    @PutMapping("/users/{id}/{name}")
    public User UpdateUserName(@PathVariable int id, @PathVariable String name){
        User user = service.updateNameById(id,name);
        if(user == null){
            System.out.println("UserNotFoundException update 호출");
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.deleteById(id);
        System.out.println(user);
        if(user == null){
            System.out.println("UserNotFoundException delete 호출");
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }
}
