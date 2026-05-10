package com.example.buisnessservice.api.logic;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogicTasksService {

    /**
     * Проверка, является ли строка палиндромом
     * Учитывает только буквы и цифры, игнорирует регистр и пробелы
     */
    public boolean isPalindrome(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        String cleaned = text.toLowerCase().replaceAll("[^a-zа-яё0-9]", "");
        String reversed = new StringBuilder(cleaned).reverse().toString();
        return cleaned.equals(reversed);
    }

    /**
     * Вычисление факториала числа (рекурсивно)
     */
    public long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Факториал отрицательного числа не определён");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /**
     * Вычисление числа Фибоначчи по индексу
     */
    public long fibonacci(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Индекс не может быть отрицательным");
        }
        if (index == 0) return 0;
        if (index == 1) return 1;
        long a = 0, b = 1;
        for (int i = 2; i <= index; i++) {
            long next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

    /**
     * Проверка числа на простоту
     */
    public boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }
        return true;
    }

    /**
     * Сортировка массива чисел
     * @param array исходный массив
     * @param order "asc" или "desc"
     * @return отсортированный массив
     */
    public List<Integer> sort(List<Integer> array, String order) {
        if (array == null || array.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>(array);
        result.sort((a, b) -> {
            if ("desc".equalsIgnoreCase(order)) {
                return b.compareTo(a);
            }
            return a.compareTo(b);
        });
        return result;
    }
}
