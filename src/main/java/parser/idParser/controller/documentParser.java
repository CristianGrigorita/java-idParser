package parser.idParser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/document")
public class documentParser {
    @GetMapping()
    public String index(Model model){
        model.addAttribute("keyOfAtt", 2);

        return "document/index";
    }
}
