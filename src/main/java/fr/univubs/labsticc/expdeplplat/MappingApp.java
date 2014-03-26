package fr.univubs.labsticc.expdeplplat;

import com.jcraft.jsch.JSchException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class MappingApp {

    /* This is the requiered service 
     * by the patient/doctor/system
     * I can be a couple TASK/RES
     * but also a simple TASK
     */
    private static final String TASK = "_TSK_BigBunnyVideo";
    private static final String subTASK[] = {"_TSK_BBVClient", "_TSK_BBVServer"};
    
    /* This is the ontology */
    private static final String ONTOLOGY = "/home/seguin/ontologies/examples/exp_lampes_rdf.owl";

    private final Path fFilePath;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, JSchException, IOException{
        MappingApp mapper = new MappingApp(ONTOLOGY);
        String[] AvailableResTab = initAvailableResTab(mapper.fFilePath);
        //tabScanning(AvailableResTab);
        /* Create an ontology object */
        InferenceEngine ontology = new InferenceEngine(ONTOLOGY);
        /* Perform ontology inferences */
        ontology.performInferences();
        /* Fill a Tab with the Resources */
        String[] AbleResTab = fillAbleResTab(mapper.fFilePath, AvailableResTab, TASK);
        //tabScanning(AbleResTab);
        /* Perform assignation algorithm to chose the best placement */
        String Resource = performPlacementAlgorithm(mapper.fFilePath, AbleResTab);
        /* Execute the task on the Environment */
        //CommandSender sender = new CommandSender(ONTOLOGY, Resource, TASK);
        String Command = getResCommand(Resource);
        RuntimeCmdSender(Command);
        log("Service executed on "+Resource);
    }

    /**
     *
     * Constructor
     *
     */
    public MappingApp(String aFileName) {
        fFilePath = Paths.get(aFileName);
    }

    /**
     *
     * @param fFilePath
     * @return AvailableResTab
     */
    private static String[] initAvailableResTab(Path fFilePath) {
        /* Method not implemented yet 
         * Tab is manually filled but 
         * it should be obtained from ONTO
         */
        String AvailableResTab[] = {"_RES_Pc1", "_RES _Pc2", "_RES_AlaCon"};
        //TabScanning(AvailableResTab);
        return AvailableResTab;
    }

    /**
     *
     * @param fFilePath
     * @param AvailableResTab
     * @param TASK
     * @return AbleResTab
     */
    private static String[] fillAbleResTab(Path fFilePath, String AvailableResTab[], String TASK) {
        /* Method not implemented yet 
         * Tab is manually filled but 
         * it should be obtained from ONTO
         */
        //String AbleResTab[] = new String[3]; // 3 Resources Max Available
        String AbleResTab[] = {"_RES_Pc1", "_RES_Pc2"};
        //TabScanning(AbleResTab);
        return AbleResTab;
    }

    /**
     * 
     * @param fFilePath
     * @param AvailableResTab
     * @param TASK
     */
    private static String performPlacementAlgorithm(Path fFilePath,String AbleResTab[]) {
        /* Method not implemented yet 
         * A random algorithm is carried out
         * We need to use a better way to assign
         * task considering the performance listed
         * in the ontology file.
         */
        int idx = new Random().nextInt(AbleResTab.length);
        String random = (AbleResTab[idx]);
        return random;
    }
    
    private static String getResCommand(String resource){
        if ("_RES_Pc1".equals(resource)){
            return "ssh seguin@192.168.100.1 gst-launch-0.10 filesrc location=/home/seguin/bigbunny/big_buck_bunny_1080p_stereo.ogg ! oggdemux ! queue ! theoradec ! xvimagesink display=:0";
        } else if ("_RES_Pc2".equals(resource)){
            return "ssh cedric@192.168.100.2 gst-launch-0.10 filesrc location=/home/cedric/igepv2/bigBunny/big_buck_bunny_1080p_stereo.ogg ! oggdemux ! queue ! theoradec ! xvimagesink display=:0";
        } else {
            log("Unknown resource"+ resource);
            return null;
        }
    }
    
    private static void RuntimeCmdSender(String command) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        try {
            final Process process = runtime.exec(command);
            /* Consommation de la sortie standard de l'application externe dans un Thread separe */
            new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line = "";
                        try {
                            while ((line = reader.readLine()) != null) {
                                // Traitement du flux de sortie de l'application si besoin est
                                log("sortie : "+line);
                            }
                        } finally {
                            reader.close();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }.start();
            /* Consommation de la sortie d'erreur de l'application externe dans un Thread separe */
            new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                        String line = "";
                        try {
                            while ((line = reader.readLine()) != null) {
                                // Traitement du flux d'erreur de l'application si besoin est
                                log("erreur : "+line);
                            }
                        } finally {
                            reader.close();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }.start();
        } catch (IOException e) {
        }

    }
    
    /* Private method for printing results */
    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }

    /* Private method to browse a tab */
    private static void tabScanning(String[] tab) {
        for (String str : tab) {
            log(str);
        }
    }

}
