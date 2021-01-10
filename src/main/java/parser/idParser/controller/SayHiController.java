package parser.idParser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import parser.idParser.model.Hi;

@RestController()
@RequestMapping("/sayHi")
public class SayHiController {
    @GetMapping("/api/{name}")
    public Hi getEntity(@PathVariable String name){

        return new Hi(name);
    }
}
