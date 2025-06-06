package com.r2m.grpc.storagemanager.GrpcManager;

import com.r2m.grpc.storagemanager.storage.infrastructure.repository.RackRepository;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class GrpcConfigServer {

  @Autowired
  private RackRepository rackRepository;

  @Bean
  public Server grpcServer(List<BindableService> grpcServices) throws IOException, InterruptedException {
    rackRepository.findAll();

    System.out.println("Starting GRPC server");
    var serverBuilder = ServerBuilder
            .forPort(8081);

    grpcServices
            .forEach(serverBuilder::addService);

    var server = serverBuilder.build();
    server.start();
    server.awaitTermination();


    return server;
  }
}
