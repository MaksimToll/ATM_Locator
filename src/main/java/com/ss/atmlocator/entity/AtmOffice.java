package com.ss.atmlocator.entity;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import static org.apache.commons.lang.ObjectUtils.*;

/**
 * Created by Olavin on 17.11.2014.
 */
@Entity
@Table(name="atm")
public class AtmOffice implements Comparable<AtmOffice>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column
    private String address;

    @Embedded
    private GeoPosition geoPosition;

    @Column
    private int state; //todo: substitute with enum

    @Enumerated(EnumType.ORDINAL)
    private AtmType type;

    public enum AtmType { IS_ATM, IS_OFFICE, IS_ATM_OFFICE }

    @Column
    private Timestamp lastUpdated;

    @Column
    private String photo;  // filename of real street photo


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    Bank bank;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtmOffice atmOffice = (AtmOffice) o;

        return address.equals(atmOffice.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atmOffice", fetch = FetchType.LAZY)
    private Set<AtmComment> atmComments;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atmOffice", fetch = FetchType.LAZY)
    private Set<AtmFavorite> atmFavorites;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public AtmType getType() {
        return type;
    }

    public void setType(AtmType type) {   this.type = type;}

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public Set<AtmComment> getAtmComments() {
        return atmComments;
    }

    public void setAtmComments(Set<AtmComment> atmComments) {
        this.atmComments = atmComments;
    }

    public Set<AtmFavorite> getAtmFavorites() {
        return atmFavorites;
    }

    public void setAtmFavorites(Set<AtmFavorite> atmFavorites) {
        this.atmFavorites = atmFavorites;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
    
    @Override
    public int compareTo(AtmOffice other){
        return this.getAddress().compareTo(other.getAddress());
    }
}

