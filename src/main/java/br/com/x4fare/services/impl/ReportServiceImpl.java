package br.com.x4fare.services.impl;

import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.repositories.IReport;
import br.com.x4fare.repositories.TransactionRepository;
import br.com.x4fare.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository transactionRepository;

    @Override
    public HashMap<String, List<HashMap<String, Object>>> dailyReport(LocalDate startDate, LocalDate endDate) {
        var response = new HashMap<String, List<HashMap<String, Object>>>();

        for (var date = endDate; date.isAfter(startDate) || date.equals(startDate); date = date.minusDays(1)) {
            response.put(date.toString(), getReports(transactionRepository.findAllByTransactionDate(date.toString())));
        }

        return response;
    }

    private List<HashMap<String, Object>> getReports(List<IReport> transactions) {
        var list = new ArrayList<HashMap<String, Object>>();
        var first = true;

        if (transactions.size() == 0)
            return list;

        Long   busId = 0L;
        int    numTransactions = 0;
        int    numUsers = 0;
        double totalAmount = 0;

        for (IReport report: transactions) {
            if (!Objects.equals(report.getBusId(), busId)) {
                if (!first)
                    list.add(getMap(busId, totalAmount, numTransactions, numUsers));

                first = false;
                busId = report.getBusId();
                totalAmount = 0;
                numTransactions = 0;
                numUsers = 0;
            }

            numUsers++;
            numTransactions += report.getCount();
            totalAmount += getAmount(report.getType(), report.getTripFare()) * report.getCount();
        }

        list.add(getMap(busId, totalAmount, numTransactions, numUsers));

        return list;
    }

    private HashMap<String, Object> getMap(Long busId, double totalAmount, int numTransactions, int numUsers) {
        var map = new HashMap<String, Object>();

        map.put("busId", busId);
        map.put("numUsers", numUsers);
        map.put("totalAmount", totalAmount);
        map.put("numTransactions", numTransactions);

        return map;
    }

    private double getAmount(String type, Float tripFare ) {
        return type.equals(UserDto.UserType.REGULAR.toString()) ? tripFare : (type.equals(UserDto.UserType.ELDERLY.toString()) ? 0 : (tripFare / 2));
    }
}
