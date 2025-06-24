package com.niit.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hup_region")
@Data
public class HupRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer pid;
    private String shortname;
    private String name;
    private String merger_name;
    private Integer level;
    private String pinyin;
    private String code;
    private String zip_code;
    private String first;
    private String lng;
    private String lat;
}