package org.eno;

public class InvalidCommandException extends Throwable {

    private String reportForTheUser;

    public InvalidCommandException(String reportForTheUser) {
        this.reportForTheUser = reportForTheUser;
    }

    public String getReportForTheUser() {
        return reportForTheUser;
    }
}
