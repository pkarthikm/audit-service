package com.fortinet.auditservice.model;

import com.fortinet.auditservice.enums.Status;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    private Date timestamp;

    @NonNull
    private String eventName;

    @NonNull
    private String tenantName; //The name of the tenant that the event belongs to.

    @NonNull
    private String entityName; //The object name that was the subject of the event.

    @NonNull
    private Status status;

    @NonNull
    private String actorUserId;

    @NonNull
    private String actorSessionId; //Unique session identifier of the event actor.

    private String actorClient; //User agent of the client/platform in use by the event actor.

    private String actorIpAddress;

    Map<String, String> parameters;
    Map<String, String> priorState;
    Map<String, String> resultingState;

    @NonNull
    private String apiPath;

    private String clusterId;
    private String errorDescription;
    private Integer errorStatusCode;

    @Setter(AccessLevel.NONE)
    private Date created;
}
