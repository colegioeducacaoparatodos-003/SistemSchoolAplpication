package com.angola_argentina_portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "files")
public class FileDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    private Long size;

    private String author;

    private LocalDateTime createdAt;

    // bytes do arquivo
    @Lob
    private byte[] data;

    // getters e setters

    public FileDocument() {
    }

    public FileDocument(Long id, String fileName, String contentType, Long size, String author, LocalDateTime createdAt, byte[] data) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.author = author;
        this.createdAt = createdAt;
        this.data = data;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public FileDocument id(Long id) {
        setId(id);
        return this;
    }

    public FileDocument fileName(String fileName) {
        setFileName(fileName);
        return this;
    }

    public FileDocument contentType(String contentType) {
        setContentType(contentType);
        return this;
    }

    public FileDocument size(Long size) {
        setSize(size);
        return this;
    }

    public FileDocument author(String author) {
        setAuthor(author);
        return this;
    }

    public FileDocument createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public FileDocument data(byte[] data) {
        setData(data);
        return this;
    }

    // @Override
    // public boolean equals(Object o) {
    //   return EqualsBuilder.reflectionEquals(this, o);
    // }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, contentType, size, author, createdAt, data);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", size='" + getSize() + "'" +
            ", author='" + getAuthor() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
    

}
