//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
package edu.iu.dsc.tws.rsched.schedulers.aurora;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.iu.dsc.tws.common.config.Config;
import edu.iu.dsc.tws.common.config.ConfigLoader;
import edu.iu.dsc.tws.rsched.core.SchedulerContext;

/**
 * This is the class to submit Twister2 jobs to AuroraCluster
 */
public final class AuroraJobSubmitter {
  private static final Logger LOG = Logger.getLogger(AuroraJobSubmitter.class.getName());

  private AuroraJobSubmitter() {
  }

  public static void main(String[] args) {
    Options cmdOptions = null;
    try {

      // get command line parameters
      cmdOptions = setupOptions();
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(cmdOptions, args);

      // load the configurations from config files
      // we are loading the configuration for all the components
      Config config = loadConfigurations(cmd);
      System.out.println("all config entries");
      System.out.println("number of config parameters: " + config.size());
      System.out.println(config);

      //construct the controller to submit the job to Aurora Scheduler
      String cluster = AuroraClientContext.cluster(config);
      String role = AuroraClientContext.role(config);
      String env = AuroraClientContext.environment(config);
      String jobName = AuroraClientContext.auroraJobName(config);

      AuroraClientController controller = new AuroraClientController(
          cluster, role, env, jobName, true);

      // get aurora file name to execute when submitting the job
      String auroraFilename = AuroraClientContext.auroraScript(config);

      // get environment variables from config
      Map<AuroraField, String> bindings = constructEnvVariables(config);
      // print all environment variables for debugging
      printEnvs(bindings);

      boolean jobSubmitted = controller.createJob(bindings, auroraFilename);
      if (jobSubmitted) {
        LOG.log(Level.INFO, "job submission is successfull ...");
      } else {
        LOG.log(Level.SEVERE, "job submission to Aurora failed ...");
      }

    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("AuroraJobSubmitter", cmdOptions);
      throw new RuntimeException("Error parsing command line options: ", e);
    }
  }

  /**
   * Setup the command line options for AuroraJobSubmitter
   * It gets three command line parameters:
   * twister2_home: home directory for twister2
   * config_dir: config directory for twister2 project
   * cluster_name: it should be "aurora"
   * packagePath: path of twister2 tar.gz file to be uploaded to Mesos container
   * packageFile: filename of twister2 tar.gz file to be uploaded to Mesos container
   *
   * @return cli options
   */
  private static Options setupOptions() {
    Options options = new Options();

    Option twister2Home = Option.builder("t")
        .desc("The class name of the container to launch")
        .longOpt("twister2_home")
        .hasArgs()
        .argName("twister2 home")
        .required()
        .build();

    Option configDirectory = Option.builder("d")
        .desc("The config directory")
        .longOpt("config_dir")
        .hasArgs()
        .argName("configuration directory")
        .required()
        .build();

    Option clusterName = Option.builder("n")
        .desc("The clustr name")
        .longOpt("cluster_name")
        .hasArgs()
        .argName("cluster name")
        .required()
        .build();

//    Option packagePath = Option.builder("p")
//        .desc("Package path")
//        .longOpt("package_path")
//        .hasArgs()
//        .argName("path to twister2 package file")
//        .required()
//        .build();
//
//    Option packageFile = Option.builder("f")
//        .desc("Package file name")
//        .longOpt("package_file")
//        .hasArgs()
//        .argName("filename for twister2 package")
//        .required()
//        .build();

    options.addOption(twister2Home);
    options.addOption(configDirectory);
    options.addOption(clusterName);
//    options.addOption(packagePath);
//    options.addOption(packageFile);

    return options;
  }

  /**
   * read config parameters from configuration files
   * all config files are in a single directory
   *
   * @return Config object that has values from config files and from command line
   */
  private static Config loadConfigurations(CommandLine cmd) {
    String twister2Home = cmd.getOptionValue("twister2_home");
    String configDir = cmd.getOptionValue("config_dir");
    String clusterName = cmd.getOptionValue("cluster_name");
//    String packagePath = cmd.getOptionValue("package_path");
//    String packageFile = cmd.getOptionValue("package_file");

    LOG.log(Level.INFO, String.format("Initializing process with "
            + "twister_home: %s config_dir: %s cluster_name: %s",
        twister2Home, configDir, clusterName));

    try {
//      Reflection.initialize(Class.forName(
// "edu.iu.dsc.tws.rsched.schedulers.aurora.AuroraClientContext"));
      Class.forName(AuroraClientContext.class.getName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    Config config = ConfigLoader.loadConfig(twister2Home, configDir + "/" + clusterName);

    return Config.newBuilder().putAll(config).
        put(SchedulerContext.TWISTER2_HOME.getKey(), twister2Home).
        put(SchedulerContext.TWISTER2_CLUSTER_NAME, clusterName).
//        put(AuroraClientContext.TWISTER2_PACKAGE_PATH, packagePath).
//        put(AuroraClientContext.TWISTER2_PACKAGE_FILE, packageFile).
    build();
  }

  /**
   * put relevant config parameters to a HashMap to be used as environment variables
   * when submitting jobs
   */
  public static Map<AuroraField, String> constructEnvVariables(Config config) {
    HashMap<AuroraField, String> envs = new HashMap<AuroraField, String>();
    envs.put(AuroraField.TWISTER2_PACKAGE_PATH, AuroraClientContext.packagePath(config));
    envs.put(AuroraField.TWISTER2_PACKAGE_FILE, AuroraClientContext.packageFile(config));
    envs.put(AuroraField.CLUSTER, AuroraClientContext.cluster(config));
    envs.put(AuroraField.ENVIRONMENT, AuroraClientContext.environment(config));
    envs.put(AuroraField.ROLE, AuroraClientContext.role(config));
    envs.put(AuroraField.AURORA_JOB_NAME, AuroraClientContext.auroraJobName(config));
    envs.put(AuroraField.CPUS_PER_CONTAINER, AuroraClientContext.cpusPerContainer(config));
    envs.put(AuroraField.RAM_PER_CONTAINER, AuroraClientContext.ramPerContainer(config) + "");
    envs.put(AuroraField.DISK_PER_CONTAINER, AuroraClientContext.diskPerContainer(config) + "");
    envs.put(AuroraField.NUMBER_OF_CONTAINERS, AuroraClientContext.numberOfContainers(config));
    return envs;
  }

  /**
   * print all environment variables for debuging purposes
   */
  public static void printEnvs(Map<AuroraField, String> envs) {
    LOG.log(Level.INFO, "All environment variables when submitting Aurora job");
    Set<AuroraField> keys = envs.keySet();

    for (AuroraField key : keys) {
      System.out.println(key + ": " + envs.get(key));
    }
  }
}
