package bund.bamf.cnc.k8simporter.application.ports.output;

import java.util.List;
import java.util.Set;

import bund.bamf.cnc.k8simporter.domain.model.Deployment;
import bund.bamf.cnc.k8simporter.domain.model.Namespace;

public interface GetK8sResources {
    List<Namespace> getNamespaces();
    Set<Deployment> getDeployments(Namespace namespace);
    List<Namespace> getNamespacesAlternative();
}
