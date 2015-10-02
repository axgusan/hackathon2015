package j4kdemo.videoviewerapp;

public class KinectGesture {

	public String type ="none";
	public float amount;
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass().equals(this.getClass())){
			KinectGesture kg = (KinectGesture) obj;
			return kg.type.equals(this.type);
		}
		return false;
	}
}
