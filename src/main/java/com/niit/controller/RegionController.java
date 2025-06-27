package com.niit.controller;


import com.niit.entity.Region;
import com.niit.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("/search")
    public ResponseEntity<List<Region>> searchRegions(@RequestParam String q) {
        try {
            List<Region> regions = regionRepository.findByNameOrPinyinOrCodeContaining(q);
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            e.printStackTrace(); // 或使用日志框架
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}