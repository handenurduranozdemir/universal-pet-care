package com.example.universalpetcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feedback;
    private int stars;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private User veterinarian;

    @ManyToOne
    @JoinColumn(name = "rewiever_id")
    private User patient;

    public void removeRelationship()
    {
        Optional.ofNullable(veterinarian)
                .ifPresent(vet -> vet.getReviews().remove(this));

        Optional.ofNullable(patient)
                .ifPresent(patient -> patient.getReviews().remove(this));
    }
}
