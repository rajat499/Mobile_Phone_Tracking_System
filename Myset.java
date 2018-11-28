public  class Myset{
	Node head = null;
	Node tail = null;
	public boolean isEmpty(){
		if(head==null)
			return true;
		return false;
	}

	public boolean isMember(Object o)throws ExceptionOccurred{
		//try{
			Node member = head;
			if(member == null){
				throw new ExceptionOccurred("set Empty !!");
			}
			while(member!=null){
				if(member.obj == o)
					return true;
				 member = member.right;
			}
			return false;
		//}catch(ExceptionOccurred e){
		//	System.out.println(e.getMessage());
		//	return false;
		//}
	}

	public void insert(Object o)throws ExceptionOccurred{
		/*if(!isEmpty()){
			Node add = head;
			boolean countered = false;
			while(add.right!=null){
				if(add.obj == o){
					countered = true;
					throw new ExceptionOccurred("Already Exists");
					break;
				}
				add = add.right;
			}

			if(add.obj == o && !countered){
					countered = true;
					throw new ExceptionOccurred("Already Exists");
					return;
			}

			if(add.right == null && !countered){
				Node newMember = new Node(o);
				newMember.left = add;
				add.right = newMember;
			}
		}
		else{
			Node newMember = new Node();
			newMember.obj = o;
			newMember.right = null;
			head = newMember;
		}*/
		//try{
			if(!isEmpty()){
					if(isMember(o)){
					throw new ExceptionOccurred("Error- Member Already Exists");
				}
				else{
					Node newMember = new Node(o);
					newMember.right = null;
					newMember.left = tail;
					tail.right = newMember;
					tail = newMember;
				}
			}
			else{
				Node newMember = new Node(o);
				newMember.right = null;
				newMember.left = null;
				head = newMember;
				tail = head;
			}
		//}catch(ExceptionOccurred e){
		//	System.out.println(e.getMessage());
		//}
	}

	public void delete(Object o)throws ExceptionOccurred{
		//try{	
			if(!isEmpty()){
				Node delete = head;
				if(delete.obj == o){
					head = delete.right;
					head.left = null;
				}
				else{
					while(delete.right!=null){
							if(delete.obj == o){
							delete.left.right = delete.right;
							delete.right.left = delete.left;
							delete.left = null;
							delete.right = null;
							return;
						}
						delete = delete.right;
					}
					if(delete.right == null){
						if(delete.obj == o){
							delete.left.right = null;
							delete.left = null;
						}
						else{
							System.out.println("Error- Delete object not in the set");
						}
					}
				}
			}
			else{
				throw new ExceptionOccurred("Error- set is Empty");
			}
		//}catch(ExceptionOccurred e){
		//	System.out.println(e.getMessage());
		//}
	}
	public Myset Union(Myset b)throws ExceptionOccurred{
		Myset unionSet = new Myset();
		Node head = b.head;
		while(head!=null){
			Object o = head.obj;
			unionSet.insert(o);
			head = head.right;
		}
		head = this.head;
		while(head!=null){
			Object o = head.obj;
			if(!unionSet.isMember(o)){
				unionSet.insert(o);
			}
			head = head.right;
		}
		return unionSet;
	}
	public Myset intersection(Myset b)throws ExceptionOccurred{
		Myset intersectionSet = new Myset();
		Node secondHead = b.head;
		while(secondHead != null){
			Object o = secondHead.obj;
			if(this.isMember(o))
			intersectionSet.insert(o);
			secondHead = secondHead.right;
		}
		return intersectionSet;
	}
}