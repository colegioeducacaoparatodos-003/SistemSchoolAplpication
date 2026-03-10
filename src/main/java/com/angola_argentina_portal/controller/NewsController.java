package com.angola_argentina_portal.controller;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import com.angola_argentina_portal.dto.NewsDTO;
import com.angola_argentina_portal.dto.NewsStatus;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.service.NewService;

@Controller
public class NewsController {

    @Autowired
    private NewService newService;

    // Retorna todas as notícias
    public void ListarNews(){
        try {
            List<NewsDTO> newsList = newService.getAllNews();
        } catch (Exception e) {
            
        }
    }
}
