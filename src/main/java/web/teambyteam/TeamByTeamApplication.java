package web.teambyteam;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TeamByTeamApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamByTeamApplication.class, args);

    }

}

@RestController
class Hello {
    @RequestMapping("/")
    String index() {
        return "hello";
    }

    @RequestMapping("/name")
    String name() {
        return "roy";
    }

    @RequestMapping("name/full")
    String fullName() {
        return "roy kim";
    }

    @RequestMapping("/age")
    int age() {
        return 36;
    }

    @RequestMapping("/add")
    String address() {
        return "changwon";
    }
}
