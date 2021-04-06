package com.java.learn.service.impl;

import com.java.learn.mapper.CarouselMapper;
import com.java.learn.pojo.Carousel;
import com.java.learn.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> getAllCarousel(int isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        example.and().andEqualTo("isShow", isShow);

        return carouselMapper.selectByExample(example);
    }
}
