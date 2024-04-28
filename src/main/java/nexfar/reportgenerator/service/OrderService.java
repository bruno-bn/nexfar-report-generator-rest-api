package nexfar.reportgenerator.service;

import nexfar.reportgenerator.model.Order;
import nexfar.reportgenerator.request.ReportFilter;
import nexfar.reportgenerator.request.ReportRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {

    public List<Order> getOrders(List<ReportFilter> filter);
}
