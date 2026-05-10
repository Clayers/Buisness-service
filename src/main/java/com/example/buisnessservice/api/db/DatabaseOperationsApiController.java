package com.example.buisnessservice.api.db;

import com.example.api.DatabaseOperationsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DatabaseOperationsApiController implements DatabaseOperationsApi {

    private final DatabaseOperationsService dbService;

    public DatabaseOperationsApiController(DatabaseOperationsService dbService) {
        this.dbService = dbService;
    }

    @Override
    public ResponseEntity<Void> generateAllNsi() {
        dbService.generateAllNsi();
        log.info("NSI справочники сгенерированы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> generateRandomUsers() {
        dbService.generateRandomUsers();
        log.info("Пользователи сгенерированы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> generateWorkForAllUsers() {
        dbService.generateWorkForAllUsers();
        log.info("Места работы сгенерированы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> generateRandomProducts() {
        dbService.generateRandomProducts();
        log.info("Товары сгенерированы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> generateRandomPurchases() {
        dbService.generateRandomPurchases();
        log.info("Покупки сгенерированы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> generateMissingAccounts() {
        dbService.generateMissingAccounts();
        log.info("Счета созданы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> paySalaryToAllUsers() {
        dbService.paySalaryToAllUsers();
        log.info("Зарплата начислена");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> generateAllData() {
        dbService.generateAllData();
        log.info("Все данные сгенерированы");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> clearAllTables() {
        dbService.clearAllTables();
        log.info("Все таблицы очищены");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> showStatistics() {
        dbService.showStatistics();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
