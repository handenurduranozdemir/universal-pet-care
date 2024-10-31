package com.example.universalpetcare.service.pet;

import com.example.universalpetcare.model.Pet;

import java.util.List;

public interface IPetService {
    List<Pet> savePetForAppointment(List<Pet> pets);
    Pet updatePet(Pet pet, Long id);
    void deletePet(Long id);
    Pet getPetById(Long id);
}
