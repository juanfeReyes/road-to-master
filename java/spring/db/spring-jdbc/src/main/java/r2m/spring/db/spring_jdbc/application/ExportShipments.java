package r2m.spring.db.spring_jdbc.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import r2m.spring.db.spring_jdbc.domain.Shipment;
import r2m.spring.db.spring_jdbc.infrastructure.persistence.repositories.ShipmentRepository;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportShipments {

    public static final String SHIPMENTS_SHEET_NAME = "shipments_report";
    
    private final ShipmentRepository shipmentRepository;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void execute(OutputStream outputStream) {

        try (var workbook = new SXSSFWorkbook(100)) {
            var sheet = workbook.createSheet(SHIPMENTS_SHEET_NAME);
            streamShipments(sheet);
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("Error opening workbook", e);
        }
    }

    /**
     * Query shipments into stream and adds each resultSet into xlsx row
     * @param sheet
     */
    private void streamShipments(SXSSFSheet sheet) {
        AtomicInteger rowIdx = new AtomicInteger(1);
        jdbcTemplate.getJdbcTemplate().setFetchSize(100);
        try (var shipments = queryShipmentStream()) {
            shipments.forEach((shipment) -> {
                buildShipmentRow(shipment, sheet, rowIdx);
            });
        }
    }

    private Stream<Shipment> queryShipmentStream() {
        return jdbcTemplate.queryForStream("select * from logistics.shipment",
                new MapSqlParameterSource(),
                (rs, rowId) -> Shipment.builder()
                        .id(rs.getInt("id"))
                        .source(rs.getString("source"))
                        .destination(rs.getString("destination"))
                        .build());
    }

    private static void buildShipmentRow(Shipment shipment, SXSSFSheet sheet, AtomicInteger rowIdx) {
        var row = sheet.createRow(rowIdx.getAndIncrement());
        row.createCell(1).setCellValue(shipment.getId());
        row.createCell(2).setCellValue(shipment.getSource());
        row.createCell(3).setCellValue(shipment.getDestination());
    }
}
