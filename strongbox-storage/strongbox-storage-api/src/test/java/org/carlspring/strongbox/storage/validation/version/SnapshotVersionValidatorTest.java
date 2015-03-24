package org.carlspring.strongbox.storage.validation.version;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.carlspring.maven.commons.util.ArtifactUtils;
import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.storage.repository.Repository;
import org.carlspring.strongbox.testing.TestCaseWithArtifactGeneration;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.fail;

/**
 * @author stodorov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/strongbox-*-context.xml", "classpath*:/META-INF/spring/strongbox-*-context.xml"})
public class SnapshotVersionValidatorTest extends TestCaseWithArtifactGeneration
{

    @Autowired
    private ConfigurationManager configurationManager;

    private Repository repository;

    private SnapshotVersionValidator validator = new SnapshotVersionValidator();


    @Before
    public void setUp()
            throws Exception
    {
        repository = configurationManager.getConfiguration().getStorage("storage0").getRepository("snapshots");
    }

    @Test
    public void testSnapshotValidation()
            throws VersionValidationException, NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        final String pattern = "^([0-9]+)(\\.([0-9]+))(-(SNAPSHOT|([0-9]+)(\\.([0-9]+)(-([0-9]+))?)?))$";

        System.out.println("1.0-20131004.115330-1 ? " + "1.0-20131004.115330-1".matches(pattern));
        System.out.println("1.0-20131004          ? " + "1.0-20131004".matches(pattern));
        System.out.println("1.0-20131004.115330   ? " + "1.0-20131004.115330".matches(pattern));
        System.out.println("1.0-20131004.115330-1 ? " + "1.0-20131004.115330-1".matches(pattern));

        Artifact validArtifact1 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-SNAPSHOT");
        Artifact validArtifact2 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-20131004");
        Artifact validArtifact3 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-20131004.115330");
        Artifact validArtifact4 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-20131004.115330-1");

        validator.validate(repository, ArtifactUtils.convertArtifactToPath(validArtifact1));
        validator.validate(repository, ArtifactUtils.convertArtifactToPath(validArtifact2));
        validator.validate(repository, ArtifactUtils.convertArtifactToPath(validArtifact3));
        validator.validate(repository, ArtifactUtils.convertArtifactToPath(validArtifact4));

        // If we've gotten here without an exception, then things are alright.
    }

    @Test
    public void testInvalidArtifacts()
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        Artifact invalidArtifact1 = generateArtifact(repository.getBasedir(), "1");
        Artifact invalidArtifact2 = generateArtifact(repository.getBasedir(), "1.0");

        try
        {
            validator.validate(repository, ArtifactUtils.convertArtifactToPath(invalidArtifact1));
            fail("Incorrectly validated artifact with version 1!");
        }
        catch (VersionValidationException ignored)
        {
        }

        try
        {
            validator.validate(repository, ArtifactUtils.convertArtifactToPath(invalidArtifact2));
            fail("Incorrectly validated artifact with version 1.0!");
        }
        catch (VersionValidationException ignored)
        {
        }
    }

}
