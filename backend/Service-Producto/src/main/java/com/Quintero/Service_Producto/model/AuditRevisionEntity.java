package com.Quintero.Service_Producto.model;

import org.hibernate.envers.RevisionEntity;

import jakarta.persistence.Entity;
import com.Quintero.Service_Producto.config.audit.UserRevisionListener;

import org.hibernate.envers.DefaultRevisionEntity;

@Entity
@RevisionEntity(UserRevisionListener.class) // Aquí vinculamos el Listener que creamos antes
public class AuditRevisionEntity extends DefaultRevisionEntity {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}