package unipos.core.components.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ggradnig
 * 
 */
@Service
public class ArtifactServiceImpl implements ArtifactService
{
	@Autowired
	private ArtifactRepository artifactRepository;
}
