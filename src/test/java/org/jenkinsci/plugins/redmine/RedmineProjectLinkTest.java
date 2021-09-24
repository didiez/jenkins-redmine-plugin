package org.jenkinsci.plugins.redmine;

import hudson.model.FreeStyleProject;
import hudson.model.Job;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

/**
 *
 * @author didiez
 */
public class RedmineProjectLinkTest {
    
    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Test
    public void testRedmineProjectLinkActionIsAddedWhenRedmineProjectIsConfigured () throws Exception {
        Job myFreestyleJob = rule.jenkins.createProject(FreeStyleProject.class, "myFreestyleJob");
        myFreestyleJob.addProperty(new RedmineWebsiteJobProperty("redmine", "myProjectOne"));
        assertEquals(1, myFreestyleJob.getActions(RedmineProjectLinkAction.class).size());

        Job myPipelineJob = rule.jenkins.createProject(WorkflowJob.class, "myPipelineJob");
        myPipelineJob.addProperty(new RedmineWebsiteJobProperty("redmine", "myProjectTwo"));
        assertEquals(1, myPipelineJob.getActions(RedmineProjectLinkAction.class).size());
    }
    
    @Test
    public void testRedmineProjectLinkActionIsNotAddedWhenRedmineProjectIsNotConfigured () throws Exception {
        Job job1 = rule.jenkins.createProject(WorkflowJob.class, "job1");
        assertTrue(job1.getActions(RedmineProjectLinkAction.class).isEmpty());

        Job job2 = rule.jenkins.createProject(WorkflowJob.class, "job2");
        job2.addProperty(new RedmineWebsiteJobProperty("redmine", null));
        assertTrue(job2.getActions(RedmineProjectLinkAction.class).isEmpty());
        
        Job job3 = rule.jenkins.createProject(WorkflowJob.class, "job3");
        job3.addProperty(new RedmineWebsiteJobProperty("redmine", ""));
        assertTrue(job3.getActions(RedmineProjectLinkAction.class).isEmpty());
    }
    
}
