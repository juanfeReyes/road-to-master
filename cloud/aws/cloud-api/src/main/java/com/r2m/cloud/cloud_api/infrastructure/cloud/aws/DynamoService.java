package com.r2m.cloud.cloud_api.infrastructure.cloud.aws;

import com.r2m.cloud.cloud_api.domain.Product;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamoService {

    private final DynamoDbClient client;

    private final DynamoDbEnhancedClient enhancedClient;

    public DynamoService() {
        this.client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(this.client)
                .build();
    }

    public <T> DynamoDbTable<T> buildTable(String tableName, Class<T> clazz){
        return enhancedClient.table(tableName, TableSchema.fromBean(clazz));
    }

    public <T> void puItem(String tableName, T item){
        DynamoDbTable<T> table = (DynamoDbTable<T>) buildTable(tableName, item.getClass());
        table.putItem(item);
    }

    public <T> List<T> scanItems(String tableName, Class<T> clazz){
        DynamoDbTable<T> table = buildTable(tableName, clazz);
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .limit(1)
                .build();
        return table.scan(request).items().stream().toList();

    }
}
