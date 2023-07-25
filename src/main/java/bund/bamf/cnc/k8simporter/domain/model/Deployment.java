package bund.bamf.cnc.k8simporter.domain.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Node
public class Deployment {
    public final static String LABEL_NAME = "k8s-app";

    @Id
    String k8sName;
    @Property(LABEL_NAME)
    String k8sApp;
    @Relationship(type = "DIRECTED", direction = Direction.OUTGOING)
    Namespace namespace;
}
