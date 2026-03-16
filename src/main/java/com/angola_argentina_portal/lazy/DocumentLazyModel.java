package com.angola_argentina_portal.lazy;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.service.DocumentService;

public class DocumentLazyModel extends LazyDataModel<DocumentTableDTO> {
<<<<<<< HEAD

     private final DocumentService service;
    private final String referenceType;
    private final int referenceId;
=======
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35

    private final DocumentService service;

    public DocumentLazyModel(DocumentService service) {
        this.service = service;
    }

    @Override
    public List<DocumentTableDTO> load(
            int first,
            int pageSize,
            Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {

        int page = first / pageSize;

        Sort sort = Sort.unsorted();

<<<<<<< HEAD
        if (sortBy != null && !sortBy.isEmpty()) {
=======
        if (!sortBy.isEmpty()) {
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35

            SortMeta meta = sortBy.values().iterator().next();

            sort = Sort.by(
                    meta.getOrder().isAscending()
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC,
<<<<<<< HEAD
                    meta.getField()
            );
        }

        Page<DocumentTableDTO> result =
                service.findLazy(referenceType, referenceId, page, pageSize, sort);

        this.setRowCount((int) result.getTotalElements());
=======
                    meta.getField());
        }

        Page<DocumentTableDTO> result = service.findLazy(page, pageSize, sort);

        setRowCount((int) result.getTotalElements());
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35

        return result.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {

        Page<DocumentTableDTO> page = service.findLazy(0, 1, Sort.unsorted());

        return (int) page.getTotalElements();
    }

    @Override
    public String getRowKey(DocumentTableDTO dto) {
        return String.valueOf(dto.getPkDocument());
    }
    // Aqui

    @Override
    public DocumentTableDTO getRowData(String rowKey) {

        int id = Integer.parseInt(rowKey);

        List<DocumentTableDTO> list = (List<DocumentTableDTO>) getWrappedData();

        if (list != null) {
            for (DocumentTableDTO dto : list) {
                if (dto.getPkDocument() == id) {
                    return dto;
                }
            }
        }

        return null;
    }
<<<<<<< HEAD

    @Override
    public int count(Map<String, FilterMeta> filterBy) {

        Page<DocumentTableDTO> result =
                service.findLazy(referenceType, referenceId, 0, Integer.MAX_VALUE, Sort.unsorted());

        return (int) result.getTotalElements();
    }
    
=======
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
}
