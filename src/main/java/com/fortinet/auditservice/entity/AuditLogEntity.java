package com.fortinet.auditservice.entity;

import com.fortinet.auditservice.service.StringEncrypt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Immutable
@Table(name="audit_log",
        indexes = {@Index(name = "tsIndex", columnList = "timestamp"),
                @Index(name = "eventNameIndex", columnList = "event_name"),
                @Index(name = "entityNameIndex", columnList = "entity_name"),
                @Index(name = "tnIndex", columnList = "tenant_name"),})
public class AuditLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String tenantName; //The name of the tenant that the event belongs to.

    @Column(nullable = false)
    private String entityName; //The object name that was the subject of the event.

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    private String status;

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    private String actorUserId;

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    private String actorSessionId; //Unique session identifier of the event actor.

    @Convert(converter = StringEncrypt.class)
    private String actorClient; //User agent of the client/platform in use by the event actor.

    @Convert(converter = StringEncrypt.class)
    private String actorIpAddress;

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    @Lob
    private String parameters;

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    @Lob
    private String priorState;

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    @Lob
    private String resultingState;

    @Column(nullable = false)
    @Convert(converter = StringEncrypt.class)
    private String apiPath;

    @Convert(converter = StringEncrypt.class)
    private String clusterId;
    @Convert(converter = StringEncrypt.class)
    private String errorDescription;
    @Convert(converter = StringEncrypt.class)
    private String errorStatusCode;

    @CreationTimestamp
    private Date created;
}
