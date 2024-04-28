package br.com.x4fare.repositories;

import br.com.x4fare.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = """
        select t.bus_id as busId, b.trip_fare as tripFare, t.user_id as userId, u.type as type, count(t.user_id) as count
          from transactions t
          join buses b on b.id = t.bus_id
          join users u on u.id = t.user_id
         where to_char(transaction_date , 'YYYY-MM-DD') = :date
         group by bus_id, trip_fare, user_id, u.type
         order by bus_id, trip_fare, user_id, u.type
    """, nativeQuery = true)
    List<IReport> findAllByTransactionDate(String date);

}

