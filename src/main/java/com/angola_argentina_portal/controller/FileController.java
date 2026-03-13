package com.angola_argentina_portal.controller;

import java.io.ByteArrayInputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.angola_argentina_portal.lazy.FileLazyModel;
import com.angola_argentina_portal.service.FileService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FileController {

    private static final long serialVersionUID = 1L;

    @Inject
    private FileService fileService;

    
    private FileLazyModel lazyModel;

    private StreamedContent downloadFile;

    private String filterFileName;
    private String filterAuthor;

    @PostConstruct
    public void init() {
        if (lazyModel == null) {
            lazyModel = new FileLazyModel(fileService);
        }
    }

    // 🔹 Método chamado quando usuário clica em "Download"
    public void download(Long fileId) {
        var file = fileService.getFileForDownload(fileId);

        downloadFile = DefaultStreamedContent.builder()
                .name(file.getFileName())
                .contentType(file.getContentType())
                .stream(() -> new ByteArrayInputStream(file.getData()))
                .build();
    }

    // 🔹 Getters e Setters
    public FileLazyModel getLazyModel() {
        return lazyModel;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public String getFilterFileName() {
        return filterFileName;
    }

    public void setFilterFileName(String filterFileName) {
        this.filterFileName = filterFileName;
        lazyModel.setNameFilter(filterFileName);
    }

    public String getFilterAuthor() {
        return filterAuthor;
    }

    public void setFilterAuthor(String filterAuthor) {
        this.filterAuthor = filterAuthor;
        lazyModel.setAuthorFilter(filterAuthor);
    }
}
