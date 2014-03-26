/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.univubs.labsticc.expdeplplat;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
/**
 *
 * @author cedric
 */
public class InferenceEngine {
    
    private static String ontology;

    /**
     * Constructor.
     *
     * @param ONTO
     */
    public InferenceEngine(String ONTO) {
        ontology = ONTO;
    }

    public final void performInferences() throws OWLOntologyCreationException, OWLOntologyStorageException {
        /* Get hold of an ontology manager */
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        /* Create a file object that points to the local copy */
        File Ontology = new File(ontology);
        /* Load the local copy */
        OWLOntology localOnto = manager.loadOntologyFromOntologyDocument(Ontology);
        /* Create Pellet reasoner */
        com.clarkparsia.pellet.owlapiv3.PelletReasoner reasoner
                = PelletReasonerFactory.getInstance().createReasoner(localOnto);
        /* Get hold of an infered ontology manager */
        OWLOntology inferredOnto = manager.createOntology();
        /* Perform the inferences with Pellet reasoner */
        InferredOntologyGenerator generator
                = new InferredOntologyGenerator(reasoner);
        /* Fill the infered Ontology with the obtained solution */
        generator.fillOntology(manager, inferredOnto);
        /* Save the infered Ontology into the initial File */
        IRI ontoIRI = IRI.create(Ontology);
        manager.saveOntology(inferredOnto, ontoIRI);
        //manager.saveOntology(inferredOnto, new OWLXMLOntologyFormat(), ontoIRI);
        /* Remove the ontology again so we can reload it later */
        manager.removeOntology(localOnto);
        manager.removeOntology(inferredOnto);
    }
}

/* At this point we are able to perform inferences and
 * save them in the same ontology file.
 * However, the SWRL rule is deleted during the process
 * we should re-write it in the file.
 */