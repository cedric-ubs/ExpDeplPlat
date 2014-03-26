package fr.univubs.labsticc.expdeplplat;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
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
    private static final String TASK = "__tsk_AllumerLumiere";

    /* This is the ontology */
    private static final String ONTOLOGY = "/home/cedric/ontologies/examples/exp_lampes_rdf.owl";

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
        CommandSender sender = new CommandSender(ONTOLOGY, Resource, TASK);
        
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
        String AvailableResTab[] = {"_lampeEIB1", "_lampeEIB2", "_TVNSX"};
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
        String AbleResTab[] = {"_lampeEIB1", "_lampeEIB2"};
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
