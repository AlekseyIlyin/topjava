<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html lang="ru">
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal</h2>
<p>${meal.id == '-1' ? 'Новый' : meal.id}</p>
<form action="meal" method="post">
    <input type="text" name="id" hidden value="${meal.id}">
    <p>
        <label for="dateTime">dateTime</label>
        <input id="dateTime" type="datetime-local" name="dateTime" value="${meal.dateTime}">
    </p>
    <p>
        <label for="description">description</label>
        <input id="description" type="text" name="description" value="${meal.description}">
    </p>
    <p>
        <label for="calories">calories</label>
        <input id="calories" type="number" min="0" name="calories" value="${meal.calories}">
    </p>
    <p>
        <button type="submit">Записать</button>
    </p>
</form>

</body>
</html>