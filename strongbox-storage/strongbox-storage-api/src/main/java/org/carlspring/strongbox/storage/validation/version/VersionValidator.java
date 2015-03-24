package org.carlspring.strongbox.storage.validation.version;

import org.carlspring.strongbox.storage.repository.Repository;

import org.apache.maven.artifact.Artifact;

/**
 * @author mtodorov
 */
public interface VersionValidator
{

    /**
     * Checks if an artifact version is acceptable by the repository.
     *
     * @param repository    The repository.
     * @param artifactPath  The artifact being deployed.
     */
    void validate(Repository repository, String artifactPath) throws VersionValidationException;

}
