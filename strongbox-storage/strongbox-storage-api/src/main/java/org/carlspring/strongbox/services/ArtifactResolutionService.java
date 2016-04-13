package org.carlspring.strongbox.services;

import org.carlspring.strongbox.storage.resolvers.LocationResolver;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author mtodorov
 */
public interface ArtifactResolutionService
{

    InputStream getInputStream(String storageId,
                               String repositoryId,
                               String artifactPath)
            throws IOException, NoSuchAlgorithmException;

    OutputStream getOutputStream(String storageId,
                                 String repositoryId,
                                 String artifactPath)
                    throws IOException, NoSuchAlgorithmException, XmlPullParserException;

    Map<String, LocationResolver> getResolvers();

}
