package nexfar.reportgenerator.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportRequest {

    private String key;
    private String format;
    private List<ReportFilter> filters;
}
