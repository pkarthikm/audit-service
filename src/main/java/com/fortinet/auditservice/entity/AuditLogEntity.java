package com.fortinet.auditservice.entity;

import com.fortinet.auditservice.enums.Status;
import com.fortinet.auditservice.service.Encrypt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private Status status;

    @Column(nullable = false)
    @Convert(converter = Encrypt.class)
    private String actorUserId;

    @Column(nullable = false)
    @Convert(converter = Encrypt.class)
    private String actorSessionId; //Unique session identifier of the event actor.

    @Convert(converter = Encrypt.class)
    private String actorClient; //User agent of the client/platform in use by the event actor.

    @Convert(converter = Encrypt.class)
    private String actorIpAddress;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="event_parameters", joinColumns=@JoinColumn(name="event_id"))
    Map<String, String> parameters = new HashMap<>();

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="event_prior_state", joinColumns=@JoinColumn(name="event_id"))
    Map<String, String> priorState = new HashMap<>();

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="event_resulting_state", joinColumns=@JoinColumn(name="event_id"))
    Map<String, String> resultingState = new HashMap<>();

    @Column(nullable = false)
    @Convert(converter = Encrypt.class)
    private String apiPath;

    @Convert(converter = Encrypt.class)
    private String clusterId;
    @Convert(converter = Encrypt.class)
    private String errorDescription;
    @Convert(converter = Encrypt.class)
    private Integer errorStatusCode;

    @CreationTimestamp
    private Date created;
}
