package com.utc2.cinema.dao;

import java.util.List;

public interface DaoInterface<T> {

    public int insertData(T target);
    public int updateData(T target);
    public int deleteData(T target);
    public T getData(String email, String password);
    public List<T> getAllData();
}
