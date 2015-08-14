package com.offbytwo.jenkins.integration;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildCause;
import com.offbytwo.jenkins.model.BuildChangeSet;
import com.offbytwo.jenkins.model.BuildChangeSetAuthor;
import com.offbytwo.jenkins.model.BuildChangeSetItem;
import com.offbytwo.jenkins.model.BuildChangeSetPath;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MavenJobWithDetails;
import com.offbytwo.jenkins.model.View;

public class JenkinsChangeSetExamplet {

    @Test
    public void shouldAddStringParamToAnExistingJob() throws IOException {
         JenkinsServer js = new
         JenkinsServer(URI.create("http://localhost:10090/"));
//        JenkinsServer js = new JenkinsServer(URI.create("http://ci.soebes.de:8080/"));
        Map<String, View> views = js.getViews();
        for (Entry<String, View> view : views.entrySet()) {
            System.out.println("view: " +view.getKey() + " " + view.getValue().getName());
            View selectedView = js.getView(view.getKey());
            System.out.println(" Selected View: " + selectedView.getName());
            List<Job> jobs = selectedView.getJobs();
            for (Job job : jobs) {
                System.out.println(" Selected View job: " + job.getName());
            }
        }
//        MavenJobWithDetails mavenJob = js.getMavenJob("javaee");
        MavenJobWithDetails mavenJob = js.getMavenJob("appassembler-maven-plugin");

        BuildWithDetails details = mavenJob.getLastBuild().details();
//        BuildWithDetails details = mavenJob.getBuilds().get(10).details();
        System.out.println("Build Number: " + details.getNumber());

        List<BuildCause> causes = details.getCauses();
        for (BuildCause buildCause : causes) {
            System.out.println("-----------------------");
            System.out.println(" Cause:" + buildCause.getShortDescription());
            System.out.println(" username:" + buildCause.getUserName());
            System.out.println(" userId:" + buildCause.getUserId());
        }
        System.out.println("==============================================");
        BuildChangeSet changeSet = details.getChangeSet();
        System.out.println("ChangeSet Kind:" + changeSet.getKind());
        System.out.println("Number of CS:" + changeSet.getItems().size());

        List<BuildChangeSetAuthor> culprits = details.getCulprits();

        for (BuildChangeSetAuthor item : culprits) {
            System.out.println(" Culprit item:");
            System.out.println("   fullName: " + item.getFullName() + " url:" + item.getAbsoluteUrl());
        }
        List<BuildChangeSetItem> items = changeSet.getItems();
        System.out.println("ChangeSet:");
        for (BuildChangeSetItem item : items) {
            System.out.println(" comment:" + item.getComment());
            System.out.println(" affectedPath:");
            for (String path : item.getAffectedPaths()) {
                System.out.println(" Path:" + path);
            }
            System.out.println(" commitId:" + item.getCommitId());
            System.out.println(" date:" + item.getDate());
            System.out.println(" id:" + item.getId());
            System.out.println(" msg:" + item.getMsg());
            System.out.println(" timestamp:" + item.getTimestamp());
            System.out.println(" path:");
            for (BuildChangeSetPath changeSetPath : item.getPaths()) {
                System.out.println("  CS: " + changeSetPath.getEditType() + " " + changeSetPath.getFile());
            }
            System.out.println(" Author:");
            System.out.println("    url:" + item.getAuthor().getAbsoluteUrl());
            System.out.println("    fullName:" + item.getAuthor().getFullName());
        }

    }
}