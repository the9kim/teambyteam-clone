package web.teambyteam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/phone")
    String phone() {
        return "010-1234-5678";
    }
}
