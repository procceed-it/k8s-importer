package bund.bamf.cnc.k8simporter.domain.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import bund.bamf.cnc.k8simporter.application.ports.input.ImportK8sResources;
import bund.bamf.cnc.k8simporter.application.ports.output.DeploymentAdapter;
import bund.bamf.cnc.k8simporter.application.ports.output.GetK8sResources;
import bund.bamf.cnc.k8simporter.application.ports.output.NamespaceAdapter;
import bund.bamf.cnc.k8simporter.domain.model.Deployment;
import bund.bamf.cnc.k8simporter.domain.model.Namespace;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImportK8sResourcesImpl implements ImportK8sResources {
    GetK8sResources getK8sResources;
    NamespaceAdapter namespaceAdapter;
    DeploymentAdapter deploymentAdapter;

    @Override
    public void importK8sResources() {
        List<Namespace> namespaces = getK8sResources.getNamespaces();
        // List<Namespace> namespaces = getK8sResources.getNamespacesAlternative();
        for (Namespace ns : namespaces) {
            namespaceAdapter.save(ns);
            Set<Deployment> deployments = getK8sResources.getDeployments(ns);
            deploymentAdapter.saveAll(deployments);
        }
    }
}
