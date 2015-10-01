package com.darkprograms.speech.raiseYourHand;

import java.util.List;

public class GooglesAnswer {

	private String googleSaid = null;
	private Double googleConfidence = (double) 0;
	private List<String> otherPoscibilities = null;

	// getters
	public String getBestGuess() {
		return googleSaid;
	}

	public double getProbability() {
		return googleConfidence;
	}

	public List<String> getOtherGuesses() {
		return otherPoscibilities;
	}

	// setters
	public void setBestGuess(String bestGuess) {
		googleSaid = bestGuess;
	}

	public void setProbability(double confidence) {
		googleConfidence = confidence;
	}

	public void setOtherPoscibilities(List<String> other) {
		otherPoscibilities = other;
	}
}
