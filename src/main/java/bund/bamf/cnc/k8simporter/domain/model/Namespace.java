package bund.bamf.cnc.k8simporter.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter @Setter
@Node
public class Namespace {
    public final static String LABEL_NAME = "kubernetes.io/metadata.name";

    @Id
    String k8sName;
    @Property(LABEL_NAME)
    String name;
    // besser als starre labels -> dynamisches Mapping per label-map:
    Map<String, String> labels;
}
