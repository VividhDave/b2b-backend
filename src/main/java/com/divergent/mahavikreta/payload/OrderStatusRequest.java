package com.divergent.mahavikreta.payload;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequest {
	private Long id;

	@NotNull
	private Long orderId;
	@NotNull
	private String statusMessage;
	@NotNull
	private String description;

	private boolean isNotificationSend;

}
