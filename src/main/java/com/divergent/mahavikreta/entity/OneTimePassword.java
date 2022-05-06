package com.divergent.mahavikreta.entity;

import com.divergent.mahavikreta.audit.DateAudit;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "one_time_password")
public class OneTimePassword extends DateAudit implements Serializable  {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "whatsapp_mobile_number")
    private String whatsappMobileNumber;

    @Column(name = "one_time_password")
    private int oneTimePassword;
}
