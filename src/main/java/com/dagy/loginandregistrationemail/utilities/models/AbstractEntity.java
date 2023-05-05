package com.dagy.loginandregistrationemail.utilities.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
//EntityListeners ecoute les modif sur createdate et modifdate et enregistre auto dans la bd
public class AbstractEntity implements Serializable {

    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    private Long id;

    @CreatedDate
    @Column(name = "createdAtDate", nullable = true, updatable = false)
    private Instant createdAtDate;

    @LastModifiedDate
    @Column(name = "modifiedAtDate")
    private Instant modifiedAtDate;


}

