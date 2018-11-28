public class Exchange{
	
	public int uid = 0;
	Exchange upLevel = null;
	Exchange nextLevel = null;
	Exchange sameLevel = null;
	ExchangeList list = null;
	private MobilePhoneSet residentSet = new MobilePhoneSet(this);
	String type = "Exchange";
	
	public Exchange(int number){
		uid = number;
	}

	public int uid(){
		return uid;
	}

	public void updateResidentSet(MobilePhoneSet m){
		residentSet = m;
	}

	public Exchange parent(){
		if(upLevel==null)	
			return null;
		if(upLevel.nextLevel==this)
			return upLevel;
		else
			return upLevel.parent();
	}

	public Exchange findExchange(int id){
		if(this.uid() == id){
			return this;
		}
		if(this.list==null){
			return null;
		}
		else{
			return this.list.findExchange(id);
		}
	}

	public int numChildren(){
		
		int count = 0;
		Exchange children = nextLevel;
		
		while(children != null){
			count++;
			children = children.sameLevel ;
		}

		return count;
	}

	public void switchOn(int number)throws ExceptionOccurred{
		Node mobileNode = this.residentSet().search(number);
		if(mobileNode!=null){
			((MobilePhone)(mobileNode.obj)).switchOn();
		}
		if(this.parent()==null){
			return;
		}
		this.parent().switchOn(number);
	}

	public void switchOff(int number)throws ExceptionOccurred{
		MobilePhoneSet set=this.residentSet();
		if(set != null){
			Node child = set.search(number);
			if(child!=null){
				((MobilePhone)(child.obj)).switchOff();
				if(this.list==null){
					return;
				}
				else{
					this.list.switchOff(number);
				}
			}
			else{
				return;
			}
		}
		else{
			return;
		}
	}

	public void delete(int number)throws ExceptionOccurred{
		this.residentSet.removeMobilePhone(number);
		if(this.list!=null){
			this.list.updateAllSets(this.residentSet);
		}
		else{
			return;
		}
	}

	public Exchange child(int i)throws ExceptionOccurred{
		//try{
			if(i<0){
				throw new ExceptionOccurred("Error- Negative Child doesn't exist.. !!");
			}

			Exchange ithChild = nextLevel;
			for(int n=0; n<i && ithChild != null; n++){
				ithChild = ithChild.sameLevel;
			}
			if(ithChild ==  null){
				throw new ExceptionOccurred("Error- Number of Children are less than "+(i+1)+", Hence no such child exist.");
			}
			return ithChild;
		//}catch(ExceptionOccurred e){
		//	System.out.println(e.getMessage());
		//	return null;
		//}
	}

	public boolean isRoot(){
		if(parent() == null && this.uid()==0)
			return true;
		else
			return false;
	}

	public RoutingMapTree subtree(int i)throws ExceptionOccurred{
		Exchange ithChild = child(i);
		if(ithChild!=null){
			RoutingMapTree subTree = new RoutingMapTree();
			subTree.root.uid = ithChild.uid();
			subTree.root.upLevel=ithChild.upLevel;
			subTree.root.nextLevel = ithChild.nextLevel;
			return subTree;
		}
		else{
			return null;
		}
	}

	public MobilePhoneSet residentSet()throws ExceptionOccurred{
		if(residentSet == null || nextLevel!=null){
			residentSet = new MobilePhoneSet(this);
		}
		Exchange merge = nextLevel;
		while(merge!=null){
			residentSet = merge.residentSet().union(residentSet);
			merge = merge.sameLevel;
		}
		return residentSet;
	}
}