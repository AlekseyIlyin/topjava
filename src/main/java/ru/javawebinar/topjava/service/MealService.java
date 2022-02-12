package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        mealFoundAndEnableAccess(meal);
        return repository.save(meal);
    }

    public void delete(int id) {
        Meal meal = checkNotFoundWithId(repository.get(id), id);
        mealFoundAndEnableAccess(meal);
    }

    public Meal get(int id) {
        Meal meal = checkNotFoundWithId(repository.get(id), id);
        return mealFoundAndEnableAccess(meal);
    }

    public Collection<MealTo> getAll() {
        return MealsUtil.getTos(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Collection<MealTo> getAllUserMeals(int userId) {
        return getAll().stream()
                .filter(meal -> meal.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void update(Meal meal) {
        mealFoundAndEnableAccess(meal);
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    private Meal mealFoundAndEnableAccess(Meal meal) {
        if (meal == null || meal.getUserId() != SecurityUtil.authUserId()) {
            throw new NotFoundException("meal not found or access denied");
        }
        return meal;
    }

}