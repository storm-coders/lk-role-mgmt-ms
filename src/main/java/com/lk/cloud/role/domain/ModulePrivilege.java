package com.lk.cloud.role.domain;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name =  "module_privilege")
public class ModulePrivilege {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "module_privilege_id")
    private UUID id;

    @Column(name = "privilege_description")
    private String description;

    @Column(name = "privilege_code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "module_id")
    private ApplicationModule module;
}
