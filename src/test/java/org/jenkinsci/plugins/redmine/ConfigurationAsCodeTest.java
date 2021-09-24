package org.jenkinsci.plugins.redmine;

import static io.jenkins.plugins.casc.misc.Util.getUnclassifiedRoot;
import static io.jenkins.plugins.casc.misc.Util.toStringFromYamlFile;
import static io.jenkins.plugins.casc.misc.Util.toYamlString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.model.CNode;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * Configuration as code integration tests (read & export).
 *
 * @author didiez
 */
public class ConfigurationAsCodeTest {

    @ClassRule
    @ConfiguredWithCode("configuration-as-code.yml")
    public static JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();

    @Test
    public void testConfig() throws Exception {
        final RedmineGlobalConfiguration config = j.jenkins.getDescriptorByType(RedmineGlobalConfiguration.class);

        assertNotNull(config.getWebsites());
        assertThat(config.getWebsites(), hasSize(3));

        assertThat(config.getWebsites(), containsInAnyOrder(
                new RedmineWebsite("redmine", "http://localhost/redmine/", "*,refs,references,IssueID,fixes,closes"),
                new RedmineWebsite("bluemine", "http://localhost/bluemine/", "*"),
                new RedmineWebsite("greenmine", "http://localhost/greenmine/", null)));
    }

    @Test
    public void testExport() throws Exception {
        ConfigurationContext context = new ConfigurationContext(ConfiguratorRegistry.get());
        CNode yourAttribute = getUnclassifiedRoot(context).get("redmine");

        String exported = toYamlString(yourAttribute);
        String expected = toStringFromYamlFile(this, "configuration-as-code-export.yml");

        assertThat(exported, is(expected));
    }
}
