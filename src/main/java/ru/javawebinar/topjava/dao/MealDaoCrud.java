package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoCrud implements Dao<Meal, Integer> {
    private final ConcurrentMap<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger index = new AtomicInteger(0);


    public MealDaoCrud() {
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(index.incrementAndGet());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal read(Integer id) {
        return meals.get(id);
    }

    @Override
    public Meal update(Meal meal) {
        if (meal.getId() != null) {
            meals.put(meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public boolean delete(Integer id) {
        return meals.remove(id) != null;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

}
