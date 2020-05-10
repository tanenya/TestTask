package com.commerzbank.task.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Test task Commerzbank
 *
 * Authentication request
 *
 * @author vtanenya
 * */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
}
