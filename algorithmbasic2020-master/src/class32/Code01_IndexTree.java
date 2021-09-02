package class32;
/*
线段树是范围更新，indexTree是"单点更新" 复杂度都是logN 意义就是时间复杂度从N优化到logN
indexTree的优势：可以推到二维

tree数组的含义：代表特定范围的累加和
代表：1 12 3 1234 5 56 7 12345678 9 910 11 9101112
下标：1 2  3 4    5 6  7 8        9 10  11 12
小规律：2的幂次求的是全部

i位置管哪些位置的累加和：二进制形式 举例：
	01011000
	01010001到  (最后一个1变0，然后加1)
	01011000    (到自己)

借助上面这个特性,怎么求任意1~i位置的累加和：
举例1~33 0100001
拆成 0100001 和 010000 0000001 这两个位置的 （1变0）
这三个位置相加即可;如果是2的幂次，就只有本身

关键点：最右侧的1--求累加的时候剥去，更新某个index时加上
 */
public class Code01_IndexTree {

	// indexTree实现 (下标从1开始!)
	public static class IndexTree {
		//初始是0，如果要用的话，给定一个arr，要遍历一遍arr调用add方法，但是一定要注意下标！在这之后，做任意的更新和查询范围累加和都变成logN了！
		private int[] tree;
		private int N;

		// 0位置弃而不用！
		public IndexTree(int size) {
			N = size;
			tree = new int[N + 1];
		}

		// 1~index 累加和是多少？
		/*
		举例1~33 0100001
		拆成 0100001 和 010000 0000001 这两个位置的 （1变0）
		这三个位置相加即可;如果是2的幂次，就只有本身
		不断的"剥去最右侧的1"
		 */
		public int sum(int index) {
			int ret = 0;
			while (index > 0) {
				ret += tree[index];
				index -= index & -index;
			}
			return ret;
		}
		//add方法：在index位置加d之后，对累加和数组的影响
		// index & -index : 提取出index最右侧的1出来 复习位运算 index & (~index+1)
		// index :           0011001000
		// index & -index :  0000001000
		public void add(int index, int d) {
			while (index <= N) {
				//影响自己
				tree[index] += d;
				/*
				后面会影响哪些位置,举例：
				6位置加d，110
						 110+010 = 1000 8位置
						 1000+1000 = 10000 16位置
						 就是不断的加"最右侧的1"
				 */
				index += index & -index;
			}
		}
	}
	//暴力 N复杂度的解
	public static class Right {
		private int[] nums;
		private int N;

		public Right(int size) {
			N = size + 1;
			nums = new int[N + 1];
		}

		public int sum(int index) {
			int ret = 0;
			for (int i = 1; i <= index; i++) {
				ret += nums[i];
			}
			return ret;
		}

		public void add(int index, int d) {
			nums[index] += d;
		}

	}

	public static void main(String[] args) {
		int N = 100;
		int V = 100;
		int testTime = 2000000;
		IndexTree tree = new IndexTree(N);
		Right test = new Right(N);
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int index = (int) (Math.random() * N) + 1;
			if (Math.random() <= 0.5) {
				int add = (int) (Math.random() * V);
				tree.add(index, add);
				test.add(index, add);
			} else {
				if (tree.sum(index) != test.sum(index)) {
					System.out.println("Oops!");
				}
			}
		}
		System.out.println("test finish");
	}

}
