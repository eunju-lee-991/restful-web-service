package com.example.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/jpa")
@RestController
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        //hateoas로 얻은 link로 등록 후 전체 사용자 보기 이런 게 가능한 건가@.@??? 활용을 어케 하지? 클라이언트에서 쓰기 위한 게 아닌 건가?
        //actuator랑 hal은 아예 뭐하는 놈들인지 잘 모르겠고...
        //spring security는 따로 깊게 공부해야하는 것 같고..
        EntityModel<User> model = new EntityModel<>(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        return model;
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user ){
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        User storedUser = optionalUser.get();
        storedUser.setName(user.getName());
        storedUser.setPassword(user.getPassword());

        User updatedUser = userRepository.save(storedUser); // 이러면 기존 user는 사라지는 거야??????

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                //.path("/{id}")
                .buildAndExpand(updatedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // *** user.get().getPosts() Null일 때 (findById가 null 일 때?) 커스텀 exception 발생하게
        if(user.get().getPosts().size() < 1){
            throw new PostNotFoundException("해당 사용자의 등록된 글이 없습니다.");
        }else {
            return user.get().getPosts();
        }
    }

    @GetMapping("/posts")
    public List<Post> retrieveAllPosts(){
        return postRepository.findAll();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts/{postid}")
    public EntityModel<Post> retrieveUser(@PathVariable int id, @PathVariable int postid){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        Optional<Post> posts = postRepository.findById(postid);

        if(!posts.isPresent()){
            throw new PostNotFoundException("해당 번호로 등록된 글이 없습니다.");
        }

        EntityModel<Post> model = new EntityModel<>(posts.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllPostsByUser(id));
        model.add(linkTo.withRel("all-user-posts"));

        return model;
    }

    @DeleteMapping("/users/{id}/posts/{postid}")
    public void deletePost(@PathVariable int postid){
        postRepository.deleteById(postid);
    }

    @PutMapping("/users/{id}/posts/{postid}")
    public ResponseEntity<Post> updatePost(@PathVariable int postid, @RequestBody Post post ){
        Optional<Post> optionalPost = postRepository.findById(postid);

        if(!optionalPost.isPresent()){
            //throw new UserNotFoundException(String.format("ID[%s] not found", id));
            System.out.println("PostNotFound");
        }

        Post storedPost = optionalPost.get();
        storedPost.setDescription(post.getDescription());

        Post updatedPost = postRepository.save(storedPost); // 이러면 기존 post는 사라지는 거야??????

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                //.path("/{id}")
                .buildAndExpand(updatedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //http://localhost:8088/jpa/users/100/posts/2
    // *** 개별 게시물 조회o, 삭ㅈ[o, 수정...
}
