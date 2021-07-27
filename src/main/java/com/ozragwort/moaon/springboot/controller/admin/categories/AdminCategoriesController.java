package com.ozragwort.moaon.springboot.controller.admin.categories;

import com.ozragwort.moaon.springboot.config.auth.LoginUser;
import com.ozragwort.moaon.springboot.config.auth.dto.SessionUser;
import com.ozragwort.moaon.springboot.dto.categories.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.service.youtube.YoutubeCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AdminCategoriesController {

    private final YoutubeCategoriesService youtubeCategoriesService;

    @GetMapping("/admin/categories/list")
    public String adminFindCategory(Model model,
                                    @LoginUser SessionUser user,
                                    @RequestParam(required = false) Map<String, Object> keyword,
                                    Pageable pageable) {

        if (user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        List<CategoriesResponseDto> categories = youtubeCategoriesService.findAll(keyword, pageable);

        model.addAttribute("categoryList", categories);

        return "admin/categoryList";
    }

}
