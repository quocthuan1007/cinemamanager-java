package com.utc2.cinema.service;

import com.utc2.cinema.dao.FoodDao;
import com.utc2.cinema.model.entity.Food;

import java.util.List;

public class FoodService {
    public static List<Food> getAllFoodData()
    {
        return FoodDao.getAllFoods();
    }
    public static Food insertFood(Food food)
    {
        FoodDao a = new FoodDao();
        return a.insertFood(food);
    }
    public static boolean updateFood(Food food)
    {
        FoodDao a = new FoodDao();
        return a.updateFood(food);
    }
    public static boolean deleteFood(int id)
    {
        FoodDao a= new FoodDao();
        return a.deleteFood(id);
    }
}
