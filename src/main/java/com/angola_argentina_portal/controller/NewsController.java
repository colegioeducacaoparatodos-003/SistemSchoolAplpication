package com.angola_argentina_portal.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.angola_argentina_portal.dto.NewsDTO;
import com.angola_argentina_portal.dto.NewsStatus;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.service.NewService;

@Controller
public class NewsController {

    @Autowired
    private NewService newService;

    // Retorna todas as notícias
    @GetMapping("/all")
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(newService.getAllNews());
    }

    // Notícias por status
    @GetMapping
    public ResponseEntity<List<NewsDTO>> getNewsByStatus(@PathVariable NewsStatus status) {
        return ResponseEntity.ok(newService.getNewsByStatus(status));
    }

    // Pesquisar notícias pelo título
    @GetMapping("/search")
    public ResponseEntity<List<NewsDTO>> searchNewsByTitle(@RequestParam("q") String keyword) {
        return ResponseEntity.ok(newService.searchNewsByTitle(keyword));
    }

    // Top notícias por visualizações
    @GetMapping("/top")
    public ResponseEntity<List<NewsDTO>> getTopNewsByViews() {
        return ResponseEntity.ok(newService.getTopNewsByViews());
    }

}
