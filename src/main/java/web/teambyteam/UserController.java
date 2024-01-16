package web.teambyteam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String addNewUser(@RequestParam String name,
                             @RequestParam String email) {

        MyUser myUser = new MyUser();
        myUser.setName(name);
        myUser.setEmail(email);

        MyUser save = userRepository.save(myUser);


        return save.getName() + " saved!";
    }

    @GetMapping("/get")
    public List<MyUser> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/delete")
    public String getEmail(@RequestParam String name) {
        MyUser user = userRepository.findByName(name);

        userRepository.delete(user);

        return user.getName() + " delete!";
    }

    @PostMapping("/update")
    public String update(@RequestParam String name,
                         @RequestParam String newName) {
        MyUser user = userRepository.findByName(name);

        user.setName(newName);

        userRepository.flush();

        return user.getName() + "updated!";
    }


}
