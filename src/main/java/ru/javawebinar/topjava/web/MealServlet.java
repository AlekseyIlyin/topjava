package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealService mealService;
    public MealServlet() {
        this.mealService = new MealService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getHttpServletMapping().getMatchValue()) {
            case "editmeal" : {
                String stringId = request.getParameter("id");
                if (stringId == null || stringId.isEmpty()) {
                    log.debug("preparing new meal");
                    request.setAttribute("meal", new Meal());
                } else {
                    log.debug("preparing edit meal");
                    try {
                        request.setAttribute("meal", mealService.getById(Integer.parseInt(stringId)));
                    } catch (NumberFormatException e) {
                        log.error(e.toString());
                        request.setAttribute("meals", mealService.getMealToListFromMealList(mealService.getAllMeals()));
                        request.getRequestDispatcher("/meals.jsp").forward(request, response);
                    }
                }
                log.debug("redirect to editmeal");
                request.getRequestDispatcher("/editmeal.jsp").forward(request, response);
                break;
            }
            case "deletemeal" : {
                String stringId = request.getParameter("id");
                if (stringId != null && !stringId.isEmpty()) {
                    mealService.deleteById(Integer.parseInt(stringId));
                }
                log.debug("redirect to meals");
                request.setAttribute("meals", mealService.getMealToListFromMealList(mealService.getAllMeals()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            }
            default: {
                log.debug("redirect to meals");
                request.setAttribute("meals", mealService.getMealToListFromMealList(mealService.getAllMeals()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        Integer calories = Integer.parseInt(request.getParameter("calories"));
        mealService.saveMeal(new Meal(id, dateTime, description, calories));
        log.debug("meal saved");
        request.setAttribute("meals", mealService.getMealToListFromMealList(mealService.getAllMeals()));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        log.debug("redirect to meals");
    }
}
