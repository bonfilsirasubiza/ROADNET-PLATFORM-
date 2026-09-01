package com.roadnet.model;

import com.roadnet.model.enums.GeoScope;
import com.roadnet.model.enums.Intention;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_intentions", joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "intention", length = 40)
    private List<Intention> intentions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GeoScope geoScope;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_interests", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "interest", length = 100)
    private List<String> interests = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_lifestyle", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "lifestyle", length = 100)
    private List<String> lifestyle = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Intention> getIntentions() {
        return intentions;
    }

    public void setIntentions(List<Intention> intentions) {
        this.intentions = intentions;
    }

    public GeoScope getGeoScope() {
        return geoScope;
    }

    public void setGeoScope(GeoScope geoScope) {
        this.geoScope = geoScope;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(List<String> lifestyle) {
        this.lifestyle = lifestyle;
    }
}
