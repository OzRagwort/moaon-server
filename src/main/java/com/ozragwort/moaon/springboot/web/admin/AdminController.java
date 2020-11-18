package com.ozragwort.moaon.springboot.web.admin;

import com.ozragwort.moaon.springboot.config.auth.LoginUser;
import com.ozragwort.moaon.springboot.config.auth.dto.SessionUser;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.service.CategoriesService;
import com.ozragwort.moaon.springboot.service.ChannelsService;
import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.web.dto.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.web.dto.VideosResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final CategoriesService categoriesService;

    private final ChannelsService channelsService;

    private final VideosService videosService;

    @GetMapping("/admin")
    public String admin(Model model, @LoginUser SessionUser user) {

        if(user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        return "admin/index";
    }

    @GetMapping("/admin/categories/list")
    public String adminCategoryList(Model model,
                                    @LoginUser SessionUser user,
                                    @RequestParam(value = "no", required = false) Long idx,
                                    @RequestParam(value = "maxResult", defaultValue = "10") int size,
                                    @RequestParam(value = "page", defaultValue = "1") int pageCount) {

        if (user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        List<CategoriesResponseDto> categories = new ArrayList<>();

        if (idx != null) {
            categories.add(categoriesService.findById(idx));
        } else {
            categories = categoriesService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        }

        model.addAttribute("categoryList", categories);

        return "admin/categoryList";
    }
    @GetMapping("/admin/categories/crud")
    public String adminCategoryCrud(Model model, @LoginUser SessionUser user) {

        if(user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        return "admin/categoryCRUD";
    }

    @GetMapping("/admin/channels/list")
    public String adminChannelList(Model model,
                                   @LoginUser SessionUser user,
                                   @RequestParam(value = "no", required = false) Long idx,
                                   @RequestParam(value = "id", required = false) String channelId,
                                   @RequestParam(value = "category", required = false) Long categoryId,
                                   @RequestParam(value = "maxResult", defaultValue = "10") int size,
                                   @RequestParam(value = "page", defaultValue = "1") int pageCount) {

        if(user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        List<ChannelsResponseDto> channels;

        if (idx != null) {
            channels = channelsService.findById(idx);
        } else if (channelId != null) {
            channels = channelsService.findByChannelId(channelId);
        } else if (categoryId != null) {
            channels = channelsService.findByCategoryIdx(categoryId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        } else {
            channels = channelsService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        }

        model.addAttribute("channelList", channels);

        return "admin/channelList";
    }

    @GetMapping("/admin/channels/crud")
    public String adminChannelCrud(Model model, @LoginUser SessionUser user) {

        if(user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        return "admin/channelCRUD";
    }

    @GetMapping("/admin/videos/list")
    public String adminVideoList(Model model,
                                 @LoginUser SessionUser user,
                                 @RequestParam(value = "no", required = false) Long idx,
                                 @RequestParam(value = "id", required = false) String videoId,
                                 @RequestParam(value = "channel", required = false) String channelId,
                                 @RequestParam(value = "category", required = false) Long categoryId,
                                 @RequestParam(value = "maxResult", defaultValue = "10") int size,
                                 @RequestParam(value = "page", defaultValue = "1") int pageCount) {

        if(user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        List<VideosResponseDto> videos;
        if (idx != null)
            videos = videosService.findById(idx);
        else if (videoId != null)
            videos = videosService.findByVideoId(videoId);
        else if (channelId != null)
            videos = videosService.findByChannelId(channelId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        else if (categoryId != null)
            videos = videosService.findByCategoryIdx(categoryId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        else
            videos = videosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        model.addAttribute("videoList", videos);

        return "admin/videoList";
    }

    @GetMapping("/admin/videos/crud")
    public String adminVideoCrud(Model model, @LoginUser SessionUser user) {

        if(user != null) {
            model.addAttribute("loginedUserName", user.getEmail());
        }

        return "admin/videoCRUD";
    }




}
