package com.ozragwort.moaon.springboot.controller.admin.videos;

import com.ozragwort.moaon.springboot.dto.admin.AdminVideosSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.apiResult.FailedResponse;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import com.ozragwort.moaon.springboot.service.youtube.YoutubeVideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AdminVideosController {

    private final YoutubeVideosService youtubeVideosService;
    private final VideosService videosService;

    @PostMapping("/admin/videos/crud")
    public ResponseEntity<ApiResult> adminSaveVideo(@RequestBody AdminVideosSaveRequestDto requestDto) {
        VideosResponseDto responseDto = youtubeVideosService.save(requestDto);

        ApiResult apiResult;
        apiResult = responseDto == null
                ? new ApiResult().failed(new FailedResponse(400, "Invalid Video ID : " + requestDto.getVideoId()))
                : new ApiResult().succeed(responseDto);

        return ResponseEntity.ok().body(apiResult);
    }

    @PutMapping("/admin/videos/crud/{videoId}")
    public ResponseEntity<ApiResult> adminRefreshVideo(@PathVariable String videoId) {
        VideosResponseDto responseDto = youtubeVideosService.refresh(videoId);

        ApiResult apiResult = new ApiResult().succeed(responseDto);
        return ResponseEntity.ok().body(apiResult);
    }

    @GetMapping("/admin/videos/crud")
    public String adminVideoCrud() {
        return "admin/videoCRUD";
    }

    @GetMapping("/admin/videos/list")
    public String adminFindVideo(Model model,
                                 @RequestParam(required = false) Map<String, Object> keyword,
                                 Pageable pageable) {
        List<VideosResponseDto> videosResponseDtoList = videosService.findAll(keyword, pageable);
        model.addAttribute("videoList", videosResponseDtoList);

        return "admin/videoList";
    }

    @DeleteMapping("/admin/videos/crud/{videoId}")
    public ResponseEntity<ApiResult> adminDeleteVideo(@PathVariable String videoId) {
        youtubeVideosService.delete(videoId);

        ApiResult apiResult = new ApiResult().succeed(videoId);
        return ResponseEntity.ok().body(apiResult);
    }

}
