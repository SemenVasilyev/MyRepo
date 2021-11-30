package com.example.demo;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Semen V
 * @created 30|11|2021
 */
public class Task4 {
    public static final String LIST = "src/main/resources/list.csv";

    public static void toDo() throws IOException {
        Reader in = new FileReader(LIST);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(';')
                .withQuote('”')
                .parse(in);
        List<BankData> bankDataList = new ArrayList<>();
        for (CSVRecord record : records) {
            bankDataList.add(createBean(record));
        }

        bankDataList.sort((b1, b2) -> b1.compareTo(b2));

        bankDataList.stream().forEach(System.out::println);
    }

    public static BankData createBean(CSVRecord record){
        BankData bankData = new BankData();
        try {
            bankData.setFid(Integer.parseInt(record.get(0)));
            bankData.setSerialNum(record.get(1));
            bankData.setMemberCode(record.get(2));
            bankData.setAcctType(record.get(3));
            bankData.setOpenedDt(record.get(4));
            bankData.setAcctRteCde(record.get(5));
            bankData.setReportingDt(record.get(6));
            bankData.setCreditLimit(record.get(7));
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return bankData;
    }




}


