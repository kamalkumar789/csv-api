package com.file.csv.Configuration;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Client {


    @Bean
    public ProjectApiRoot createApiClient() {
        return ApiRootBuilder.of()
                .defaultClient(ClientCredentials.of()
                                .withClientId("gQGeRTTa8Ec5NyFuQj-Wt49X")
                                .withClientSecret("kqMEDPv9ewLVIgsjfVePOaSGPSYP1Li4")
                                .build(),
                        ServiceRegion.GCP_US_CENTRAL1)
                .build("mirakl-demo");
    }
}