package com.example.tugas12.models;

public class ResponseModel<T> {
    public ResponseModel(){
    }
    public ResponseModel(Boolean status){
        Status = status;
    }
    public ResponseModel(Boolean status, T data){
        Status = status;
        Data = data;
    }
    public Boolean Status = false;
    public String Message = null;
    public T Data;
}
