package com.neo.automationtesting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client{
    private String clientName;
    private String clientEmail;
    private long mobileNo;
}

