package com.example.buisnessservice.api.logic;

import com.example.api.LogicTasksApi;
import com.example.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class LogicTasksApiController implements LogicTasksApi {

    private final LogicTasksService logicTasksService;

    public LogicTasksApiController(LogicTasksService logicTasksService) {
        this.logicTasksService = logicTasksService;
    }

    @Override
    public ResponseEntity<Void> checkPalindrome(CheckPalindromeRequest checkPalindromeRequest) {
        boolean result = logicTasksService.isPalindrome(checkPalindromeRequest.getText());
        log.info("Проверка палиндрома: текст='{}', результат={}", checkPalindromeRequest.getText(), result);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> calculateFactorial(CalculateFactorialRequest calculateFactorialRequest) {
        long result = logicTasksService.factorial(calculateFactorialRequest.getNumber());
        log.info("Факториал: число={}, результат={}", calculateFactorialRequest.getNumber(), result);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> calculateFibonacci(CalculateFibonacciRequest calculateFibonacciRequest) {
        long result = logicTasksService.fibonacci(calculateFibonacciRequest.getIndex());
        log.info("Число Фибоначчи: индекс={}, результат={}", calculateFibonacciRequest.getIndex(), result);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> isPrimeNumber(CalculateFactorialRequest calculateFactorialRequest) {
        boolean result = logicTasksService.isPrime(calculateFactorialRequest.getNumber());
        log.info("Проверка на простоту: число={}, результат={}", calculateFactorialRequest.getNumber(), result);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> sortArray(SortArrayRequest sortArrayRequest) {
        String order = sortArrayRequest.getOrder() != null ? sortArrayRequest.getOrder().name() : "asc";
        List<Integer> sorted = logicTasksService.sort(
                sortArrayRequest.getArray(),
                order
        );
        log.info("Сортировка массива: исходный={}, порядок={}, результат={}",
                sortArrayRequest.getArray(),
                order,
                sorted);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}