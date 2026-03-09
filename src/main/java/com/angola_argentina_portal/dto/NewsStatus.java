package com.angola_argentina_portal.dto;


public enum NewsStatus {

    DRAFT,
    PUBLISHED,
    ARCHIVED;

        // Método para verificar se o status é PUBLISHED
    public boolean isPublished() {
        return this == PUBLISHED;
    }

    // Método para verificar se o status é DRAFT
    public boolean isDraft() {
        return this == DRAFT;
    }

    // Método para verificar se o status é ARCHIVED
    public boolean isArchived() {
        return this == ARCHIVED;
    }
    
}
