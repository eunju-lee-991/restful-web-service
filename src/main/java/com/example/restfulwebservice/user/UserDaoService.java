package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int userCount = 3;

    static  {
        users.add(new User(1,"kenneth", new Date(), "pass1", "701010-1111111"));
        users.add(new User(2,"Alice", new Date(), "pass2", "701010-1111112"));
        users.add(new User(3,"Elena", new Date(), "pass3", "701010-1111113"));
    }

    public List<User> findAll(){
        return  users;
    }

    public User save(User user){
        if(user.getId()==null){
            user.setId(++userCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id){
        for(User user:users){
            if(user.getId() == id){
                return  user;
            }
        }

        return null;
    }

    public User updateNameById(int id, String name){
        User user = findOne(id);
        System.out.println("여기?");

        if(user == null){
            System.out.println("UserNotFoundException ... 호출 service 단에서 호출해도 되는 건가?");
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }else {
            user.setName(name);
        }
        return user;
    }

    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()){
            User user = iterator.next();

            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }
}