/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guesthouse.entities;


public class Staff extends Person {
    private int    staffId;
    private String username;
    private String password;
    private String role; // "Admin" or "Staff"

    public Staff(int staffId, String firstName, String lastName,
                 String email, String phone, String username, String password, String role) {
        super(firstName, lastName, email, phone);
        this.staffId  = staffId;
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    @Override
    public String getRole() { return role; }

    public int    getStaffId()  { return staffId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role)         { this.role = role; }
}
