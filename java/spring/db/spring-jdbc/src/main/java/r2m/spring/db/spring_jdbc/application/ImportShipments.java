package r2m.spring.db.spring_jdbc.application;


import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import r2m.spring.db.spring_jdbc.domain.Shipment;
import r2m.spring.db.spring_jdbc.infrastructure.persistence.repositories.ShipmentRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportShipments {

    private final ShipmentRepository shipmentRepository;

    public void execute(MultipartFile file) throws IOException {
        List<Shipment> shipments = new ArrayList<>();
        var workbook = WorkbookFactory.create(file.getInputStream());
        var sheet = workbook.getSheetAt(0);
        for(int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            var row = sheet.getRow(i);
            shipments.add(Shipment.builder()
                    .source(row.getCell(0).getStringCellValue())
                    .destination(row.getCell(1).getStringCellValue())
                    .build());

        }

        shipmentRepository.saveAll(shipments);
    }
}
