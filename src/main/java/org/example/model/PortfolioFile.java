package org.example.model;

import javax.persistence.*;

@Entity
public class PortfolioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] fileData;

    @Column(length = 255)
    private String fileName;

    @Column(length = 10)
    private String fileExtension;

    @ManyToOne
    @JoinColumn(name = "graduate_id", nullable = false)
    private Graduate graduate;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Graduate getGraduate() {
        return graduate;
    }

    public void setGraduate(Graduate graduate) {
        this.graduate = graduate;
    }
}
