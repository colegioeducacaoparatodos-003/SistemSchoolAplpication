package com.angola_argentina_portal.lazy;


import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.angola_argentina_portal.model.Announcement;
import com.angola_argentina_portal.repository.AnnouncementRepository;

import java.util.List;
import java.util.Map;

import javax.swing.SortOrder;

public class AnnouncementLazyModel
        extends LazyDataModel<Announcement> {

    private final AnnouncementRepository repository;

    public AnnouncementLazyModel(AnnouncementRepository repository) {
        this.repository = repository;
    }

    public List<Announcement> load(int first, int pageSize,
            String sortField, SortOrder sortOrder,
            Map<String, Object> filters) {

        Pageable pageable = PageRequest.of(first / pageSize, pageSize);
        Page<Announcement> page = repository.findAll(pageable);

        setRowCount((int) page.getTotalElements());
        return page.getContent();
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public List<Announcement> load(int first, int pageSize, Map<String, SortMeta> sortBy,
            Map<String, FilterMeta> filterBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public String getRowKey(Announcement entity) {
        return String.valueOf(entity.getPkAnnouncement());
    }

    @Override
    public Announcement getRowData(String rowKey) {
        int id = Integer.parseInt(rowKey);

        if (this.getWrappedData() != null) {
            for (Announcement entity : this.getWrappedData()) {
                if (entity.getPkAnnouncement() == id) {
                    return entity;
                }
            }
        }
        return null;
    }
}
