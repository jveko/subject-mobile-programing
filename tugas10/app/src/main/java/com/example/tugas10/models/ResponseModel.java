package com.example.tugas10.models;

public class ResponseModel<T> {
    public Boolean status = false;
    public String message = null;
    public T data;
}
