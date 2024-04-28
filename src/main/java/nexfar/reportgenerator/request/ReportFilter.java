package nexfar.reportgenerator.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportFilter {

    private String key;
    private String operation;
    private String value1;
    private String value2;
}
