package com.spring.photolib.webapp.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name="Roles")
public class Role {
     
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rid")
    private Integer rid;
     
	@Column(name="role")
    private String role;
     
    @OneToMany(mappedBy="role")
    private Set<User> userRoles;
 
    public Integer getRid() {
        return rid;
    }
 
    public void setRid(Integer rid) {
        this.rid = rid;
    }
 
    public String getRole() {
        return role;
    }
 
    public void setRole(String role) {
        this.role = role;
    }
 
    public Set<User> getUserRoles() {
        return userRoles;
    }
 
    public void setUserRoles(Set<User> userRoles) {
        this.userRoles = userRoles;
    }
    
    
     
}