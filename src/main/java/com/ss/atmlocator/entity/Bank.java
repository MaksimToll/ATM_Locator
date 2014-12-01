package com.ss.atmlocator.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by Olavin on 18.11.2014.
 */
@Entity
@Table(name="banks")
public class Bank implements Comparable<Bank>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column
    private String name;

    @Column
    private int mfoCode = 0;

    @Column
    private String webSite;

    @Column
    private String logo;

    @Column
    private String iconAtm;

    @Column
    private String iconOffice;

    @Column
    private Timestamp lastUpdated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bank")
    private Set<AtmOffice> atmOfficeSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bank")
    private Set<AtmParser> atmParserSet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "network_id")
    private AtmNetwork network;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMfoCode() {
        return mfoCode;
    }

    public void setMfoCode(int mfoCode) {
        this.mfoCode = mfoCode;
    }

    public Set<AtmParser> getAtmParserSet() {
        return atmParserSet;
    }

    public void setAtmParserSet(Set<AtmParser> atmParserSet) {
        this.atmParserSet = atmParserSet;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIconAtm() {
        return iconAtm;
    }

    public void setIconAtm(String iconAtm) {
        this.iconAtm = iconAtm;
    }

    public String getIconOffice() {
        return iconOffice;
    }

    public void setIconOffice(String iconOffice) {
        this.iconOffice = iconOffice;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<AtmOffice> getAtmOfficeSet() {
        return atmOfficeSet;
    }

    public void setAtmOfficeSet(Set<AtmOffice> atmOfficeSet) {
        this.atmOfficeSet = atmOfficeSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (Id != bank.Id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Id;
    }

    public AtmNetwork getNetwork() {
        return network;
    }

    public void setNetwork(AtmNetwork network) {
        this.network = network;
    }

    public Bank() {
    }

    public Bank(String name, String webSite) {
        this.name = name;
        this.webSite = webSite;
    }

    @Override
    public int compareTo(Bank other){
        return this.getName().compareTo(other.getName());
    }
}
