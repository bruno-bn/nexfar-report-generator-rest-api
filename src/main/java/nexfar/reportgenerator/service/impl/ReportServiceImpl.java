package nexfar.reportgenerator.service.impl;

import nexfar.reportgenerator.model.Order;
import nexfar.reportgenerator.model.OrderItem;
import nexfar.reportgenerator.request.ReportRequest;
import nexfar.reportgenerator.service.OrderService;
import nexfar.reportgenerator.service.ReportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    OrderService orderService;

    @Override
    public byte[] generateReport(ReportRequest request) {

        validateFormat(request.getFormat());
        validateKey(request.getKey());

        List<Order> orders = orderService.getOrders(request.getFilters());

        if (request.getFormat().equals("XLS")) {
            if (request.getKey().equals("ORDER_SIMPLE")) {
                return generateSimpleXLS(orders);
            } else {
                return generateDetailedXLS(orders);
            }
        } else if (request.getFormat().equals("CSV")) {
            if (request.getKey().equals("ORDER_SIMPLE")) {
                return generateSimpleCSV(orders);
            } else {
                return generateDetailedCSV(orders);
            }
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível gerar o relatório.");
    }

    private void validateFormat(String format) {
        if (!(format.equals("XLS") || format.equals("CSV"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato inválido: " + format);
        }
    }

    private void validateKey(String key) {
        if (!(key.equals("ORDER_SIMPLE") || key.equals("ORDER_DETAILED"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de relatório inválido: " + key);
        }
    }

    private byte[] generateSimpleXLS(List<Order> orders) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "CNPJ", "Name", "Created At", "Status", "Net Total", "Total with Taxes"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getClient().getCnpj());
                row.createCell(2).setCellValue(order.getClient().getName());
                row.createCell(3).setCellValue(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                row.createCell(4).setCellValue(order.getStatus());
                row.createCell(5).setCellValue(order.getNetTotal().doubleValue());
                row.createCell(6).setCellValue(order.getTotalWithTaxes().doubleValue());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private byte[] generateDetailedXLS(List<Order> orders) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "CNPJ", "Name", "Created At", "Status", "Product Sku", "Product Name", "Quantity", "Price", "Final Price"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(order.getId());
                    row.createCell(1).setCellValue(order.getClient().getCnpj());
                    row.createCell(2).setCellValue(order.getClient().getName());
                    row.createCell(3).setCellValue(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    row.createCell(4).setCellValue(order.getStatus());
                    row.createCell(5).setCellValue(item.getProduct().getSku());
                    row.createCell(6).setCellValue(item.getProduct().getName());
                    row.createCell(7).setCellValue(item.getQuantity());
                    row.createCell(8).setCellValue(item.getPrice());
                    row.createCell(9).setCellValue(item.getFinalPrice());
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private byte[] generateSimpleCSV(List<Order> orders) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);

            writer.write("ID;CNPJ;Name;Created At;Status;Net Total;Total with Taxes\n");

            for (Order order : orders) {
                writer.write(String.format("%s;%s;%s;%s;%s;%.2f;%.2f\n",
                        order.getId(),
                        order.getClient().getCnpj(),
                        order.getClient().getName(),
                        order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                        order.getStatus(),
                        order.getNetTotal().doubleValue(),
                        order.getTotalWithTaxes().doubleValue()));
            }
            writer.flush();
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private byte[] generateDetailedCSV(List<Order> orders) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);

            writer.write("ID;CNPJ;Name;Created At;Status;Product Sku;Product Name;Quantity;Price;Final Price\n");

            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%d;%.2f;%.2f\n",
                            order.getId(),
                            order.getClient().getCnpj(),
                            order.getClient().getName(),
                            order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                            order.getStatus(),
                            item.getProduct().getSku(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getFinalPrice()));
                }
            }
            writer.flush();
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
