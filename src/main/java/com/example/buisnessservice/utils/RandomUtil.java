package com.example.buisnessservice.utils;

import java.util.List;
import java.util.Random;

/**
 * Утилита для генерации случайных значений
 */
public class RandomUtil {

    private static final Random random = new Random();

    /**
     * Генерирует случайное целое число в диапазоне [min, max]
     * @param min минимальное значение (включительно)
     * @param max максимальное значение (включительно)
     * @return случайное число
     */
    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Генерирует случайное длинное целое число в диапазоне [min, max]
     */
    public static long randomLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + (long) (random.nextDouble() * (max - min + 1));
    }

    /**
     * Генерирует случайное число с плавающей точкой в диапазоне [min, max)
     */
    public static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + random.nextDouble() * (max - min);
    }

    /**
     * Возвращает случайный элемент из списка
     * @param list список, из которого выбирается элемент
     * @return случайный элемент или null, если список пуст
     */
    public static <T> T randomFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Генерирует случайную зарплату в диапазоне от 30 000 до 200 000
     * @return случайная зарплата
     */
    public static double randomSalary() {
        return randomDouble(30000.0, 200000.0);
    }

    /**
     * Возвращает true с заданной вероятностью
     * @param probability вероятность от 0.0 до 1.0
     */
    public static boolean chance(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        return random.nextDouble() < probability;
    }
}
