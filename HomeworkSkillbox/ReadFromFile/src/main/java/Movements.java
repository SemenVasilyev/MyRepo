import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Movements {
    public static final String MOVEMENTS_LIST = "data/movementList.csv";
    private ArrayList<Movement> listMovements;

    public Movements(String pathMovementsCsv) {
        listMovements = new ArrayList<>();
        List<String[]> list = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(pathMovementsCsv));
            list = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }
        list.stream().skip(1).forEach(s -> listMovements.add(new Movement(s)));
    }

    public double getExpenseSum() {
        return listMovements.stream().mapToDouble(Movement::getExpense).sum();
    }

    public double getIncomeSum() {
        return listMovements.stream().mapToDouble(Movement::getIncom).sum();
    }

    public void print() {
        System.out.println("Сумма расходов: " + getExpenseSum() + " руб.\n" +
                           "Сумма доходов: " + getIncomeSum() + " руб.\n\n" +
                            "Суммы расходов по организациям:\n");
        Map<String, List<Movement>> map = listMovements.stream().collect(Collectors.groupingBy(Movement::getOrganization));
        for (Map.Entry<String, List<Movement>> entry : map.entrySet()) {
            double summ = 0;
            for (Movement movement : entry.getValue()) {
                summ += movement.getExpense();
            }
            System.out.println(entry.getKey() + " " + summ+ " руб.");
        }
    }

    private class Movement {
        private String accountType;
        private String accountNumber;
        private String currency;
        private Date date;
        private String reference;
        private String description;
        private String organization;
        private double incom;
        private double expense;

        public Movement(String[] MovementCsv) {
            this.accountType = MovementCsv[0];
            this.accountNumber = MovementCsv[1];
            this.currency = MovementCsv[2];
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("dd.MM.yy");
            try {
                this.date = format.parse(MovementCsv[3]);
            } catch (ParseException e) {
                System.out.println(e);
            }
            this.reference = MovementCsv[4];
            this.description = MovementCsv[5];
            this.incom = Double.parseDouble(MovementCsv[6].replace(",", "."));
            this.expense = Double.parseDouble(MovementCsv[7].replace(",", "."));
            parseDiscription();
        }

        private void parseDiscription() {
            organization = description.replaceFirst(".+?\\s", "")
                                      .replaceFirst("\\s\\d\\d\\..+", "")
                                        .replaceFirst(".+/", "").trim();
        }

        public String getOrganization() {
            return organization;
        }

        public double getIncom() {
            return incom;
        }

        public double getExpense() {
            return expense;
        }
    }
}
