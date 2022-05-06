package com.divergent.mahavikreta.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressFilter {

    private Long id;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String phone;
    private Long userId;
    private String addressType;
}
