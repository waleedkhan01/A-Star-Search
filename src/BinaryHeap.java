import java.util.Scanner;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class BinaryHeap {
	private static final int child = 2;
	private int heap_size;
	private int [] heap;
	
	public BinaryHeap() {
		this.heap_size = 0;
		Arrays.fill(heap, -1);
	}
	
	public void heapify_up(int child) {
		int temp = heap[child];
		while(child > 0 && temp < heap[get_parent_index(child)]) {
			heap[child] = heap[get_parent_index(child)];
			child = get_parent_index(child);
		}
		heap[child] = temp;
	}
	
	public void heapify_down(int child) {
		int i;
		int temp = heap[child];
		
		while(get_kthChild_index(child, 1) < heap_size) {
			i = minimum_child(child);
			if(heap[i] < temp) {
				heap[child] = heap[i];
			}
			else {
				break;
			}
			child = i;
		}
		heap[child] = temp;
	}
	
	private int minimum_child(int i) {
		int best = get_kthChild_index(i, 1);
		int k = 2;
		int pos = get_kthChild_index(i, k);
		while((k <= child) && (pos < heap_size)) {
			if(heap[pos] < heap[best]) {
				best = pos;
			}
			pos = get_kthChild_index(i, k++);
		}
		return best;
	}
	
	public int delete(int i) {
		if(is_empty()) {
			throw new NoSuchElementException("Underflow");
		}
		int key = heap[i];
		heap[i] = heap[heap_size - 1];
		heap_size--;
		heapify_down(i);
		return key;
	}
	
	public boolean is_empty() {
		if(heap_size ==0) {
			return true;
		}
		return false;
	}
	
	public boolean is_full() {
		if(heap_size == heap.length) {
			return true;
		}
		return false;
	}
	
	public void clear_heap() {
		heap_size = 0;
	}
	
	public int get_parent_index(int i) {
		int res = (i-1)/child;
		return res;
	}
	
	public int get_kthChild_index(int i, int k) {
		int res = child*i+k;
		return res;
	}
	
	public void insert(int x) {
		if(is_full()) {
			throw new NoSuchElementException("Overflow");
		}
		
		heap[heap_size++] = x;
		heapify_up(heap_size - 1);	
	}
	
	public int find_least() {
		if(is_empty()) {
			throw new NoSuchElementException("Underflow");
		}
		return heap[0];
	}
}
