package com.angola_argentina_portal.controller;

import java.io.Serializable;
import java.util.List;

import com.angola_argentina_portal.dto.CreateNewsDTO;
import com.angola_argentina_portal.dto.ResponseNewsDTO;
import com.angola_argentina_portal.dto.UpdateNewsDTO;
import com.angola_argentina_portal.service.NewsService;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class NewsController implements Serializable {

    private static final long serialVersionUID = 1L;

    private CreateNewsDTO createNewsDTO = new CreateNewsDTO();
    private UpdateNewsDTO updateNewsDTO = new UpdateNewsDTO();

    private List<ResponseNewsDTO> list;

    private Long selectedID;

    @Inject
    private NewsService newsService;

    // Carregar Página de Notícias

    public String loadNewsPage() {

        try {
            list = newsService.getAllNews();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/news/news.xhtml?faces-redirect=true";
    }

        public void save(){

        try{

            newsService.save(createNewsDTO);

            createNewsDTO = new CreateNewsDTO();

            list = newsService.getAllNews();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

        public void update(){

        try{

            newsService.update(updateNewsDTO);

            list = newsService.getAllNews();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

        public void delete(){

        try{

            newsService.delete(selectedID);

            list = newsService.getAllNews();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
