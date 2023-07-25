package bund.bamf.cnc.k8simporter.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import bund.bamf.cnc.k8simporter.application.ports.input.ImportK8sResources;


@Component
@Order(0)
class MyApplicationListener 
    implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ImportK8sResources importK8sResources; 

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        importK8sResources.importK8sResources();
    }
}

