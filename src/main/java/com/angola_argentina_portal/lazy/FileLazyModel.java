package com.angola_argentina_portal.lazy;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.FileTableDTO;
import com.angola_argentina_portal.service.FileService;

public class FileLazyModel extends LazyDataModel<FileTableDTO> {

    private final FileService fileService;

    private String nameFilter;
    private String authorFilter;

    public FileLazyModel(FileService fileService) {
        this.fileService = fileService;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public void setAuthorFilter(String authorFilter) {
        this.authorFilter = authorFilter;
    }

    @Override
    public List<FileTableDTO> load(
            int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        int page = first / pageSize;

        String sortField = "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;

        // Configura ordenação caso exista
        if (!sortBy.isEmpty()) {
            SortMeta meta = sortBy.values().iterator().next();
            sortField = meta.getField();
            direction = meta.getOrder().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        // Configura filtros caso existam
        if (filterBy != null) {
            if (filterBy.containsKey("fileName")) {
                Object val = filterBy.get("fileName").getFilterValue();
                this.nameFilter = val != null ? val.toString() : null;
            }
            if (filterBy.containsKey("author")) {
                Object val = filterBy.get("author").getFilterValue();
                this.authorFilter = val != null ? val.toString() : null;
            }
        }

        // Busca os dados da página
        Page<FileTableDTO> result = fileService.searchFiles(
                page,
                pageSize,
                nameFilter,
                authorFilter,
                sortField,
                direction);

        // Define o total de registros para o paginator
        setRowCount((int) result.getTotalElements());

        return result.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {

        String name = null;
        String author = null;

        if (filterBy != null) {
            if (filterBy.containsKey("fileName")) {
                Object val = filterBy.get("fileName").getFilterValue();
                name = val != null ? val.toString() : null;
            }
            if (filterBy.containsKey("author")) {
                Object val = filterBy.get("author").getFilterValue();
                author = val != null ? val.toString() : null;
            }
        }

        // Busca apenas o total de registros filtrados via Service
        return fileService.countFiles(name, author);
    }
}
