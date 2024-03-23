package com.fortinet.auditservice.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Greeting {

    private String msg;
    private String name;
}
