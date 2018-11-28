public class MobilePhoneSet{
	Exchange parent = null;
	String type = "mobilePhoneSet";
	Myset set = new Myset();
	

	public MobilePhoneSet(Exchange b){
		parent = b;
	}

	public String type(){
		return type;
	}

	public Exchange parent(){
		return parent;
	}

	public Node search(int number){
		Node temp = set.head;
		while(temp!=null){
			if(((MobilePhone)(temp.obj)).mobileNumber == number){
				return temp;
			}
			temp = temp.right;
		}
		return null;
	}

	public boolean isMember(int number){
		if(search(number)==null){
			return false;
		}
		return true;
	}

	public boolean isEmpty(){
		return set.isEmpty();
	}

	public void addMobilePhone(int number,String status)throws ExceptionOccurred{
		//try{
			if(search(number) == null){
				MobilePhone phone = new MobilePhone(number);
				phone.parent = parent;
				phone.status = status;
				set.insert(phone);
			}
			else{
				throw new ExceptionOccurred("Error- Mobile Number ="+number+" Already exists !!");
			}
		//}catch(ExceptionOccurred e){
		//	System.out.println(e.getMessage());
		//}
	}


	public void removeMobilePhone(int number)throws ExceptionOccurred{
		//try{
			Node delNode = search(number);

			if(delNode == null){
				throw new ExceptionOccurred("Error- Mobile Number = "+number+" does not exist !!");
			}
			else{
				set.delete(delNode.obj);
			}
		//}catch(ExceptionOccurred e){
			//System.out.println(e.getMessage());
		//}
	}

	public String toString(){
		Node str = set.head;
		String s = "";
		if(str!=null){
			if(((MobilePhone)(str.obj)).status())
				s += ((MobilePhone)(str.obj)).mobileNumber;
			str = str.right;
		}
		while(str != null){
			if(((MobilePhone)(str.obj)).status())
				s += ", "+((MobilePhone)(str.obj)).mobileNumber;
			str = str.right;
		}
		
		if(s.equals(""))
			return "No elements in Resident set of this Exchange Node.";
		else{
			if(s.charAt(0)==',')
				s=s.substring(2);
			return s;
		}
	}

	public MobilePhoneSet intersection(MobilePhoneSet b)throws ExceptionOccurred{
		MobilePhoneSet intersectionSet = new MobilePhoneSet(parent);
		Node secondHead = b.set.head;
		while(secondHead != null){
			int num = ((MobilePhone)(secondHead.obj)).mobileNumber;
			if(this.isMember(num))
				intersectionSet.addMobilePhone(num,((MobilePhone)(secondHead.obj)).status);
			secondHead = secondHead.right;
		}
		return intersectionSet;
	}

	public MobilePhoneSet union(MobilePhoneSet b)throws ExceptionOccurred{
		MobilePhoneSet unionSet = new MobilePhoneSet(parent);
		Node head = b.set.head;
		while(head!=null){
			int num = ((MobilePhone)(head.obj)).mobileNumber;
			unionSet.addMobilePhone(num,((MobilePhone)(head.obj)).status);
			head = head.right;
		}
		head = this.set.head;
		while(head!=null){
			int num = ((MobilePhone)(head.obj)).mobileNumber;
			if(!unionSet.isMember(num)){
				unionSet.addMobilePhone(num,((MobilePhone)(head.obj)).status);
			}
			head = head.right;
		}
		return unionSet;
	}

	public static void main(String[] args)throws ExceptionOccurred{
			Exchange root = new Exchange(0);
			MobilePhoneSet mps = new MobilePhoneSet(root);
			root.updateResidentSet(mps);
			mps.addMobilePhone(7,"on");
			mps.addMobilePhone(45,"on");
			
			System.out.println("mps="+mps.toString());

			System.out.println("contains 8 = "+mps.isMember(8));
			System.out.println("contains 7 = "+mps.isMember(7));
			System.out.println("contains 45 = "+mps.isMember(45));
			System.out.println("contains 9 = "+mps.isMember(9));
			//mps.removeMobilePhone(9);
			mps.removeMobilePhone(7);
			System.out.println("mps = "+mps.toString());
			MobilePhoneSet mps2 = new MobilePhoneSet(root);
			mps2.addMobilePhone(9,"on");
			mps2.addMobilePhone(45,"on");
			mps2.addMobilePhone(8,"on");
			System.out.println("mps2 = "+mps2.toString());
			MobilePhoneSet unionAB = mps.union(mps2);
			System.out.println("union = "+unionAB.toString());
			MobilePhoneSet intersectionAB = mps.intersection(mps2);
			System.out.println("intersection = "+intersectionAB.toString());
			System.out.println("mps = "+mps.toString());
			System.out.println("mps2 = "+mps2.toString());
	}
} 