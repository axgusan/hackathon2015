package com.pearson.hackathon.controller;

import java.util.Scanner;

import com.darkprograms.speech.raiseYourHand.EventDrivenSpeachToTextController;
import com.darkprograms.speech.raiseYourHand.GooglesAnswer;
import com.darkprograms.speech.raiseYourHand.SpeechListener;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.pearson.hackathon.ui.NotePanel;

import j4kdemo.videoviewerapp.Kinect;
import j4kdemo.videoviewerapp.KinectGesture;
import j4kdemo.videoviewerapp.KinectGestureListener;

public class KinectSpeechController implements KinectGestureListener, SpeechListener {

	GSpeechResponseListener speechResponseListener;
	NotePanel notePanel;
	EventDrivenSpeachToTextController mcontroller ;
	
	public KinectSpeechController(boolean useKinect) {
		
		notePanel = NotePanel.createAndShowGUI();
		if(useKinect){
			Kinect kinect = new Kinect();
			kinect.registerGestureListener(this);
		
			kinect.start(Kinect.DEPTH | Kinect.COLOR | Kinect.SKELETON | Kinect.XYZ | Kinect.PLAYER_INDEX);
		}
		
	}
	
	
	public void onReceiveGesture(KinectGesture gesture){
		
		// when a gesture is received get speech and put it in the ui
		String speech ;  // = Gspeech.getCurrent
		
		if(gesture.type.equals("LEFT")){
			if(mcontroller!=null)
				return;

			mcontroller = EventDrivenSpeachToTextController.startAudioCapture(gesture,this);
		}
		if(gesture.type.equals("LEFT_LOW")&&mcontroller!=null){

			mcontroller.endAudioCapture();
			mcontroller=null;
		}
		
		
	}
	

	public void onReceived(Object gesture, GooglesAnswer answer) {
		KinectGesture g =(KinectGesture )gesture;
		
		notePanel.addNote("normal", answer.getBestGuess());
		
	}
	
	private String getStyleFromGesture(KinectGesture gesture){
		String returnString = null;
		
		
		return returnString;
	}
	
	public static void main(String[] args) {
		boolean useKinect = true;
		KinectSpeechController ksc = new KinectSpeechController(useKinect);
		
		KinectGesture start = new KinectGesture();
		start.type ="LEFT";
		
		KinectGesture end = new KinectGesture();
		end.type ="NONE";
		
		
		// wait until return is hit
		new Scanner(System.in).nextLine();
		ksc.onReceiveGesture(start);
		new Scanner(System.in).nextLine();
		ksc.onReceiveGesture(end);
		new Scanner(System.in).nextLine();
	}


}
