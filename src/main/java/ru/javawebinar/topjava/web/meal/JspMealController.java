package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@RequestMapping()
@Controller
public class JspMealController extends MealAbstractRestController {
    private static final Logger log = LoggerFactory.getLogger(MealAbstractRestController.class);

    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping(value = "/meals")
    protected String doPost(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "dateTime") String strDateTime,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "calories") int calories,
            HttpServletRequest request) {
        log.info("post");
        final Meal meal = new Meal(LocalDateTime.parse(strDateTime), description, calories);
        if (id == null) {
            create(meal);
        } else {
            update(meal, id);
        }
        return "redirect:/meals";
    }

    @GetMapping("/mealForm")
    public String doMealForm(
            @RequestParam(name = "action") String action,
            @RequestParam(name = "id", required = false) Integer id,
            Model model
    ) {
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals")
    public String doAction(
            @RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "startTime", required = false) LocalTime startTime,
            @RequestParam(name = "endTime", required = false) LocalTime endTime,
            Model model,
            HttpServletRequest request
    ) {
        switch (action == null ? "all" : action) {
            case "delete" -> {
                delete(id);
                return "redirect:meals";
            }
            case "create", "update" -> {
                request.setAttribute("action", action);
                request.setAttribute("id", id);
                return "forward:/mealForm";
            }
            case "filter" -> {
                model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
                return "forward:/meals";
            }
            default -> {
                model.addAttribute("meals", getAll());
            }
        }
        return "meals";
    }
}
