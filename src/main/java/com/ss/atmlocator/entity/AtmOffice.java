package com.ss.atmlocator.entity;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

import static com.ss.atmlocator.entity.AtmState.NO_LOCATION;

/**
 * Created by Olavin on 17.11.2014.
 */
@Entity
@Table(name = "atm")
public class AtmOffice implements Comparable<AtmOffice> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String address;

    @Embedded
    private GeoPosition geoPosition;

    @Enumerated(EnumType.ORDINAL)
    private AtmState state;

    @Enumerated(EnumType.ORDINAL)
    private AtmType type;

    public enum AtmType { IS_ATM, IS_OFFICE, IS_ATM_OFFICE }

    @Column
    private Timestamp lastUpdated;

    @Column
    private String photo;  // filename of real street photo

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atmOffice", fetch = FetchType.LAZY)
    private Set<AtmComment> atmComments;

    @Transient
    private int commentsCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public AtmOffice() {
        this.state = NO_LOCATION;
        this.type = AtmType.IS_ATM;
    }

    public AtmOffice(final String address, final AtmType type) {
        this.address = address;
        this.type = type;
        this.state = NO_LOCATION;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AtmOffice)) return false;

        AtmOffice atmOffice = (AtmOffice) o;
        return address.equals(atmOffice.address);
    }

    @Override
    public int hashCode() {
        if (address == null) {
            return 0;
        }
        return address.hashCode();
    }

    public int getCommentsCount() {
        return atmComments.size();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public AtmState getState() {
        return state;
    }

    public void setState(AtmState state) {
        this.state = state;
    }

    public AtmType getType() {
        return type;
    }

    /**
     * Returns text representation of ATM type to display at web-page
     * @return ATM type as String
     */
    public String getTypeString() {
        String str;
        switch (type) {
            case IS_ATM: str = "ATM"; break;
            case IS_OFFICE: str = "Branch"; break;
            case IS_ATM_OFFICE: str = "Branch, ATM"; break;
            default: str = "Undefined";
        }
        return str;
    }

    public String getStateString() {
        String str;
        switch (state) {
            case NORMAL: str = "Normal"; break;
            case DISABLED: str = "Disabled"; break;
            case BAD_ADDRESS: str = "Bad Address"; break;
            case NO_LOCATION: str = "No location"; break;
            default: str = "Undefined";
        }
        return str;
    }

    public void setType(AtmType type) {
        this.type = type;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTimeString() {
        if (lastUpdated == null) {
            return "null";
        } else {
            return String.format("%1$TD %1$TT", lastUpdated);
        }
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

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public int compareTo(final AtmOffice other) {
        return this.getAddress().compareTo(other.getAddress());
    }

    @Override
    public String toString() {
        return "AtmOffice{"
                + "address='" + address + '\''
                + ", type=" + type.ordinal() + '}';
    }
}

