package ss.projectt10.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountId;
    private String password;
    private String fullName;
    private String avatar;
    private String dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    private String company;

    public Account() {
    }



    public Account(String accountId, String password, String fullName, String phoneNumber) {
        this.accountId = accountId;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Account(String accountId, String password, String fullName, String avatar, String dateOfBirth, String address, String email, String phoneNumber, String company) {
        this.accountId = accountId;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.company = company;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
