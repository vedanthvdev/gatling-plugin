package io.gatling.jenkins;

import hudson.model.Action;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReportViewer implements Action {
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

        rsp.setContentType("text/html");
        try (InputStream in = new FileInputStream(indexFile);
             OutputStream out = rsp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        String path = req.getRestOfPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        File reportDir = simulation.getSimulationDirectory();
        File requestedFile = new File(reportDir, path);

        if (!requestedFile.exists() || !requestedFile.getCanonicalPath().startsWith(reportDir.getCanonicalPath())) {
            rsp.sendError(404, "File not found");
            return;
        }

        String contentType = getContentType(path);
        rsp.setContentType(contentType);

        try (InputStream in = new FileInputStream(requestedFile);
             OutputStream out = rsp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}