package com.mykhailopavliuk.util;


import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.util.urlHandler.PingStatistics;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ExcelHandler {
    public static void exportUsersToTable(Path path, List<User> userList) throws IOException {
        int rowCounter = 0;
        String[] columnNames = {"Id", "Email", "Number of urls"};
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Users");
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);

        Row header = sheet.createRow(rowCounter);

        CellStyle headerStyle = getStyledHeader(workbook);

        Cell headerCell;

        for (String columnName : columnNames) {
            headerCell = header.createCell(rowCounter++);
            headerCell.setCellValue(columnName);
            headerCell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        rowCounter = 0;
        for (User user : userList) {
            Row row = sheet.createRow(++rowCounter);

            Cell cell1 = row.createCell(0);
            cell1.setCellValue(user.getId());
            cell1.setCellStyle(style);

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(user.getEmail());
            cell2.setCellStyle(style);

            Cell cell3 = row.createCell(2);
            cell3.setCellValue(user.getUrls().size());
            cell3.setCellStyle(style);
        }

        try (FileOutputStream outputStream = new FileOutputStream(path.resolve("Users.xlsx").toFile())) {
            workbook.write(outputStream);
            workbook.close();

        }

    }

    public static void exportUrlStatisticsToTable(Path outputPath, List<PingStatistics> pingStatisticsList) throws IOException {
        int rowCounter = 0;
        String[] columnNames = {
                "Url path",
                "Total number of requests",
                "Number of timeout requests",
                "Slowest response time (ms)",
                "Fastest response time (ms)",
                "Average response time (ms)"
        };
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Url Statistics");
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 40 * 256);
        sheet.setColumnWidth(2, 40 * 256);
        sheet.setColumnWidth(3, 40 * 256);
        sheet.setColumnWidth(4, 40 * 256);
        sheet.setColumnWidth(5, 40 * 256);

        Row header = sheet.createRow(rowCounter);

        CellStyle headerStyle = getStyledHeader(workbook);

        Cell headerCell;

        for (String columnName : columnNames) {
            headerCell = header.createCell(rowCounter++);
            headerCell.setCellValue(columnName);
            headerCell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        rowCounter = 0;
        for (PingStatistics pingStatistics : pingStatisticsList) {
            Row row = sheet.createRow(++rowCounter);

            Cell cell1 = row.createCell(0);
            cell1.setCellValue(pingStatistics.getUrl().getPath());
            cell1.setCellStyle(style);

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(pingStatistics.getTotalNumberOfRequests());
            cell2.setCellStyle(style);

            Cell cell3 = row.createCell(2);
            cell3.setCellValue(pingStatistics.getNumberOfNotTimeoutResponses());
            cell3.setCellStyle(style);

            Cell cell4 = row.createCell(3);
            cell4.setCellValue(pingStatistics.getSlowestResponseTime().toMillis());
            cell4.setCellStyle(style);

            Cell cell5 = row.createCell(4);
            cell5.setCellValue(pingStatistics.getFastestResponseTime().toMillis());
            cell5.setCellStyle(style);

            Cell cell6 = row.createCell(5);
            cell6.setCellValue(pingStatistics.getAverageResponseTime().toMillis());
            cell6.setCellStyle(style);
        }

        try (FileOutputStream outputStream = new FileOutputStream(outputPath.resolve("Url Statistics.xlsx").toFile())) {
            workbook.write(outputStream);
            workbook.close();
        }
    }

    private static CellStyle getStyledHeader(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        return headerStyle;
    }
}
