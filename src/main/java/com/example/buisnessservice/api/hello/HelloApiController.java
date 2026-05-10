package com.example.buisnessservice.api.hello;


import com.example.api.HelloApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApiController implements HelloApi {
    @Autowired
    private HelloApiService helloApiService;

    public HelloApiController(HelloApiService helloApiService) {
        this.helloApiService = helloApiService;
    }


    @Override
    public ResponseEntity<Void> getHello() {

        helloApiService.processHello();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
