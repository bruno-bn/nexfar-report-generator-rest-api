package nexfar.reportgenerator.enums;

import lombok.Getter;

@Getter
public enum ReportType {
    ORDER_SIMPLE_XLS("PedidoResumido", "xlsx"),
    ORDER_DETAILED_XLS("PedidoDetalhado", "xlsx"),
    ORDER_SIMPLE_CSV("PedidoResumido", "csv"),
    ORDER_DETAILED_CSV("PedidoDetalhado", "csv");

    private final String fileName;
    private final String extension;

    ReportType(String fileName, String extension) {
        this.fileName = fileName;
        this.extension = extension;
    }

    public String getFileName() {
        return fileName + "." + extension;
    }
}
