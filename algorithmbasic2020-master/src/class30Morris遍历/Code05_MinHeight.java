package class30Morris遍历;

public class Code05_MinHeight {

	public static class Node {
		public int val;
		public Node left;
		public Node right;

		public Node(int x) {
			val = x;
		}
	}
	//求二叉树的最小深度
	public static int minHeight1(Node head) {
		if (head == null) {
			return 0;
		}
		return p(head);
	}

	// 返回x为头的树，最小深度是多少
	public static int p(Node x) {
		if (x.left == null && x.right == null) {
			return 1;
		}
		// 左右子树起码有一个不为空
		int leftH = Integer.MAX_VALUE;
		if (x.left != null) {
			leftH = p(x.left);
		}
		int rightH = Integer.MAX_VALUE;
		if (x.right != null) {
			rightH = p(x.right);
		}
		return 1 + Math.min(leftH, rightH);
	}
//---------------------------------------------
	// 根据morris遍历改写，需要能手画过程才比较好懂哦。。。。
	public static int minHeight2(Node head) {
		if (head == null) {
			return 0;
		}
		Node cur = head;//当前节点
		Node mostRight = null;//左子树的最右节点
		int curLevel = 0;//当前深度
		int minHeight = Integer.MAX_VALUE;//最小深度
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				int rightBoardSize = 1;
				while (mostRight.right != null && mostRight.right != cur) {
					rightBoardSize++;//记录找到左子树的最右节点的路径上的节点个数
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					// 第一次到达cur，则左移，深度++
					curLevel++;
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					// 第二次到达
					//如果此时mostRight的左节点为空，说明其是一个叶子节点，更新最小深度
					if (mostRight.left == null) {
						minHeight = Math.min(minHeight, curLevel);
					}
					curLevel -= rightBoardSize;//根据mostRight的深度减去路径上的节点个数得到cur的深度
					mostRight.right = null;
				}
			} else {
				//左子树如果为空，只有一次到达才可能，直接深度++
				//然后右移，如果它原来是叶子节点，右移要么会回到某个根节点，要么结束(整个二叉树的最右节点)
				curLevel++;
			}
			cur = cur.right;
		}
		//最后单独处理判断整个二叉树的最右节点
		int finalRight = 1;
		cur = head;
		while (cur.right != null) {
			finalRight++;
			cur = cur.right;
		}
		//如果整个二叉树的最右节点是一个叶子节点，才更新深度。
		if (cur.left == null && cur.right == null) {
			minHeight = Math.min(minHeight, finalRight);
		}
		return minHeight;
	}

	// for test
	public static Node generateRandomBST(int maxLevel, int maxValue) {
		return generate(1, maxLevel, maxValue);
	}

	// for test
	public static Node generate(int level, int maxLevel, int maxValue) {
		if (level > maxLevel || Math.random() < 0.5) {
			return null;
		}
		Node head = new Node((int) (Math.random() * maxValue));
		head.left = generate(level + 1, maxLevel, maxValue);
		head.right = generate(level + 1, maxLevel, maxValue);
		return head;
	}

	public static void main(String[] args) {
		int treeLevel = 7;
		int nodeMaxValue = 5;
		int testTimes = 100000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(treeLevel, nodeMaxValue);
			int ans1 = minHeight1(head);
			int ans2 = minHeight2(head);
			if (ans1 != ans2) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish!");

	}

}
