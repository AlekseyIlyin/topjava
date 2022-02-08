package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class MealDaoImp implements Dao<Meal,Integer>{

    private final Map<Integer,Meal> meals = new HashMap<>(7);

    public MealDaoImp() {
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    private Integer getNewId() {
        if (meals.isEmpty()) {
            return 0;
        } else {
            return Collections.max(meals.keySet()) + 1;
        }
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(getNewId());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal read(Integer id) {
        return meals.get(id);
    }

    @Override
    public Meal update(Meal meal) {
        meals.put(meal.getId(), meal);
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
