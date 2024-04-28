package nexfar.reportgenerator.service;

import nexfar.reportgenerator.model.Order;
import nexfar.reportgenerator.request.ReportRequest;

import java.util.List;

public interface ReportService {

    byte[] generateReport(ReportRequest request);
}
