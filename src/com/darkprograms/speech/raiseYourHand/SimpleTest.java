package com.darkprograms.speech.raiseYourHand;

public class SimpleTest {

	public static void main(String[] args) {

		EventDrivenSpeachToTextController mcontroller = new EventDrivenSpeachToTextController();
		GooglesAnswer theResult;

		System.out.println("INFO: Attempting to start audio capture.");
		mcontroller.startAudioCapture();
		try {
			System.out.println("INFO: Waiting for 5 sec while recording.");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}// Records for 5 seconds
		
		System.out.println("INFO: Attempting to end audio capture.");
		theResult = mcontroller.endAudioCapture();

		System.out.println("INFO: ↓↓↓↓↓↓↓↓RESULTS RECIEVED↓↓↓↓↓↓↓↓");
		System.out.println("INFO: best guess    > " + theResult.getBestGuess());
		System.out.println("INFO: probability   > "
				+ theResult.getProbability());
		System.out.println("INFO: other guesses > "
				+ theResult.getOtherGuesses());
		System.out.println("INFO: ↑↑↑↑↑↑↑↑RESULTS RECIEVED↑↑↑↑↑↑↑↑");
	}

}
