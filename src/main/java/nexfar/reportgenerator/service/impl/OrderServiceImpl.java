package nexfar.reportgenerator.service.impl;

import nexfar.reportgenerator.model.Order;
import nexfar.reportgenerator.request.ReportFilter;
import nexfar.reportgenerator.respository.OrderRepositoryCustom;
import nexfar.reportgenerator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepositoryCustom orderRepositoryCustom;

    @Override
    public List<Order> getOrders(List<ReportFilter> filters) {

        return orderRepositoryCustom.getOrderList(filters);
    }
}
