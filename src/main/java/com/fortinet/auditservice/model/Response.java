package com.fortinet.auditservice.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    @NonNull
    private String message;
}
