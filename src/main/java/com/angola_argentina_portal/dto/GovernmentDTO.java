package com.angola_argentina_portal.dto;

import java.time.LocalDateTime;

public class GovernmentDTO {

    // Response DTO
    public class GovernmenResponseDTO {

        private Long id;
        private String fullName;
        private String typeEm;
        private String title;
        private String subTitle;
        private String description;
        private LocalDateTime inicio;
        private LocalDateTime termino;

        public GovernmenResponseDTO() {
        }

        public GovernmenResponseDTO(Long id, String fullName, String typeEm,
                String title, String subTitle,
                String description,
                LocalDateTime inicio, LocalDateTime termino) {
            this.id = id;
            this.fullName = fullName;
            this.typeEm = typeEm;
            this.title = title;
            this.subTitle = subTitle;
            this.description = description;
            this.inicio = inicio;
            this.termino = termino;
        }

        public Long getId() {
            return this.id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFullName() {
            return this.fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getTypeEm() {
            return this.typeEm;
        }

        public void setTypeEm(String typeEm) {
            this.typeEm = typeEm;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return this.subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getInicio() {
            return this.inicio;
        }

        public void setInicio(LocalDateTime inicio) {
            this.inicio = inicio;
        }

        public LocalDateTime getTermino() {
            return this.termino;
        }

        public void setTermino(LocalDateTime termino) {
            this.termino = termino;
        }
    }

    // Create DTO
    public class GovernmentCreateDTO {

        private String fullName;
        private String typeEm;
        private String title;
        private String subTitle;
        private String description;
        private LocalDateTime inicio;
        private LocalDateTime termino;

        public GovernmentCreateDTO() {
        }

        // getters and setters

        public String getFullName() {
            return this.fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getTypeEm() {
            return this.typeEm;
        }

        public void setTypeEm(String typeEm) {
            this.typeEm = typeEm;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return this.subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getInicio() {
            return this.inicio;
        }

        public void setInicio(LocalDateTime inicio) {
            this.inicio = inicio;
        }

        public LocalDateTime getTermino() {
            return this.termino;
        }

        public void setTermino(LocalDateTime termino) {
            this.termino = termino;
        }

    }

}
