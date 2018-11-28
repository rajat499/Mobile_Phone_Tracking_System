public class ExchangeList{
	
	Exchange head = null;
	Exchange tail = null;
	
	public ExchangeList(int id){
		head = new Exchange(id);
		head.nextLevel = null;
		head.sameLevel = null;
		tail = head;
	}

	public void addExchange(int uid){
		Exchange newNode = new Exchange(uid);
		newNode.upLevel = tail;
		newNode.nextLevel = null;
		newNode.sameLevel = null;
		tail.sameLevel = newNode;
		tail = newNode;
	}

	public Exchange findExchange(int id){
		Exchange find = head;
		while(find!=null){
			if(find.uid()==id)
				return find;
			if(find.list!=null){
				Exchange childFind = find.list.findExchange(id);
				if(childFind==null)
					find = find.sameLevel;
				else
					return childFind;
			}
			else{
				find = find.sameLevel;
			}
		}
		return null;
	}

	public void updateAllSets(MobilePhoneSet set)throws ExceptionOccurred{
		Exchange head = this.head;
		while(head!=null){
			head.updateResidentSet(head.residentSet().intersection(set));
			if(head.list!=null){
				head.list.updateAllSets(set);
			}
			head = head.sameLevel;
		}
	}

	public Exchange findPhone(int number)throws ExceptionOccurred{
		Exchange head = this.head;
		while(head != null){
			if(head.residentSet().isMember(number)){
				if(head.nextLevel == null){
					return head;
				}
				else{
					return head.list.findPhone(number);
				}
			}
			else{
				head = head.sameLevel;
			}
		}
		return null;
	}

	public void switchOff(int number)throws ExceptionOccurred{
		Exchange switchOffExchange = head;
		while(switchOffExchange!=null){
			MobilePhoneSet set = switchOffExchange.residentSet();
			if(set==null){
				switchOffExchange = switchOffExchange.sameLevel;
			}
			else{
				Node child = set.search(number);
				if(child==null){
					switchOffExchange = switchOffExchange.sameLevel;
				}
				else{
					((MobilePhone)(child.obj)).switchOff();
					if(switchOffExchange.list!=null){
						switchOffExchange.list.switchOff(number);
					}
					return;
				}
			}
		}
		return;
	}
}