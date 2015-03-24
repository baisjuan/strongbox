package org.carlspring.strongbox.storage.validation.version;

import org.apache.maven.artifact.Artifact;
import org.carlspring.maven.commons.util.ArtifactUtils;
import org.carlspring.strongbox.storage.repository.Repository;
import org.springframework.stereotype.Component;

/**
 * @author stodorov
 * @author mtodorov
 */
@Component
public class SnapshotVersionValidator
        implements VersionValidator
{


    /**
     * Matches versions:
     * 1.0-20131004
     * 1.0-20131004.115330
     * 1.0-20131004.115330-1
     */
    @Override
    public void validate(Repository repository, String artifactPath)
            throws VersionValidationException
    {
        if (ArtifactUtils.isArtifact(artifactPath))
        {
            Artifact artifact = ArtifactUtils.convertPathToArtifact(artifactPath);

            String version = artifact.getVersion();
            if (isSnapshot(version) && !repository.acceptsSnapshots())
            {
                throw new VersionValidationException("Cannot deploy a SNAPSHOT artifact to a repository with a release policy!");
            }
            if (!isSnapshot(version) && repository.acceptsSnapshots())
            {
                throw new VersionValidationException("Cannot deploy a release artifact to a repository with a SNAPSHOT policy!");
            }
        }
    }

    public boolean isSnapshot(String version)
    {
        return version != null &&
               version.matches("^([0-9]+)(\\.([0-9]+))(-(SNAPSHOT|([0-9]+)(\\.([0-9]+)(-([0-9]+))?)?))$");
    }

}
