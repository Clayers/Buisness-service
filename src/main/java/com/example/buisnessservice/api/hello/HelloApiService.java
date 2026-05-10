package com.example.buisnessservice.api.hello;

import com.example.buisnessservice.utils.BusinessException;

import com.example.dao.dao.main.MainDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class HelloApiService {

    @Autowired
    private MainDAO mainDAO;

    public void processHello() {
        if (Math.random() > 0.5) {
            log.error("Мимо");
            throw new BusinessException("Something went wrong");
        }

        log.warn("Привет дорогой");
    }


}

