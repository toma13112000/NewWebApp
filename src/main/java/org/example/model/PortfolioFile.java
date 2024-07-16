package org.example.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PortfolioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String filePath;

    @Column(length = 255, nullable = false)
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "graduate_id", nullable = false)
    private Graduate graduate;
}
