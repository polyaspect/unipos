package unipos.core.components.artifact;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import unipos.core.components.modules.ModuleStatus;
import unipos.core.remote.artifactory.FolderInfo;
import unipos.core.utils.Security;
import unipos.core.components.modules.Module;
import unipos.core.remote.artifactory.Folder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author ggradnig
 **/

@RestController
@RequestMapping(value="/artifacts", produces=MediaType.APPLICATION_JSON_VALUE)
@Api(value="/artifacts")
@PropertySource("classpath:application.properties")
@Component
public class ArtifactController
{
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value="Find all artifacts",
		response = Artifact.class,
		responseContainer = "List")
	public List<Artifact> getAll() throws IOException {
		Resource resource = new ClassPathResource("/application.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);

		String url = props.getProperty("artifactory.url") + props.getProperty("artifactory.folderInfo");

		Security.disableSslVerification();

		RestTemplate restTemplate = new RestTemplate();
		FolderInfo response = restTemplate.getForObject(url, FolderInfo.class);

		List<Module> modules = new ArrayList<>();
		List<Artifact> artifacts = new ArrayList<>();

		for(Folder folder : response.getChildren())
		{
			if(folder.isFolder()){
				Module module = Module.builder().build();
				module.setName(folder.getUri().replace("/", ""));
				modules.add(module);

				FolderInfo moduleResponse = restTemplate.getForObject(url + folder.getUri(), FolderInfo.class);

				for(Folder artifactFolder : moduleResponse.getChildren())
				{
					if(artifactFolder.isFolder())
					{
						Artifact artifact = new Artifact();
						artifact.setModule(module);
						artifact.setVersion(artifactFolder.getUri().replace("/", ""));
						artifacts.add(artifact);
					}
				}
			}
		}

		return artifacts;
	}
}
