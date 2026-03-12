package com.angola_argentina_portal.lazy;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.service.NewsService;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class NewsLazyModel extends LazyDataModel<NewsTableDTO> {

    private final NewsService service;

    public NewsLazyModel(NewsService service) {
        this.service = service;
    }

    @Override
    public List<NewsTableDTO> load(
            int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        int page = first / pageSize;

        Sort sort = Sort.unsorted();
        if (!sortBy.isEmpty()) {
            SortMeta meta = sortBy.values().iterator().next();
            sort = Sort.by(
                    meta.getOrder().isAscending()
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC,
                    meta.getField());
        }

        // Busca paginada usando o service
        Page<NewsTableDTO> result = service.findLazy(page, pageSize, sort);

        // Atualiza total de linhas
        this.setRowCount((int) result.getTotalElements());

        return result.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        Page<NewsTableDTO> page = service.findLazy(0, 1, Sort.unsorted());
        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(NewsTableDTO dto) {
        return String.valueOf(dto.getId());
    }

    @Override
    public NewsTableDTO getRowData(String rowKey) {
        long id = Long.parseLong(rowKey);

        if (getWrappedData() != null) {
            for (NewsTableDTO dto : getWrappedData()) {
                if (dto.getId() == id) {
                    return dto;
                }
            }
        }
        return null;
    }

    // Getters e Setters
    public NewsService getService() {
        return service;
    }
}
