package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDaoImp;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.web.MealServlet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealService {
    private static final Logger log = getLogger(MealServlet.class);
    public final int CALORIES_PER_DAY = 2000;
    private final Dao<Meal,Integer> repository = new MealDaoImp();

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public Meal getById (Integer id) {
        return repository.read(id);
    }

    public List<Meal> getAllMeals() {
        log.info("get all Meals from repo");
        return repository.getAll();
    }

    public void saveMeal(Meal meal) {
        if (meal.getId() == -1) {
            repository.create(meal);
        } else {
            repository.update(meal);
        }
    }

    public void deleteById(Integer id) {
        repository.delete(id);
    }

    public List<MealTo> getMealToListFromMealList(List<Meal> meals) {
        final Map<LocalDate, Integer> caloriesSumByDate = meals.parallelStream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), Collectors.summingInt(Meal::getCalories)));
        return meals.stream()
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }

}
