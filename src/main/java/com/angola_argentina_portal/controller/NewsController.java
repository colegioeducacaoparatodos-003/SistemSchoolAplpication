package com.angola_argentina_portal.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

import com.angola_argentina_portal.dto.CreateNewsDTO;
import com.angola_argentina_portal.dto.UpdateNewsDTO;
import com.angola_argentina_portal.dto.ResponseNewsDTO;
import com.angola_argentina_portal.lazy.NewsLazyModel;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.service.NewsService;

@Named
@ViewScoped
public class NewsController implements Serializable {

    private static final long serialVersionUID = 1L;

    private News news = new News();
    private UpdateNewsDTO editDto = new UpdateNewsDTO();
    private ResponseNewsDTO responseNewsDTO = new ResponseNewsDTO();
    private Long selectedId;

    @Inject
    private NewsService newsService;

    // @Inject
    private NewsLazyModel lazyModel;

    /*
     * ---------------- LOAD PAGE ----------------
     */
    public String loadNewsPage() {
        try {

            lazyModel = new NewsLazyModel(newsService);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar notícias",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/news.xhtml?faces-redirect=true";
    }

    public NewsLazyModel getLazyModel() {
        if (lazyModel == null) {
            lazyModel = new NewsLazyModel(newsService);
        }
        return lazyModel;
    }

    /* ---------------- CRUD ---------------- */
    public void openEditDialog() {
        if (selectedId == null || selectedId == 0) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma notícia selecionada!", "");
            return;
        }

        ResponseNewsDTO dto = newsService.getAllNews().stream()
                .filter(n -> n.getId().equals(selectedId))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            editDto = new UpdateNewsDTO();
            editDto.setId(dto.getId());
            editDto.setTitle(dto.getTitle());
            editDto.setSubtitle(dto.getSubtitle());
            editDto.setSummary(dto.getSummary());
            editDto.setAuthor(dto.getAuthor());
            editDto.setCategory(dto.getCategory());
        }
    }

    public void save() {
        newsService.save(news);
        news = new News();
    }

    public void saveUpdate() {
        try {
            newsService.update(editDto);

            lazyModel = new NewsLazyModel(newsService);
            editDto = new UpdateNewsDTO();
            selectedId = null;

            addMessage(FacesMessage.SEVERITY_INFO, "Notícia", "Notícia atualizada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Notícia", e.getMessage());
        }
    }

    public void delete() {
        try {
            newsService.delete(selectedId);
            selectedId = null;
            lazyModel = new NewsLazyModel(newsService);
            addMessage(FacesMessage.SEVERITY_INFO, "Notícia", "Notícia deletada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Notícia", e.getMessage());
        }
    }

    /* ---------------- UTIL ---------------- */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    /* ---------------- GETTERS E SETTERS ---------------- */
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public UpdateNewsDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(UpdateNewsDTO editDto) {
        this.editDto = editDto;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public void setLazyModel(NewsLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public NewsService getNewsService() {
        return newsService;
    }

    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }
}