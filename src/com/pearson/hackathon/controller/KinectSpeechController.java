package com.pearson.hackathon.controller;

import java.lang.reflect.GenericSignatureFormatError;

import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.pearson.hackathon.ui.NotePanel;

public class KinectSpeechController implements KinectGestureListener {

	GSpeechResponseListener speechResponseListener;
	NotePanel notePanel;
	
	public KinectSpeechController() {
		
		notePanel = NotePanel.createAndShowGUI();
	}
	
	
	public void onReceiveGesture(KinectGesture gesture){
		
		// when a gesture is received get speech and put it in the ui
		String speech ;  // = Gspeech.getCurrent
		
		
		
	}
	
	private String getStyleFromGesture(KinectGesture gesture){
		String returnString = null;
		
		
		return returnString;
	}
}
