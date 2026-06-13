package com.Quintero.Service_Producto.config.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

import com.Quintero.Service_Producto.model.AuditRevisionEntity;

import org.springframework.security.core.Authentication;

public class UserRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {

        AuditRevisionEntity entity = (AuditRevisionEntity) revisionEntity;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            entity.setUsername(auth.getName());
        } else {
            entity.setUsername("SYSTEM"); // Usuario por defecto si no hay sesión
        }
    }
}
