package com.example.universalpetcare.request;

import com.example.universalpetcare.model.Appointment;
import com.example.universalpetcare.model.Pet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookAppointmentRequest {
    private Appointment appointment;
    private List<Pet> pets;
}
