package bund.bamf.cnc.k8simporter.application.ports.output;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import bund.bamf.cnc.k8simporter.domain.model.Namespace;

public interface NamespaceAdapter extends Neo4jRepository<Namespace, String> {

}