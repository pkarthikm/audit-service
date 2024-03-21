package com.fortinet.auditservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="counsellor",
        indexes = @Index(name = "UniqueCounsellorContactIndex", columnList = "contact_number", unique = true))
public class AuditLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String contactNumber;

    private String email;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;

}
