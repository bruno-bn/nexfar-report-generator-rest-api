package nexfar.reportgenerator.respository;

import nexfar.reportgenerator.model.Order;
import nexfar.reportgenerator.request.ReportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class OrderRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Order> getOrderList(List<ReportFilter> filters) {
        Criteria criteria = new Criteria();

        for (ReportFilter filter : filters) {
            String filterKey = filter.getKey();
            String operation = filter.getOperation();
            String value1 = filter.getValue1();
            String value2 = filter.getValue2();

            switch (filterKey) {
                case "cnpj":
                    handleCnpjFilter(criteria, operation, value1);
                    break;
                case "createdAt":
                    handleCreatedAtFilter(criteria, operation, value1, value2);
                    break;
                case "netTotal":
                    handleNetTotalFilter(criteria, operation, value1);
                    break;
                case "status":
                    handleStatusFilter(criteria, operation, value1);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filtro inválido: " + filterKey);
            }
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Order.class);
    }

    private void handleCnpjFilter(Criteria criteria, String operation, String value1) {
        if (operation.equals("EQ")) {
            criteria.and("client.cnpj").is(value1);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação inválida para filtro cnpj");
        }
    }

    private void handleCreatedAtFilter(Criteria criteria, String operation, String value1, String value2) {
        if (operation.equals("INTERVAL")) {
            LocalDateTime startDate = LocalDateTime.parse(value1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime endDate = LocalDateTime.parse(value2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            criteria.andOperator(
                    Criteria.where("createdAt").gte(startDate),
                    Criteria.where("createdAt").lte(endDate)
            );
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação inválida para filtro createdAt");
        }
    }

    private void handleNetTotalFilter(Criteria criteria, String operation, String value1) {
        float netTotalValue = Float.parseFloat(value1);

        if (operation.equals("GTE")) {
            criteria.and("netTotal").gte(netTotalValue);
        } else if (operation.equals("LTE")) {
            criteria.and("netTotal").lte(netTotalValue);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação inválida para filtro netTotal");
        }
    }

    private void handleStatusFilter(Criteria criteria, String operation, String value1) {
        if (operation.equals("EQ")) {
            criteria.and("status").is(value1);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação inválida para filtro status");
        }
    }
}
