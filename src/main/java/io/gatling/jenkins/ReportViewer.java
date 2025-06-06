package io.gatling.jenkins;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;
import hudson.model.ModelObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public class ReportViewer implements Action, ModelObject {
    private final BuildSimulation simulation;

    public ReportViewer(BuildSimulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Gatling Report";
    }

    @Override
    public String getUrlName() {
        return "report";
    }

    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        File reportDir = simulation.getSimulationDirectory();
        File indexFile = new File(reportDir, "index.html");

        if (!indexFile.exists()) {
            rsp.sendError(404, "Report not found");
            return;
        }

        // If the request is for index.html, serve it directly
        if (req.getRestOfPath().equals("/index.html")) {
            FilePath base = new FilePath(reportDir);
            DirectoryBrowserSupport dbs = new DirectoryBrowserSupport(this, base, "Gatling Report", "folder.png", true);
            dbs.setIndexFileName("index.html");
            dbs.generateResponse(req, rsp, this);
        } else {
            // Otherwise, serve our custom template
            req.getView(this, "index.jelly").forward(req, rsp);
        }
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        File reportDir = simulation.getSimulationDirectory();
        FilePath base = new FilePath(reportDir);
        DirectoryBrowserSupport dbs = new DirectoryBrowserSupport(this, base, "Gatling Report", "folder.png", true);
        dbs.setIndexFileName("index.html");
        dbs.generateResponse(req, rsp, this);
    }

    public String getSimulationName() {
        return simulation.getSimulationName();
    }

    public String getSimulationDirectory() {
        return simulation.getSimulationDirectory().getName();
    }
}