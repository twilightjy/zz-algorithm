package class30Morris遍历;
/*
Morris遍历 时间复杂度O(N)的同时，空间复杂度优化到1:
假设来到当前节点cur，开始时cur来到头节点位置
1、如果cur没有左孩子，cur向右移动 cur = cur.right
2、如果cur有左孩子，找到左子树上最右的节点mostRight
	a.如果mostRight的右指针指向空，让其指向cur，然后cur向左移动cur = cur.left
	b.如果mostRight的右指针指向cur，让其指向null,然后cur向右移动 cur = cur.right
3、cur为空时遍历停止

一个节点如果有左子树，则会被访问两次，且两次之间会把其左子树访问完

cur的左子树的最右节点是否指向cur，就能判断是第一次还是第二次访问cur，而这个作用若是普通的递归遍历，是递归过程中栈保存的而信息所提供的。这里，人为去模拟这个作用。

morris序应该不能实现后序遍历
 */
public class Code01_MorrisTraversal {

	public static class Node {
		public int value;
		Node left;
		Node right;

		public Node(int data) {
			this.value = data;
		}
	}
//额外所需堆栈空间由树的高度决定
	public static void process(Node root) {
		if (root == null) {
			return;
		}
		// 1
		process(root.left);
		// 2
		process(root.right);
		// 3
	}

//----------------------------------------
	//"翻译"morris遍历的规则:
	public static void morris(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		//cur不为空时就继续循环
		while (cur != null) {
			mostRight = cur.left;
			//若没有左孩子则直接跳过，cur右移
			if (mostRight != null) {
				//若有左孩子，找到左子树的最右节点
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				//若最右节点指向空,使其指向cur，cur左移
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				//若最右节点指向cur，使其指向空，cur右移
				} else {
					mostRight.right = null;
				}
			}
			cur = cur.right;
		}
	}
	//先序：第一次到达时打印
	public static void morrisPre(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					//第一次访问cur，打印
					System.out.print(cur.value + " ");
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			} else {
				//左孩子如果为空，则不会再回来，只会访问一次，直接打印
				System.out.print(cur.value + " ");
			}
			cur = cur.right;
		}
		System.out.println();
	}
	//中序
	public static void morrisIn(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			//在要右移之前打印。要么是只访问一次，第一次就打印，要么是第二次时才才打印，这时左子树全部访问过了，要访问右边了
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		System.out.println();
	}
    //后序遍历,由于morris序本身是无法从右孩子回到父节点的，所以需要借助于链表翻转才能从右孩子回到父节点，不能用栈不然使用了额外的空间就失去了morris原本的意义
	public static void morrisPos(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
					//第二次来到时，逆序打印左子树的右边界
					printEdge(cur.left);
				}
			}
			cur = cur.right;
		}
		printEdge(head);
		System.out.println();
	}
	//打印左子树的右边界
	public static void printEdge(Node head) {
		Node tail = reverseEdge(head);
		Node cur = tail;
		while (cur != null) {
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		reverseEdge(tail);
	}
	//链表反转
	public static Node reverseEdge(Node from) {
		Node pre = null;
		Node next = null;
		while (from != null) {
			next = from.right;
			from.right = pre;
			pre = from;
			from = next;
		}
		return pre;
	}

	// for test -- print tree
	public static void printTree(Node head) {
		System.out.println("Binary Tree:");
		printInOrder(head, 0, "H", 17);
		System.out.println();
	}

	public static void printInOrder(Node head, int height, String to, int len) {
		if (head == null) {
			return;
		}
		printInOrder(head.right, height + 1, "v", len);
		String val = to + head.value + to;
		int lenM = val.length();
		int lenL = (len - lenM) / 2;
		int lenR = len - lenM - lenL;
		val = getSpace(lenL) + val + getSpace(lenR);
		System.out.println(getSpace(height * len) + val);
		printInOrder(head.left, height + 1, "^", len);
	}

	public static String getSpace(int num) {
		String space = " ";
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			buf.append(space);
		}
		return buf.toString();
	}
	//利用morris遍历，判断一棵二叉树是否是搜索二叉树
	public static boolean isBST(Node head) {
		if (head == null) {
			return true;
		}
		Node cur = head;
		Node mostRight = null;
		Integer pre = null;
		boolean ans = true;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			if (pre != null && pre >= cur.value) {
				ans = false;//这里不直接返回false的原因是，为了还原原来的二叉树，因为morris过程中加了新的指针嘛
			}
			pre = cur.value;
			cur = cur.right;
		}
		return ans;
	}

	public static void main(String[] args) {
		Node head = new Node(4);
		head.left = new Node(2);
		head.right = new Node(6);
		head.left.left = new Node(1);
		head.left.right = new Node(3);
		head.right.left = new Node(5);
		head.right.right = new Node(7);
		printTree(head);
		morrisIn(head);
		morrisPre(head);
		morrisPos(head);
		printTree(head);

	}

}
