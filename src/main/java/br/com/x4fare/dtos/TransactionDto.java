package br.com.x4fare.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

	@NotNull(message = "busId.empty")
	private Long busId;

	@NotNull(message = "userId.empty")
	private Long userId;

	@NotNull(message = "transactionDate.empty")
	private LocalDateTime transactionDate;

}
