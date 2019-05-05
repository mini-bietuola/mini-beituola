package com.netease.mini.bietuola.mapper;

import org.springframework.stereotype.Component;

@Component
public interface CategoryMapper {

    int selectScoreById(Long id);

}
