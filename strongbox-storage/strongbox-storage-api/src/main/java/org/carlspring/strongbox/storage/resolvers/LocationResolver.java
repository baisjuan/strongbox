package org.carlspring.strongbox.storage.resolvers;

import org.apache.maven.artifact.Artifact;
import org.carlspring.strongbox.io.ArtifactInputStream;
import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * @author mtodorov
 */
public interface LocationResolver
{

    ArtifactInputStream getInputStream(String storageId,
                                       String repositoryId,
                                       String path)
            throws IOException, NoSuchAlgorithmException;

    OutputStream getOutputStream(String storageId, String repositoryId, String path)
            throws IOException;

    boolean contains(String storageId, String repositoryId, String path)
            throws IOException;

    void delete(String storageId, String repositoryId, String path, boolean force)
            throws IOException;

    void deleteTrash(String storageId, String repositoryId)
            throws IOException;

    void deleteTrash()
            throws IOException;

    void undelete(String storageId, String repositoryId, String path)
            throws IOException;

    void undeleteTrash(String storageId, String repositoryId)
            throws IOException;

    void undeleteTrash()
            throws IOException;

    void initialize()
            throws IOException;

    String getAlias();

    void setAlias(String alias);

    void updateMetadata(String storageId, String repositoryId,Artifact artifact) throws NoSuchAlgorithmException, IOException, XmlPullParserException;
}