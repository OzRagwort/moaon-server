package com.ozragwort.moaon.springboot.v1.web;

import com.ozragwort.moaon.springboot.config.auth.LoginUser;
import com.ozragwort.moaon.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class Indexcontroller {

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if(user != null)
            model.addAttribute("loginedUserName", user.getEmail());

        return "index";
    }

}
