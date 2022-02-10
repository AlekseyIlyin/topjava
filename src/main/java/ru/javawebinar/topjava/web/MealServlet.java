package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDaoCrud;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final String LIST_MEAL_REDIRECT = "/meals?action=meals";
    private static final Logger log = getLogger(MealServlet.class);
    private Dao<Meal, Integer> dao;
    private MealsUtil mealsUtil;

    public MealServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.dao = new MealDaoCrud();
        this.mealsUtil = new MealsUtil();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(mealId);
            try {
                response.sendRedirect(request.getContextPath() + LIST_MEAL_REDIRECT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            request.setAttribute("meal", dao.read(mealId));
            redirect(request, response, INSERT_OR_EDIT);
        } else if (action.equalsIgnoreCase("insert")) {
            redirect(request, response, INSERT_OR_EDIT);
        } else if (action.equalsIgnoreCase("meals")) {
            redirect(request, response, LIST_MEAL);
        } else {
            redirect(request, response, LIST_MEAL);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String idStr = request.getParameter("id");
        Integer id = null;
        if (idStr != null && !idStr.isEmpty()) {
            id = Integer.parseInt(idStr);
        }
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (id == null) {
            dao.create(new Meal(dateTime, description, calories));
        } else {
            dao.update(new Meal(id, dateTime, description, calories));
        }
        redirect(request, response, LIST_MEAL);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String forward) {
        log.debug("redirect to: " + forward);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(forward);
        if (forward.equalsIgnoreCase(LIST_MEAL)) {
            request.setAttribute("meals", mealsUtil.getAllMealTo(dao.getAll()));
        }
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

}
