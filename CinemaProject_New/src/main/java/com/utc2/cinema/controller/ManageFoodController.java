package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.Food;
import com.utc2.cinema.service.FilmService;
import com.utc2.cinema.service.FoodService;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.List;

public class ManageFoodController {

    private ObservableList<Food> listFood = FXCollections.observableArrayList();
    private Button addFoodBtn;
    private Button addOrSaveFood;
    private TableColumn<Food, Float> costCol;
    private TextField costFood;
    private Button deleteFoodBtn;
    private TableColumn<Food, String> descripCol;
    private TextArea descripFood;
    private Button editFoodBtn;
    private Pane filmForm1;
    private TableColumn<Food, String> foodCol;
    private Pane foodPane;
    private Pane foodForm;
    private TextField nameFood;
    private TextField searchFood;
    private TableView<Food> tableFood;
    public ManageFoodController(MainManagerController mainMenu) {
        this.addFoodBtn = mainMenu.getAddFoodBtn();
        this.addOrSaveFood = mainMenu.getAddOrSaveFood();
        this.deleteFoodBtn = mainMenu.getDeleteFoodBtn();
        this.editFoodBtn = mainMenu.getEditFoodBtn();

        this.costCol = mainMenu.getCostCol();
        this.descripCol = mainMenu.getDescripCol();
        this.foodCol = mainMenu.getFoodCol();

        this.costFood = mainMenu.getCostFood();
        this.nameFood = mainMenu.getNameFood();
        this.searchFood = mainMenu.getSearchFood();

        this.descripFood = mainMenu.getDescripFood();

        this.tableFood = mainMenu.getTableFood();
        this.foodForm = mainMenu.getFoodForm();
        this.foodPane = mainMenu.getFoodPane();
    }
    public void init() {
        foodCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        descripCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        costCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCost()));

        listFood.setAll(getDataFood());
        tableFood.setItems(listFood);
        tableFood.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Food selectedFood = tableFood.getSelectionModel().getSelectedItem();
                if (selectedFood != null) {
                    onClickEditFood(new ActionEvent());
                    System.out.println("Double click vào món ăn: " + selectedFood.getName());
                }
            }
        });
    }

    private void clearFoodForm() {
        nameFood.clear();
        descripFood.clear();
        costFood.clear();
        tableFood.getSelectionModel().clearSelection();
    }
    public void onClickAddFood(ActionEvent event) {
        if (!foodForm.isVisible()) foodForm.setVisible(true);
        addOrSaveFood.setText("Thêm");
        clearFoodForm();
        addOrSaveFood.setOnAction(eventVip -> {
            try {
                String name = nameFood.getText().trim();
                String description = descripFood.getText().trim();
                String costText = costFood.getText().trim();
                if (name.isEmpty()) {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Vui lòng nhập tên món ăn.");
                    return;
                }
                if (description.isEmpty()) {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Vui lòng nhập mô tả món ăn.");
                    return;
                }
                if (costText.isEmpty()) {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Vui lòng nhập giá món ăn.");
                    return;
                }

                float cost;
                try {
                    cost = Float.parseFloat(costText);
                } catch (NumberFormatException e) {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Giá món ăn phải là số hợp lệ.");
                    return;
                }

                if (name.length() > 100) {
                    CustomAlert.showError("","Có lỗi xảy ra", "Tên món ăn không được vượt quá 100 ký tự.");
                    return;
                }

                if (description.length() > 500) {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Mô tả món ăn không được vượt quá 500 ký tự.");
                    return;
                }

                if (cost <= 0) {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Giá món ăn phải lớn hơn 0.");
                    return;
                }

                Food food = new Food(0, name, description, cost);

                Food foodSQL = FoodService.insertFood(food);
                if (foodSQL != null) {
                    System.out.println("Thêm món ăn thành công!");
                    clearFoodForm();
                    listFood.add(foodSQL);
                } else {
                    CustomAlert.showError("","Có lỗi xảy ra",  "Thêm món ăn thất bại.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                CustomAlert.showError("","Có lỗi xảy ra",  "Đã xảy ra lỗi: " + e.getMessage());
            }
        });
    }

    public void onClickEditFood(ActionEvent event) {
        Food selectedFood = tableFood.getSelectionModel().getSelectedItem();

        if (selectedFood != null) {
            if (!foodForm.isVisible()) foodForm.setVisible(true);
            nameFood.setText(selectedFood.getName());
            descripFood.setText(selectedFood.getDescription());
            costFood.setText(String.valueOf(selectedFood.getCost()));

            addOrSaveFood.setText("Lưu chỉnh sửa");

            addOrSaveFood.setOnAction(e -> {
                try {
                    String name = nameFood.getText().trim();
                    String description = descripFood.getText().trim();
                    String costText = costFood.getText().trim();

                    // Kiểm tra thông tin bắt buộc
                    if (name.isEmpty() || description.isEmpty() || costText.isEmpty()) {
                        CustomAlert.showError("","Có lỗi xảy ra",  "Vui lòng nhập đầy đủ các trường bắt buộc.");
                        return;
                    }

                    // Kiểm tra định dạng giá món ăn
                    float cost;
                    try {
                        cost = Float.parseFloat(costText);
                    } catch (NumberFormatException ex) {
                        CustomAlert.showError("","Có lỗi xảy ra",  "Giá món ăn phải là số hợp lệ.");
                        return;
                    }

                    // Kiểm tra các điều kiện khác
                    if (name.length() > 100) {
                        CustomAlert.showError("","Có lỗi xảy ra",  "Tên món ăn không được vượt quá 100 ký tự.");
                        return;
                    }

                    if (description.length() > 500) {
                        CustomAlert.showError("","Có lỗi xảy ra",  "Mô tả món ăn không được vượt quá 500 ký tự.");
                        return;
                    }

                    if (cost <= 0) {
                        CustomAlert.showError("","Có lỗi xảy ra",  "Giá món ăn phải lớn hơn 0.");
                        return;
                    }

                    selectedFood.setName(name);
                    selectedFood.setDescription(description);
                    selectedFood.setCost(cost);

                    boolean updated = FoodService.updateFood(selectedFood);
                    if (updated) {
                        System.out.println("Cập nhật món ăn thành công!");
                        tableFood.refresh();  // Cập nhật lại bảng
                    } else {
                        CustomAlert.showError("","Có lỗi xảy ra",  "Cập nhật món ăn thất bại.");
                    }

                    // Reset lại hành động của nút "Thêm món ăn"
                    addFoodBtn.setOnAction(this::onClickAddFood);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    CustomAlert.showError("","Có lỗi xảy ra",  "Đã xảy ra lỗi: " + ex.getMessage());
                }
            });
        } else {
            CustomAlert.showError("","Có lỗi xảy ra",  "Vui lòng chọn một món ăn để chỉnh sửa.");
        }
    }
    public void onClickDeleteFood(ActionEvent event)
    {
        Food select = tableFood.getSelectionModel().getSelectedItem();
        if(select == null)
        {
            CustomAlert.showError("","Có lỗi xảy ra", "Vui lòng chọn 1 món ăn muốn xoá!!");
            return;
        }
        boolean result = CustomAlert.showConfirmation(
                "Xác nhận",
                "Bạn có chắc chắn muốn xóa?",
                "Dữ liệu sẽ không thể phục hồi sau khi xóa!"
        );

        if (result) {
            boolean deleted = FoodService.deleteFood(select.getId());
            if(deleted == true)
            {
                System.out.println("Xoá thành công món ăn: "+select.getName());
                listFood.remove(select);
            }
            else {
                System.out.println("Xóa đồ ăn thất bại.");
            }
            clearFoodForm();
            if(foodForm.isVisible()) foodForm.setVisible(false);
        }
    }
    private ObservableList<Food> getDataFood() {
        ObservableList<Food> listFood = FXCollections.observableArrayList();
        List<Food> list = FoodService.getAllFoodData();
        for(Food food : list)
        {
            listFood.add(food);
        }
        return listFood;
    }
    void onSearchFood() {
        String keyword = searchFood.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            tableFood.setItems(listFood);
            return;
        }
        ObservableList<Food> filteredList = FXCollections.observableArrayList();

        for (Food food : listFood) {
            if (food.getName().toLowerCase().contains(keyword) ||
                    food.getDescription().toLowerCase().contains(keyword)) {

                filteredList.add(food);
            } else {
                try {
                    float cost = Float.parseFloat(keyword);
                    if (food.getCost() == cost) {
                        filteredList.add(food);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        tableFood.setItems(filteredList);
    }

}
