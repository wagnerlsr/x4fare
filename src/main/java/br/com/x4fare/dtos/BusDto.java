package br.com.x4fare.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusDto {

	@NotNull(message = "name.empty")
	@NotBlank(message = "name.blank")
	private String name;

	@NotNull(message = "tripFare.empty")
	private Double tripFare;

}
