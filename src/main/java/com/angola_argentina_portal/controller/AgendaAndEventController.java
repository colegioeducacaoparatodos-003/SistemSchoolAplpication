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

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Controller
@Named
@ViewScoped
public class AgendaAndEventController {

    @Autowired
    // private NewService newService;

    public String loadAgendaAndEventPage() {
        try {
            // loadLazy();
        } catch (Exception e) {
            // FacesMessageUtil.errorMessage("Erro " + e.getMessage());
            e.printStackTrace();
        }
        return "/management/agenda-and-events.xhtml?faces-redirect=true";
    }
}
