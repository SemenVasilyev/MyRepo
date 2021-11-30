package com.example.demo;

import lombok.Data;

/**
 * @author Semen V
 * @created 30|11|2021
 */
@Data
public class BankData implements Comparable<BankData> {
    private int fid;
    private String serialNum;
    private String memberCode;
    private String acctType;
    private String openedDt;
    private String acctRteCde;
    private String reportingDt;
    private String creditLimit;


    @Override
    public int compareTo(BankData o) {
        return Integer.compare(this.fid, o.fid);
    }
}