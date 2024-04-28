package nexfar.reportgenerator.controller;

import nexfar.reportgenerator.request.ReportRequest;
import nexfar.reportgenerator.enums.ReportType;
import nexfar.reportgenerator.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportRequest request) {
        byte[] report = reportService.generateReport(request);

        HttpHeaders headers = new HttpHeaders();
        String fileName = getFileName(request.getKey(), request.getFormat());
        headers.setContentDispositionFormData("filename", fileName);
        headers.setContentType(MediaType.parseMediaType(getContentType(request.getFormat())));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(report);
    }

    private String getFileName(String key, String format) {
        String fileName = "";
        if (key.equals("ORDER_DETAILED")) {
            fileName = format.equals("XLS") ?
                    ReportType.ORDER_DETAILED_XLS.getFileName() :
                    ReportType.ORDER_DETAILED_CSV.getFileName();
        } else if (key.equals("ORDER_SIMPLE")) {
            fileName = format.equals("XLS") ?
                    ReportType.ORDER_SIMPLE_XLS.getFileName() :
                    ReportType.ORDER_SIMPLE_CSV.getFileName();
        }
        return fileName;
    }

    private String getContentType(String format) {
        return format.equals("XLS") ?
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" :
                "text/csv";
    }

}
