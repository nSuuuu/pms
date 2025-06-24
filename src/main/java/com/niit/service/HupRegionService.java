package com.niit.service;

import com.niit.entity.HupRegion;
import com.niit.repository.HupRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HupRegionService {
    @Autowired
    private HupRegionRepository hupRegionRepository;

    public List<HupRegion> searchRegions(String keyword) {
        // 处理拼音简写搜索，如"nxyc"转为"ningxia yinchuan"
        if (keyword.matches("^[a-zA-Z]{2,}$")) {
            keyword = keyword.toLowerCase();
            return hupRegionRepository.findByNameContainingOrPinyinContainingOrCodeContaining(keyword);
        }
        return hupRegionRepository.findByNameContainingOrPinyinContainingOrCodeContaining(keyword);
    }
}