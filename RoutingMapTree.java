import java.util.*;

public class RoutingMapTree{
	Exchange root;
	public RoutingMapTree(){
		root = new Exchange(0);
		root.upLevel = null;
		root.sameLevel = null;
	}

	public void switchOn(MobilePhone a, Exchange b)throws ExceptionOccurred{
		int mobileNumber = a.number();
		int parent = b.uid();
		Exchange parentExchangeNode = this.root.findExchange(parent);
		if(parentExchangeNode == null){
			throw new ExceptionOccurred("Error- No such parent node='"+parent+"' found.");
		}
		else{
			Node search = this.root.residentSet().search(mobileNumber);
			if(search != null){	
				if(((MobilePhone)(search.obj)).status()){
					throw new ExceptionOccurred("Error- Number-"+mobileNumber+" already switched on. You need to move the phone at Exchange-"+parent);
				}
				/*if(((MobilePhone)(search.obj)).status()){
					this.root.delete(mobileNumber);
					MobilePhoneSet set = parentExchangeNode.residentSet();
					if(set == null){
						set = new MobilePhoneSet(parentExchangeNode);
					}
					set.addMobilePhone(mobileNumber,"on");
					parentExchangeNode.updateResidentSet(set);
					throw new ExceptionOccurred("Error- Number is already Switched On at another Exchange. Shifting it to Exchange-"+parent);
				}*/
				if(parentExchangeNode.residentSet()==null){
					if(parentExchangeNode.nextLevel!=null){
						throw new ExceptionOccurred("Error- Cannot add number-"+mobileNumber+" as Exchange-"+parent+" is not level 0 exchange.");
					}
					this.root.delete(mobileNumber);
					MobilePhoneSet set = parentExchangeNode.residentSet();
					if(set == null){
						set = new MobilePhoneSet(parentExchangeNode);
					}
					set.addMobilePhone(mobileNumber,"on");
					parentExchangeNode.updateResidentSet(set);
				}
				else{
					Node mobileNode = parentExchangeNode.residentSet().search(mobileNumber);
					if(mobileNode==null){
						if(parentExchangeNode.nextLevel!=null){
							throw new ExceptionOccurred("Error- Cannot add number-"+mobileNumber+" as Exchange-"+parent+" is not level 0 exchange.");
						}
						this.root.delete(mobileNumber);
						MobilePhoneSet set = parentExchangeNode.residentSet();
						if(set == null){
							set = new MobilePhoneSet(parentExchangeNode);
						}
						set.addMobilePhone(mobileNumber,"on");
						parentExchangeNode.updateResidentSet(set);			
					}
					else{
						parentExchangeNode.switchOn(mobileNumber);
					}
				}
			}
			else{
				MobilePhoneSet set = parentExchangeNode.residentSet();
				if(set == null){
					set = new MobilePhoneSet(parentExchangeNode);
				}
				if(parentExchangeNode.nextLevel!=null){
					throw new ExceptionOccurred("Error- Cannot add number-"+mobileNumber+" as Exchange-"+parent+" is not level 0 exchange.");
				}
				set.addMobilePhone(mobileNumber,"on");
				parentExchangeNode.updateResidentSet(set);
			}
		}		
	}

	public void switchOff(MobilePhone a)throws ExceptionOccurred{
		int number = a.number();
		MobilePhoneSet set = this.root.residentSet();
		if(set==null){
			throw new ExceptionOccurred("Error- Mobile Number-"+number+" Doesn't exist");
		}
		else{
			Node find = set.search(number);
			if(find!=null){
				if(!((MobilePhone)(find.obj)).status()){
					throw new ExceptionOccurred("Error- Mobile Number-"+number+" is already switched off.");
				}
				this.root.switchOff(number);
				//this.root.delete(number);
			}
			else{
				throw new ExceptionOccurred("Error- Mobile Number-"+number+" Doesn't exist");
			}
		}
				
	}

	public void movePhone(MobilePhone a, Exchange b)throws ExceptionOccurred{
		int mobileNumber = a.number();
		int baseStation = b.uid();
		Node mobileNode = this.root.residentSet().search(mobileNumber);
		Exchange baseExchangeNode = this.root.findExchange(baseStation);
		if(mobileNode == null){
			throw new ExceptionOccurred("Error- Mobile Number-"+mobileNumber+" does not exist.");
		}
		if(baseExchangeNode == null){
			throw new ExceptionOccurred("Error- Exchange-"+baseStation+" does not exist.");	
		}
		if(baseExchangeNode.nextLevel != null){
			throw new ExceptionOccurred("Error- Exchange-"+baseStation+" is not a base station. Therefore, can not move mobile number-"+mobileNumber+" to this Exchange.");
		}
		if(!((MobilePhone)(mobileNode.obj)).status()){
			throw new ExceptionOccurred("Error-  Mobile Number-"+mobileNumber+" is switched off. Therefore, can not move to Exchange-"+baseStation);
		}
		this.root.delete(mobileNumber);
		MobilePhoneSet set = baseExchangeNode.residentSet();
		if(set == null){
			set = new MobilePhoneSet(baseExchangeNode);
		}
		set.addMobilePhone(mobileNumber,"on");
		baseExchangeNode.updateResidentSet(set);
	}

	public Exchange findPhone(MobilePhone m)throws ExceptionOccurred{
		int mobileNumber = m.number();
		Node mobileNode = this.root.residentSet().search(mobileNumber);
		if(mobileNode == null){
			throw new ExceptionOccurred("Error - No mobile phone with identifier "+mobileNumber+" found in the network");
		}
		if(!((MobilePhone)(mobileNode.obj)).status()){
			throw new ExceptionOccurred("Error - Mobile phone with identifier "+mobileNumber+" is currently switched off");
		}
		if(this.root.nextLevel == null){
			return this.root;
		}
		return this.root.list.findPhone(mobileNumber);
	}

	public Exchange lowestRouter(Exchange a, Exchange b)throws ExceptionOccurred{
		Exchange first = this.root.findExchange(a.uid());
		Exchange second = this.root.findExchange(b.uid());
		if(first == null){
			throw new ExceptionOccurred("Error- Exchange-"+a.uid()+" does not exist.");
		}
		if(second == null){
			throw new ExceptionOccurred("Error- Exchange-"+b.uid()+" does not exist.");
		}
		if(first.nextLevel != null){
			throw new ExceptionOccurred("Error- Exchange-"+a.uid()+" is not a level 0 Exchange.");
		}
		if(second.nextLevel != null){
			throw new ExceptionOccurred("Error- Exchange-"+b.uid()+" is not a level 0 Exchange.");
		}
		if(first == second){
			return first;
		}

		Exchange parent = first.parent();
		while(parent.parent() != null){
			Exchange found = parent.findExchange(b.uid());
			if(found == null){
				parent = parent.parent();
			}
			else{
				return parent;
			}
		}
		if(parent.findExchange(b.uid()) == null){
			throw new ExceptionOccurred("Error- No common Exchange Occurs.");
		}
		else{
			return parent;
		}
	}

	public ExchangeList routeCall(MobilePhone a, MobilePhone b)throws ExceptionOccurred{
		Exchange first = findPhone(a);
		Exchange second = findPhone(b);
		if(first == null){
			throw new ExceptionOccurred("Error- Mobile Phone-"+a.number()+" not found.");
		}
		if(second == null){
			throw new ExceptionOccurred("Error- Mobile Phone-"+b.number()+" not found.");
		}
		Exchange common = lowestRouter(first, second);
		if(common == null){
			throw new ExceptionOccurred("Error- Mobile Number-"+a.number()+" & "+b.number()+" Cannot be connected.");
		}

		ExchangeList list1 = new ExchangeList(first.uid());
		list1.head.upLevel = null;
		while(first != common){
			first = first.parent();
			list1.addExchange(first.uid());
		}

		ExchangeList list2 = new ExchangeList(second.uid());
		list2.head.upLevel = null;
		while(second != common){
			second = second.parent();
			list2.addExchange(second.uid());
		}

		Exchange adder = list2.tail;
		adder = adder.upLevel;
		
		while(adder != null){
			list1.addExchange(adder.uid());
			adder = adder.upLevel;
		}

		return list1;
	}

	public boolean containsNode(Exchange a){
		int uid = a.uid();
		Exchange node = this.root.findExchange(uid);
		if(node == null){
			return false;
		}
		return true;
	}

	public String performAction(String actionMessage) {
		Scanner sc = new Scanner(actionMessage);
		String query = sc.next();
		try{
			if(query.equals("addExchange")){
				int parent = sc.nextInt();
				int child = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				Exchange parentExchangeNode = this.root.findExchange(parent);
				if(this.root.findExchange(child)!=null){
					throw new ExceptionOccurred("Error- Exchange-"+child+" already exists in the tree.");
				}
				if(parentExchangeNode == null){
					throw new ExceptionOccurred("Error- No such parent node='"+parent+"' found.");
				}
				else{
					if(parentExchangeNode.list == null){
						parentExchangeNode.list = new ExchangeList(child);
						parentExchangeNode.list.head.upLevel = parentExchangeNode;
						parentExchangeNode.nextLevel = parentExchangeNode.list.head;
					}
					else{
						parentExchangeNode.list.addExchange(child);
					}
				}
				return actionMessage+": Done";
			}

			else if(query.equals("queryNthChild")){
				int parent = sc.nextInt();
				int child = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				Exchange parentExchangeNode = this.root.findExchange(parent);
				if(parentExchangeNode == null){
					throw new ExceptionOccurred("Error- No such parent node='"+parent+"' found.");
				}
				else{
					Exchange ithChild = parentExchangeNode.child(child);
					if(ithChild!=null){
						return actionMessage+": "+ithChild.uid();
					}
				}
			}

			else if(query.equals("switchOnMobile")){
				int mobileNumber = sc.nextInt();
				int parent = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				MobilePhone a = new MobilePhone(mobileNumber);
				Exchange b = new Exchange(parent);
				switchOn(a,b);
				return actionMessage+": Done";
			}

			else if(query.equals("queryMobilePhoneSet")){
				int node = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				Exchange exchangeNode = this.root.findExchange(node);
				if(exchangeNode == null){
					throw new ExceptionOccurred("Error- No such parent node='"+node+"' found.");
				}
				else{
					MobilePhoneSet set = exchangeNode.residentSet();
					if(set==null){
						set = new MobilePhoneSet(exchangeNode);
						exchangeNode.updateResidentSet(set);
					}
					return actionMessage+": "+exchangeNode.residentSet().toString();
				}
			} 

			else if(query.equals("switchOffMobile")){
				int number = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				MobilePhone a = new MobilePhone(number);
				switchOff(a);
				return actionMessage+": Done";
			}

			else if(query.equals("movePhone")){
				int mobileNumber = sc.nextInt();
				int baseStation = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				movePhone(new MobilePhone(mobileNumber), new Exchange(baseStation));
				return actionMessage+": Done";
			}

			else if(query.equals("findPhone")){
				int number = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				Exchange baseStation = findPhone(new MobilePhone(number));
				if(baseStation == null){
					throw new ExceptionOccurred("Error- Couldn't locate the base station of this mobile number-"+number);
				}
				return "queryF"+actionMessage.substring(1)+": "+baseStation.uid();
			}

			else if(query.equals("lowestRouter")){
				int first = sc.nextInt();
				int second = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				Exchange common = lowestRouter(new Exchange(first), new Exchange(second));
				return "queryL"+actionMessage.substring(1)+": "+common.uid();
			}

			else if(query.equals("findCallPath")){
				int firstNumber = sc.nextInt();
				int secondNumber = sc.nextInt();
				if(sc.hasNext()){
					throw new ExceptionOccurred("Error- More number of action arguments. Please give action in correct format.");
				}
				ExchangeList route = routeCall(new MobilePhone(firstNumber), new MobilePhone(secondNumber));
				Exchange print = route.head;
				String s="";
				
				while(print!=null){
					s+=", "+print.uid();
					print = print.sameLevel;
				}

				if(s.length() == 0)
					throw new ExceptionOccurred("Error- No Route Found For Phone Numbers-"+firstNumber+" & "+secondNumber);

				return "queryF"+actionMessage.substring(1)+": "+s.substring(2);
			}

			else{
				throw new ExceptionOccurred("Error- Incorrect Input Format or Query. No such Query found.");
			}
		}catch(ExceptionOccurred e){
			if(query.equals("findPhone") || query.equals("lowestRouter") || query.equals("findCallPath")){
				char c = actionMessage.charAt(0);
				c = (char)(c-32);
				actionMessage = "query"+c+actionMessage.substring(1);
			}
			return actionMessage+": "+e.getMessage();
		}
		finally{
			sc.close();
		}
		return "";
	}
}