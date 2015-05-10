package de.hawai.bicycle_tracking.server.dto;

import de.hawai.bicycle_tracking.server.utility.value.Address;

public class UserDTO {
    private Address address;
    private int id;
    private String firstName;
    private String name;
    private String gender;
    private String birthdate;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getCustomerid() {
        return id;
    }

    public void setCustomerid(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthday) {
        this.birthdate = birthday;
    }
}
