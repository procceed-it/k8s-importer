package bund.bamf.cnc.k8simporter.infrastructure.adapters.output;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import bund.bamf.cnc.k8simporter.application.ports.output.GetK8sResources;
import bund.bamf.cnc.k8simporter.domain.model.Deployment;
import bund.bamf.cnc.k8simporter.domain.model.Namespace;

@Component
public class K8sAdapter implements GetK8sResources {

    ObjectMapper objectMapper = new ObjectMapper();

    public List<Namespace> getNamespaces() {
        List<Namespace> namespaces = new ArrayList<>();

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("kubectl", "get", "ns", "-o", "json");
        builder.directory(new File(System.getProperty("user.home")));
        Process process;

        try {
            process = builder.start();
            process.waitFor();
            JsonNode rootNode = objectMapper.readTree(process.getInputStream());
            ArrayNode specNode = (ArrayNode)rootNode.path("items");
            for (JsonNode aNode : specNode) {
                Namespace tmpNamespace = new Namespace();

                String namespaceName = aNode.path("metadata").path("name").asText();
                tmpNamespace.setK8sName(namespaceName);
                JsonNode labels = aNode.path("metadata").path("labels");
                for (Iterator<Entry<String, JsonNode>> i = labels.fields(); i.hasNext(); ) {
                    Entry<String, JsonNode> entry = (Entry<String, JsonNode>)i.next();
                    if (entry.getKey() == Namespace.LABEL_NAME) {
                        tmpNamespace.setName(entry.getValue().toString());
                    }
                }
                namespaces.add(tmpNamespace);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return namespaces;
    }

    public List<Namespace> getNamespacesAlternative() {
        List<Namespace> namespaces = new ArrayList<>();

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("kubectl", "get", "ns", "-o", "json");
        builder.directory(new File(System.getProperty("user.home")));
        Process process;

        try {
            process = builder.start();
            process.waitFor();
            JsonNode rootNode = objectMapper.readTree(process.getInputStream());
            ArrayNode specNode = (ArrayNode)rootNode.path("items");
            for (JsonNode aNode : specNode) {
                Namespace tmpNamespace = new Namespace();

                // namespace k8s-name
                String namespaceName = aNode.path("metadata").path("name").asText();
                tmpNamespace.setK8sName(namespaceName);

                // labels
                Map<String, String> labelsMap = new HashMap<>();
                JsonNode labelNodes = aNode.path("metadata").path("labels");
                for (Iterator<Entry<String, JsonNode>> i = labelNodes.fields(); i.hasNext(); ) {
                    Entry<String, JsonNode> entry = (Entry<String, JsonNode>)i.next();
                    labelsMap.put(entry.getKey(), entry.getValue().toString());
                }
                tmpNamespace.setLabels(labelsMap);
                namespaces.add(tmpNamespace);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return namespaces;
    }

    public Set<Deployment> getDeployments(Namespace namespace) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("kubectl", "get", "deploy", "-n", namespace.getK8sName() ,"-o", "json");
        builder.directory(new File(System.getProperty("user.home")));
        Process process;

        Set<Deployment> deployments = new HashSet<>();

        try {
            process = builder.start();
            process.waitFor();
            JsonNode rootNode = objectMapper.readTree(process.getInputStream());
            ArrayNode specNode = (ArrayNode)rootNode.path("items");
            for (JsonNode aNode : specNode) {
                Deployment tmpDeployment = new Deployment();
                tmpDeployment.setK8sName(aNode.path("metadata").path("name").asText());

                JsonNode labels = aNode.path("metadata").path("labels");
                for (Iterator<Entry<String, JsonNode>> i = labels.fields(); i.hasNext(); ) {
                    Entry<String, JsonNode> entry = (Entry<String, JsonNode>)i.next();
                    if (entry.getKey() == Deployment.LABEL_NAME) {
                        tmpDeployment.setK8sApp(entry.getValue().toString());
                    }
                }

                tmpDeployment.setNamespace(namespace);
                deployments.add(tmpDeployment);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return  deployments;
    }
}
