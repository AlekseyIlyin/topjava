package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        } else if (meal.getUserId() != SecurityUtil.authUserId()) {
            return null;
        } else {
            // handle case: update, but not present in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int id) {
        Meal meal = repository.get(id);
        if (meal.getUserId() == id) {
            repository.remove(id);
            return repository.remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id) {
        Meal meal = repository.get(id);
        return meal.getUserId() == SecurityUtil.authUserId() ? meal : null;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values().stream()
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).collect(Collectors.toList());
    }

}

