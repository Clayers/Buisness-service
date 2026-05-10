package com.example.buisnessservice.api.db;


import com.example.buisnessservice.utils.RandomUtil;
import com.example.dao.dao.main.MainDAO;
import com.example.dao.objects.nsi.*;
import com.example.dao.objects.pub.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DatabaseOperationsService {

    private final MainDAO mainDAO;

    public DatabaseOperationsService(MainDAO mainDAO) {
        this.mainDAO = mainDAO;
    }

    /**
     * Генерация всех NSI справочников (профессии, компании, типы товаров, словарь имен)
     * Если данные уже есть - выводим ошибку в лог и не заполняем
     */
    public void generateAllNsi() {
        // 1. Profession
        if (mainDAO.count(Profession.class) > 0) {
            log.error("NSI Profession: данные уже есть, пропускаем");
        } else {
            List<String> names = List.of("Программист", "Водитель", "Менеджер", "Инженер", "Бухгалтер", "Маркетолог", "Дизайнер", "Аналитик");
            for (String name : names) {
                Profession p = new Profession();
                p.setName(name);
                mainDAO.addNewEntity(p);
            }
            log.info("NSI Profession: заполнено {} записей", names.size());
        }

        // 2. Company
        if (mainDAO.count(Company.class) > 0) {
            log.error("NSI Company: данные уже есть, пропускаем");
        } else {
            List<String> names = List.of("ООО Ромашка", "ИП Иванов", "Корпорация К", "ОАО Солнышко", "ООО ТехноСервис", "ЗАО Альянс", "ООО Интеграл");
            for (String name : names) {
                Company c = new Company();
                c.setName(name);
                mainDAO.addNewEntity(c);
            }
            log.info("NSI Company: заполнено {} записей", names.size());
        }

        // ProductType (справочник товаров)
        if (mainDAO.count(ProductType.class) > 0) {
            log.error("NSI ProductType: данные уже есть, пропускаем");
        } else {
            List<String> productNames = List.of(
                    "Смартфон", "Ноутбук", "Наушники", "Футболка", "Джинсы",
                    "Куртка", "Хлеб", "Молоко", "Сыр", "Война и мир",
                    "Преступление и наказание", "Стул", "Стол"
            );
            for (String name : productNames) {
                ProductType pt = new ProductType();
                pt.setName(name);
                mainDAO.addNewEntity(pt);
            }
            log.info("NSI ProductType: заполнено {} записей", productNames.size());
        }

        // 4. NameDictionary
        if (mainDAO.count(NameDictionary.class) > 0) {
            log.error("NSI NameDictionary: данные уже есть, пропускаем");
        } else {
            // Фамилии
            List<String> lastNames = List.of("Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов", "Попов", "Васильев");
            for (String value : lastNames) {
                NameDictionary nd = new NameDictionary();
                nd.setFieldType(FieldTypeEnum.LASTNAME);
                nd.setValue(value);
                mainDAO.addNewEntity(nd);
            }

            // Имена
            List<String> firstNames = List.of("Иван", "Пётр", "Алексей", "Дмитрий", "Егор", "Михаил", "Александр");
            for (String value : firstNames) {
                NameDictionary nd = new NameDictionary();
                nd.setFieldType(FieldTypeEnum.FIRSTNAME);
                nd.setValue(value);
                mainDAO.addNewEntity(nd);
            }

            // Отчества
            List<String> patronymics = List.of("Иванович", "Петрович", "Сидорович", "Алексеевич", "Дмитриевич", "Михайлович");
            for (String value : patronymics) {
                NameDictionary nd = new NameDictionary();
                nd.setFieldType(FieldTypeEnum.PATRONYMIC);
                nd.setValue(value);
                mainDAO.addNewEntity(nd);
            }

            log.info("NSI NameDictionary: заполнено {} записей",
                    lastNames.size() + firstNames.size() + patronymics.size());
        }
    }

    /**
     * Генерация случайных пользователей (10 штук)
     * Имена, фамилии, отчества берутся из справочника NameDictionary
     */
    public void generateRandomUsers() {
        // Проверяем, есть ли данные в NameDictionary
        if (mainDAO.count(NameDictionary.class) == 0) {
            log.error("NameDictionary пуст, сначала выполните generateAllNsi()");
            return;
        }

        // Получаем все фамилии, имена, отчества из справочника
        List<NameDictionary> lastNames = mainDAO.getNsiDao().findNameDictionaryByFieldType(FieldTypeEnum.LASTNAME);
        List<NameDictionary> firstNames = mainDAO.getNsiDao().findNameDictionaryByFieldType(FieldTypeEnum.FIRSTNAME);
        List<NameDictionary> patronymics = mainDAO.getNsiDao().findNameDictionaryByFieldType(FieldTypeEnum.PATRONYMIC);

        // Проверяем, что данные есть
        if (lastNames.isEmpty() || firstNames.isEmpty() || patronymics.isEmpty()) {
            log.error("Не хватает данных в NameDictionary для генерации пользователей");
            return;
        }

        // Получаем все возможные работы из справочника
        List<UserWork> allWorks = mainDAO.getPublicDao().findAllUserWorks();
        if (allWorks.isEmpty()) {
            log.error("Нет справочника работ, сначала выполните generateWorkForAllUsers()");
            return;
        }

        // Генерируем 10 пользователей
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setLastName(RandomUtil.randomFromList(lastNames).getValue());
            user.setFirstName(RandomUtil.randomFromList(firstNames).getValue());
            user.setPatronymic(RandomUtil.randomFromList(patronymics).getValue());

            // Выбираем случайную работу из справочника
            UserWork randomWork = RandomUtil.randomFromList(allWorks);
            user.setCurrentWork(randomWork);

            mainDAO.addNewEntity(user);
            log.debug("Создан пользователь: {} {} {}, работа: {} в {}",
                    user.getLastName(), user.getFirstName(), user.getPatronymic(),
                    randomWork.getProfession().getName(),
                    randomWork.getCompany().getName());
        }

        log.info("Генерация пользователей: создано 10 записей");
    }

    /**
     * Генерация 5 случайных записей (профессия + компания)
     * Если такая комбинация уже существует - пропускаем и пишем в лог
     */
    public void generateWorkForAllUsers() {
        // Проверяем, есть ли профессии и компании в NSI
        List<Profession> professions = mainDAO.getNsiDao().findAllProfessions();
        List<Company> companies = mainDAO.getNsiDao().findAllCompanies();

        if (professions.isEmpty() || companies.isEmpty()) {
            log.error("Нет профессий или компаний, сначала выполните generateAllNsi()");
            return;
        }

        int generatedCount = 0;
        int skippedCount = 0;

        // Генерируем 5 записей справочника работ
        for (int i = 0; i < 5; i++) {
            Profession randomProfession = RandomUtil.randomFromList(professions);
            Company randomCompany = RandomUtil.randomFromList(companies);

            // Проверяем, существует ли уже такая комбинация профессия+компания
            boolean exists = mainDAO.getPublicDao().isUserWorkExists(randomProfession, randomCompany);

            if (exists) {
                log.debug("Комбинация уже существует: профессия={}, компания={}, пропускаем",
                        randomProfession.getName(), randomCompany.getName());
                skippedCount++;
                continue;
            }

            // Создаём запись в справочнике работ (без пользователя)
            UserWork userWork = new UserWork();
            userWork.setProfession(randomProfession);
            userWork.setCompany(randomCompany);
            userWork.setSalary(BigDecimal.valueOf(RandomUtil.randomSalary()));
            mainDAO.addNewEntity(userWork);
            generatedCount++;
        }

        log.info("Генерация справочника работ: создано {}, пропущено {}", generatedCount, skippedCount);
    }

    /**
     * Генерация товаров
     * Если товар уже существует - увеличиваем его количество (поставка)
     * Если товара нет - создаём новую запись
     */
    public void generateRandomProducts() {
        // Проверяем, есть ли товары в NSI
        List<ProductType> productTypes = mainDAO.getNsiDao().findAllProductTypes();
        if (productTypes.isEmpty()) {
            log.error("Нет товаров в NSI, сначала выполните generateAllNsi()");
            return;
        }

        // Получаем все существующие товары одной выборкой
        List<Product> existingProducts = mainDAO.getPublicDao().findAllProducts();

        // Создаём мапу для быстрого поиска (Integer id типа товара -> Product)
        Map<Long, Product> productMap = existingProducts.stream()
                .collect(Collectors.toMap(
                        p -> p.getProductType().getId(),
                        p -> p
                ));

        int createdCount = 0;
        int updatedCount = 0;

        for (ProductType productType : productTypes) {
            Product existingProduct = productMap.get(productType.getId());

            if (existingProduct != null) {
                // Товар есть - увеличиваем количество (поставка)
                int additionalQuantity = RandomUtil.randomInt(5, 50);
                int newQuantity = existingProduct.getQuantity() + additionalQuantity;
                existingProduct.setQuantity(newQuantity);
                mainDAO.merge(existingProduct);

                // Записываем историю
                StockHistory stockHistory = new StockHistory();
                stockHistory.setProduct(existingProduct);
                stockHistory.setQuantityChange(additionalQuantity);
                stockHistory.setReason("PURCHASE");
                mainDAO.getPublicDao().save(stockHistory);

                updatedCount++;
                log.debug("Поставка товара: {}, количество увеличено на {}, теперь {}",
                        productType.getName(), additionalQuantity, newQuantity);
            } else {
                // Товара нет - создаём новый
                double price = RandomUtil.randomDouble(10.0, 1000.0);
                Product newProduct = new Product();
                newProduct.setProductType(productType);
                newProduct.setPrice(BigDecimal.valueOf(price));
                newProduct.setQuantity(RandomUtil.randomInt(10, 100));
                mainDAO.addNewEntity(newProduct);
                createdCount++;
                log.debug("Создан новый товар: {}, цена: {}", productType.getName(), price);
            }
        }

        log.info("Генерация товаров: создано {}, обновлено (поставка) {}", createdCount, updatedCount);
    }

    /**
     * Генерация случайных покупок
     * Случайные пользователи покупают случайные товары в случайном количестве
     */
    public void generateRandomPurchases() {
        // Проверяем, есть ли пользователи
        List<User> users = mainDAO.getPublicDao().findAllUsers();
        if (users.isEmpty()) {
            log.error("Нет пользователей, сначала выполните generateRandomUsers()");
            return;
        }

        // Проверяем, есть ли товары
        List<Product> products = mainDAO.getPublicDao().findAllProducts();
        if (products.isEmpty()) {
            log.error("Нет товаров, сначала выполните generateRandomProducts()");
            return;
        }

        // Проверяем, есть ли счета у пользователей
        List<Account> accounts = mainDAO.getPublicDao().findAllAccounts();
        if (accounts.isEmpty()) {
            log.error("Нет счетов, сначала выполните generateMissingAccounts()");
            return;
        }

        int generatedCount = 0;
        int insufficientFundsCount = 0;
        int outOfStockCount = 0;

        // Генерируем от 5 до 20 покупок
        int purchaseCount = RandomUtil.randomInt(5, 20);

        for (int i = 0; i < purchaseCount; i++) {
            // Выбираем случайного пользователя и его счёт
            User randomUser = RandomUtil.randomFromList(users);
            Account userAccount = mainDAO.getPublicDao().findAccountByUser(randomUser);

            if (userAccount == null) {
                insufficientFundsCount++;
                continue;
            }

            // Выбираем случайный товар
            Product randomProduct = RandomUtil.randomFromList(products);

            // Случайное количество от 1 до 5
            int quantity = RandomUtil.randomInt(1, 5);

            // Проверяем, есть ли товар в нужном количестве
            if (randomProduct.getQuantity() < quantity) {
                log.debug("Товара {} недостаточно: нужно {}, есть {}",
                        randomProduct.getProductType().getName(), quantity, randomProduct.getQuantity());
                outOfStockCount++;
                continue;
            }

            // Рассчитываем стоимость
            BigDecimal totalPrice = randomProduct.getPrice().multiply(BigDecimal.valueOf(quantity));

            // Проверяем, хватает ли денег на счету
            if (userAccount.getBalance().compareTo(totalPrice) < 0) {
                log.debug("У пользователя {} недостаточно средств: нужно {}, есть {}",
                        randomUser.getFirstName() + " " + randomUser.getLastName(),
                        totalPrice, userAccount.getBalance());
                insufficientFundsCount++;
                continue;
            }

            // Списание со счёта
            userAccount.setBalance(userAccount.getBalance().subtract(totalPrice));
            mainDAO.merge(userAccount);

            // Уменьшаем количество товара
            randomProduct.setQuantity(randomProduct.getQuantity() - quantity);
            mainDAO.getPublicDao().update(randomProduct);

            // Записываем историю
            StockHistory stockHistory = new StockHistory();
            stockHistory.setProduct(randomProduct);
            stockHistory.setQuantityChange(-quantity);
            stockHistory.setReason("SALE");
            mainDAO.getPublicDao().save(stockHistory);

            // Создаём запись о покупке
            Purchase purchase = new Purchase();
            purchase.setUser(randomUser);
            purchase.setProduct(randomProduct);
            purchase.setQuantity(quantity);
            purchase.setTotalPrice(totalPrice);
            mainDAO.addNewEntity(purchase);

            // Записываем транзакцию
            Transaction transaction = new Transaction();
            transaction.setAccount(userAccount);
            transaction.setAmount(totalPrice.negate());
            transaction.setOperationType("PURCHASE");
            transaction.setDescription("Покупка: " + randomProduct.getProductType().getName() + " x" + quantity);
            mainDAO.addNewEntity(transaction);

            generatedCount++;
            log.debug("Покупка: пользователь={}, товар={}, количество={}, сумма={}",
                    randomUser.getFirstName() + " " + randomUser.getLastName(),
                    randomProduct.getProductType().getName(), quantity, totalPrice);
        }

        log.info("Генерация покупок: создано {}, не хватило денег {}, нет товара {}",
                generatedCount, insufficientFundsCount, outOfStockCount);
    }

    /**
     * Создать счета для всех пользователей, у кого их нет
     */
    public void generateMissingAccounts() {
        List<User> users = mainDAO.getPublicDao().findAllUsers();
        if (users.isEmpty()) {
            log.error("Нет пользователей, сначала выполните generateRandomUsers()");
            return;
        }

        int createdCount = 0;
        int skippedCount = 0;

        for (User user : users) {
            // Проверяем, есть ли уже счёт у пользователя
            Account existingAccount = mainDAO.getPublicDao().findAccountByUser(user);

            if (existingAccount != null) {
                log.debug("Счёт уже существует: пользователь {} {}",
                        user.getFirstName(), user.getLastName());
                skippedCount++;
                continue;
            }

            // Создаём новый счёт с балансом 0
            Account account = new Account();
            account.setUser(user);
            account.setBalance(BigDecimal.ZERO);
            mainDAO.addNewEntity(account);
            createdCount++;
            log.debug("Создан счёт для пользователя: {} {}",
                    user.getFirstName(), user.getLastName());
        }

        log.info("Создание счетов: создано {}, пропущено {}", createdCount, skippedCount);
    }

    /**
     * Начислить зарплату всем пользователям на основе их мест работы
     */
    public void paySalaryToAllUsers() {
        List<User> users = mainDAO.getPublicDao().findAllUsers();
        if (users.isEmpty()) {
            log.error("Нет пользователей, сначала выполните generateRandomUsers()");
            return;
        }

        int paidCount = 0;
        int noWorkCount = 0;

        for (User user : users) {
            UserWork currentWork = user.getCurrentWork();

            if (currentWork == null) {
                log.debug("У пользователя {} {} нет работы",
                        user.getFirstName(), user.getLastName());
                noWorkCount++;
                continue;
            }

            Account account = mainDAO.getPublicDao().findAccountByUser(user);
            if (account == null) {
                log.debug("У пользователя {} {} нет счёта",
                        user.getFirstName(), user.getLastName());
                continue;
            }

            BigDecimal salary = currentWork.getSalary();
            if (salary == null) {
                log.debug("У пользователя {} {} зарплата не указана",
                        user.getFirstName(), user.getLastName());
                continue;
            }

            account.setBalance(account.getBalance().add(salary));
            mainDAO.merge(account);

            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setAmount(salary);
            transaction.setOperationType("SALARY");
            transaction.setDescription("Начисление зарплаты: " + currentWork.getProfession().getName());
            mainDAO.addNewEntity(transaction);

            paidCount++;
            log.debug("Начислена зарплата пользователю {} {}: {}",
                    user.getFirstName(), user.getLastName(), salary);
        }

        log.info("Начисление зарплаты: выплачено {}, нет работы {}", paidCount, noWorkCount);
    }

    public void generateAllData() {
        generateAllNsi();
        generateRandomUsers();
        generateWorkForAllUsers();
        generateRandomProducts();
        generateMissingAccounts();
        log.info("Генерация всех данных завершена");
    }

    public void clearAllTables() {
        // TODO: очистить таблицы (пользователи, работы, покупки, транзакции, счета)
        log.info("Очистка всех таблиц");
    }

    public void showStatistics() {
        // TODO: вывести статистику в лог
        log.info("Статистика:");
        // логика получения статистики
    }
}
