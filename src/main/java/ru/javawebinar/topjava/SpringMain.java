package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            // test meal controller and repo
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

            // getAll
            List<MealTo> mealList = mealRestController.getAll();
            System.out.println("method getAll is worked: " + !mealList.isEmpty());

            int idMeal = mealList.get(0).getId();

            // get
            Meal meal = mealRestController.get(idMeal);
            System.out.println("method get is worked: " + (meal != null));

            // create
            Meal newMeal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, 1);
            Meal mealFromRepo = mealRestController.create(newMeal);
            System.out.println("method create is worked: " + (mealRestController.get(mealFromRepo.getId()) != null));

            // update
            final int testCalories = 99_999;
            Meal testUpdateMeal = new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), testCalories, meal.getUserId());
            mealRestController.update(testUpdateMeal, idMeal);
            meal = mealRestController.get(idMeal);
            System.out.println("method update is worked: " + (meal.getCalories() == testCalories));

            // delete
            mealRestController.delete(idMeal);
            try {
                mealRestController.get(idMeal);
            } catch (NullPointerException e) {
                System.out.println("method delete is worked: true");
            }

            // delete alien meal
            try {
                mealRestController.delete(8); // alien meal with userId=2
            } catch (NullPointerException e) {
                System.out.println("method delete alien meal is worked: true");
            }
        }
    }
}
