package br.com.x4fare.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private Long id;

	@NotNull
    @ManyToOne
	private Bus bus;

	@NotNull
	@ManyToOne
	private User user;

	@NotNull
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;

}
