/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.AirportDuration;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="voliMinimo"
    private TextField voliMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroOreTxtInput"
    private TextField numeroOreTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnOttimizza"
    private Button btnOttimizza; // Value injected by FXMLLoader

    private int numMin = 0; 
    private int numMax = 0;
    
    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {

    	txtResult.clear();
    	
    	try {
    		
    		numMin = Integer.parseInt(voliMinimo.getText());
    		model.creaGrafo(numMin);
    		cmbBoxAeroportoPartenza.getItems().addAll(model.listaVertici());
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("Si prega di inserire un valore intero come numero di ore minimo");
    		return;
    	}
    	
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Airport scelto = cmbBoxAeroportoPartenza.getValue();
    	if(scelto!=null) {
    		  txtResult.appendText("Dall'aereoporto scelto è possibile raggiungere: \n");
    		for(AirportDuration ad : model.listaVicini(scelto)) 
    			txtResult.appendText(ad.getVicino()+" con durata media "+(double)Math.round(ad.getAvgDurata()*100)/100+"\n");
    		
    		
    	}else
    		txtResult.appendText("Si prega di scegliere un aereoporto");
    	
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {

    	txtResult.clear();	
    	try {
    		
    		numMax = Integer.parseInt(numeroOreTxtInput.getText());
    		
    		Airport scelto = cmbBoxAeroportoPartenza.getValue();
        	if(scelto!=null) {
        		 
        		txtResult.appendText("Il cammino ottimo dall'aereporto scelto è: \n\n");
        		  
        		  Set<AirportDuration> ris =  model.camminoOttimo(numMax, scelto);
        		for(AirportDuration ad : ris) 
        			txtResult.appendText(ad.getVicino()+" con durata media "+(double)Math.round(ad.getAvgDurata()*200)/100+"\n");
        		txtResult.appendText("\n Il cammino ottimo dall'aereporto scelto è formato da : "+ris.size()+" aereoporti."
        				+ "\n E prevede "+(double)Math.round(model.calcolaPeso(ris)*1000)/1000+" ore di viaggio.");
        		
        	}else
        		txtResult.appendText("Si prega di scegliere un aereoporto");
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("Si prega di inserire un valore intero come numero di ore massimo");
    		return;
    	}
    	
    	
  	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert voliMinimo != null : "fx:id=\"voliMinimo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroOreTxtInput != null : "fx:id=\"numeroOreTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnOttimizza != null : "fx:id=\"btnOttimizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
  		this.model = model;
  		
  	}
}
