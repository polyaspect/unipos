package unipos.core.components.artifact;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ggradnig
 *
 * Spring repository for CRUD operations on Entity
 *
 */

public interface ArtifactRepository extends MongoRepository<Artifact, String>
{
}
