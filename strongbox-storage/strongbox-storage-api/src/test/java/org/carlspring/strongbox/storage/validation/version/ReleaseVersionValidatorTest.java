package org.carlspring.strongbox.storage.validation.version;

import org.apache.maven.artifact.Artifact;
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
 * @author mtodorov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/strongbox-*-context.xml", "classpath*:/META-INF/spring/strongbox-*-context.xml"})
public class ReleaseVersionValidatorTest extends TestCaseWithArtifactGeneration
{

    @Autowired
    private ConfigurationManager configurationManager;

    private Repository repository;

    private ReleaseVersionValidator validator = new ReleaseVersionValidator();


    @Before
    public void setUp()
            throws Exception
    {
        repository = configurationManager.getConfiguration().getStorage("storage0").getRepository("releases");
    }

    @Test
    public void testReleaseValidation()
            throws VersionValidationException, NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        /**
         * Test valid artifacts
         */
        Artifact validArtifact1 = generateArtifact(repository.getBasedir(), "com.foo:bar:1");
        Artifact validArtifact2 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0");

        validator.validate(repository, ArtifactUtils.convertArtifactToPath(validArtifact1));
        validator.validate(repository, ArtifactUtils.convertArtifactToPath(validArtifact2));

        // If we've gotten here without an exception, then things are alright.
    }

    @Test
    public void testInvalidArtifacts()
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        /**
         * Test invalid artifacts
         */
        Artifact invalidArtifact3 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-SNAPSHOT");
        Artifact invalidArtifact4 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-20131004");
        Artifact invalidArtifact5 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-20131004.115330");
        Artifact invalidArtifact6 = generateArtifact(repository.getBasedir(), "com.foo:bar:1.0-20131004.115330-1");

        try
        {
            validator.validate(repository, ArtifactUtils.convertArtifactToPath(invalidArtifact3));
            fail("Incorrectly validated artifact with version 1.0-SNAPSHOT!");
        }
        catch (VersionValidationException ignored)
        {
        }

        try
        {
            validator.validate(repository, ArtifactUtils.convertArtifactToPath(invalidArtifact4));
            fail("Incorrectly validated artifact with version 1.0-20131004!");
        }
        catch (VersionValidationException ignored)
        {
        }

        try
        {
            validator.validate(repository, ArtifactUtils.convertArtifactToPath(invalidArtifact5));
            fail("Incorrectly validated artifact with version 1.0-20131004.115330!");
        }
        catch (VersionValidationException ignored)
        {
        }

        try
        {
            validator.validate(repository, ArtifactUtils.convertArtifactToPath(invalidArtifact6));
            fail("Incorrectly validated artifact with version 1.0-20131004.115330-1!");
        }
        catch (VersionValidationException ignored)
        {
        }
    }

}
