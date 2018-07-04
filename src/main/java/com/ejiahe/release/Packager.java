package com.ejiahe.release;

import com.ejiahe.Application;
import com.ejiahe.app.license.uitls.compress.TarGzipTask;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author mailingfeng
 * Created on 18-5-22.
 */
public class Packager {

    private static final Logger Log = LoggerFactory.getLogger(Packager.class);

    //DIY
    private static final String VERSION = Application.getVersion();
    private static final String APP_DEV_HOME = "E:\\workspace\\License\\";
    public static final String APP_NAME = "cipherlicense";

    //Path
    public File changeLog = new File(APP_DEV_HOME + "/changelog.html");
    public File readme = new File(APP_DEV_HOME + "/README.md");
    public File installShell = new File(APP_DEV_HOME + "/install/" + APP_NAME + ".sh");

    public File sourceDir = new File(APP_DEV_HOME + "/out/artifacts/" + APP_NAME);
    public File outputTmpDir = new File(APP_DEV_HOME + "/out/dist/tmp");
    public File outputReleaseDir = new File(APP_DEV_HOME + "/out/dist/release");

    public File outputFile = new File(outputTmpDir, APP_NAME + ".tar.gz");
    public File releasePackage = new File(outputReleaseDir, VERSION + ".tar.gz");

    public static void main(String[] args){

        try {
            new Packager().build();
        } catch (IOException e) {
            Log.error(e.getMessage(),e);
        }

    }

    public void build() throws IOException {
        Log.info("Build Start...");

        Log.info("check the conditions...");
        checkSourceDir();
        checkSourceInstallShell();
        checkTargetDir();

        Log.info("try to remove TarGzip...");

        if(outputFile.exists()){
            outputFile.delete();
        }

        Log.info("tarGzip the sourceDir to..." + outputFile.getAbsolutePath());
        new TarGzipTask(sourceDir, outputFile).tarGzip();

        Log.info("move install shell to..." + outputTmpDir);
        Files.copy(installShell, new File(outputTmpDir, installShell.getName()));

        Log.info("move changelog.html to..." + outputTmpDir);
        Files.copy(changeLog, new File(outputTmpDir, changeLog.getName()));

        Log.info("try move README.md to..." + outputTmpDir);
        if(readme != null && readme.exists()){
            Files.copy(readme, new File(outputTmpDir, readme.getName()));
        }

        Log.info("tarGzip the release package to..." + releasePackage);
        if(releasePackage.exists()){
            releasePackage.delete();
        }

        new TarGzipTask(outputTmpDir, releasePackage, false).tarGzip();

        Log.info("Build Success!");

    }


    private void checkSourceDir(){
        if(sourceDir == null || !sourceDir.exists()){
            throw new IllegalStateException("the sourceDir can not found: " + sourceDir);
        }
    }

    private void checkSourceInstallShell(){
        if(installShell == null || !installShell.exists()){
            throw new IllegalStateException("the installShell can not found: " + installShell);
        }
    }


    private void checkTargetDir(){
        if(outputTmpDir == null || (outputTmpDir.exists() && !outputTmpDir.isDirectory())){
            throw new IllegalStateException("the outputDir must be an directory: " + outputTmpDir);
        }

        if(outputReleaseDir == null || (outputReleaseDir.exists() && !outputReleaseDir.isDirectory())){
            throw new IllegalStateException("the outputDir must be an directory: " + outputReleaseDir);
        }

        if(!outputTmpDir.exists()){
            outputTmpDir.mkdirs();
        }

        if(!outputReleaseDir.exists()){
            outputReleaseDir.mkdirs();
        }
    }

}
