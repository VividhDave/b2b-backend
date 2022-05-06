package com.divergent.mahavikreta.payload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReturnStatusRequest {

	@NotNull
	private Long returnOrderId;
	@NotNull
	private String statusMessage;
	@NotNull
	private String description;

}
