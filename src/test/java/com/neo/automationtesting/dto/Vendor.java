package com.neo.automationtesting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Vendor {
    private String vendorName;
    private String vendorEmail;
    private long vendorMobileNo;
    private String materialType;
}
