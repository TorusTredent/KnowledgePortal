package by.knowledgeportal.controller;

import by.knowledgeportal.dto.RecordSaveDto;
import by.knowledgeportal.entity.User;
import by.knowledgeportal.exception.RecordNotFoundException;
import by.knowledgeportal.service.DisciplineService;
import by.knowledgeportal.service.RecordService;
import by.knowledgeportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final DisciplineService disciplineService;
    private final UserService userService;

    @GetMapping("/upload")
    public String uploadFile(Model model) {
        model.addAttribute("file", new RecordSaveDto());
        model.addAttribute("disciplineNames", disciplineService.findAllNames());
        model.addAttribute("recordTypes", recordService.getAllRecordsTypes());
        return "record/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("file") RecordSaveDto recordSaveDto, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        if (recordService.upload(recordSaveDto, user)) {
            model.addAttribute("message", "File was upload");
        } else {
            model.addAttribute("file", recordSaveDto);
            model.addAttribute("message", "Something went wrong");
        }
        model.addAttribute("disciplineNames", disciplineService.findAllNames());
        model.addAttribute("recordTypes", recordService.getAllRecordsTypes());
        return "record/upload";
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<?> downloadFileById(@PathVariable Long id) {
        try {
            Resource resource = recordService.downloadById(id);
            return ResponseEntity.ok()
                    .headers(getHeaderForDownload(resource))
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (RecordNotFoundException | IOException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("disciplines", disciplineService.findAll());
        model.addAttribute("files", recordService.getAllConfirmedRecordsDto());
        return "record/records";
    }


    private HttpHeaders getHeaderForDownload(Resource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8) + "\"");
        return headers;
    }
}
