package br.com.x4fare.repositories;

public interface IReport {

    Long    getBusId();
    Float   getTripFare();
    Long    getUserId();
    String  getType();
    Integer getCount();

}
