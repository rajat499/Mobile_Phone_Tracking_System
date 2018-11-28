public class MobilePhone{
	int mobileNumber;
	String status = "on";
	Exchange parent = null;

	public MobilePhone(int number){
		mobileNumber = number;
	}

	public int number(){
		return mobileNumber;
	}

	public String type(){
		return "mobile";
	}

	public boolean status(){
		if(this.status == "on")
			return true;
		return false;
	}

	public void switchOn(){
		this.status = "on";
	}

	public void switchOff(){
		this.status = "off";
	}

	public Exchange location()throws ExceptionOccurred{
		//try{
			if(this.status == "off"){
				throw new ExceptionOccurred("Error- Phone switched off. Cannot return base Exchange.");
			}
			return parent;
		//}catch(ExceptionOccurred e){
		//	System.out.println(e.getMessage());
		//	return null;
		//}
	}
}