package com.sesac.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config")
@RefreshScope
@Tag(name = "Config Test", description = "Made for check config")
public class ConfigController {

    @Value("${user.service.name}")
    private String serviceName;

    @Value("${user.service.version}")
    private String serviceVersion;

    @Value("${user.service.description}")
    private String serviceDescription;

    @GetMapping
    @Operation(summary = "Search config info", description = "Check the config from config-server")
    public Map<String, String> getConfig() {
        Map<String, String> map = new HashMap<>();

        map.put("serviceName", serviceName);
        map.put("serviceVersion", serviceVersion);
        map.put("serviceDescription", serviceDescription);

        return map;
    }

}
