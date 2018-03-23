package eu.ensg.jade.rules;

/**
 * Rule is the class implementing the rules that are used to place a punctual object
 * 
 * @author JADE
 */

public class Rule implements IRule{

// ========================== ATTRIBUTES ===========================


// ========================== CONSTRUCTORS =========================	


// ========================== GETTERS/SETTERS ======================


// ========================== METHODS ==============================
	
	/*
	 * Fonction qui test le nombre de routes par intersection
	 * - Si 1 => cul de sac 
	 * - Si 2 => Rétrécissement
	 * 		  => Test de sens
	 *        => ou rien
	 * - Si 3 => Test Bretelle
	 *        => Test Rond point
	 * 		  => Test de sens
	 * 		  => Test d'importance
	 * 		  => Test nombre de voies
	 * 		  => Algo de placement de signalisation en fonction des résultats obtenus
	 * 
	 * - Si 4 => Test Rond point
	 * 		  => Test de sens
	 * 		  => Test d'importance
	 * 		  => Test nombre de voies
	 * 		  => Algo de placement de signalisation en fonction des résultats obtenus
	 * - Si 5 ou plus 
	 *        => Test de sens 
	 *        => Cédez le passage et flèches bleu pour modéliser un rond point. 
	 * 
	 * CAS PARTICULIERS : BRETELLE, ROND POINT (ATTENTION,toujours test de sens)
	 */
	
}
