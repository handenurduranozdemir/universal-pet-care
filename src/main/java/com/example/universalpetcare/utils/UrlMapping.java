package com.example.universalpetcare.utils;

public class UrlMapping {
    public static final String API = "/api/v1";
    public static final String USERS = API + "/users";

    public static final String UPDATE_USER = "/update/{userId}";
    public static final String REGISTER_USER ="/register";
    public static final String GET_USER_BY_ID = "/user/{userId}";
    public static final String DELETE_USER_BY_ID = "/delete/{userId}";
    public static final String GET_ALL_USERS = "/all-users";

    /*       APPOINTMENT API      */
    public static final String APPOINTMENTS = API + "/appointments";
    public static final String ALL_APPOINTMENTS = "/all";
    public static final String BOOK_APPOINTMENT = "/book-appointment";
    public static final String GET_APPPOINTMENT_BY_ID = "/appointment/{id}";
    public static final String UPDATE_APPOINTMENT = "/appointment/{id}/update";
    public static final String DELETE_APPOINTMENT = "/appointment/{id}/delete";
    public static final String GET_APPPOINTMENT_BY_NUMBER = "/appointment/{appointmentNo}/appointment-number";

    /*       PET API      */
    public static final String PETS = API + "/pets";
    public static final String SAVE_PETS_FOR_APPOINTMENT = "/save-pets";
    public static final String DELETE_PET_BY_ID = "/pet/{petId}/delete";
    public static final String GET_PET_BY_ID = "pet/{petId}/pet";
    public static final String UPDATE_PET = "/pet/{petId}/update";

    /*         PHOTO API            */
    public static final String PHOTOS = API + "/photos";
    public static final String UPLOAD_PHOTO = "/photo/upload";
    public static final String UPDATE_PHOTO = "/photo/{id}/update";
    public static final String DELETE_PHOTO = "/photo/{id}/user/{userId}delete";
    public static final String GET_PHOTO_BY_ID = "/photo/{id}/photo";
}


