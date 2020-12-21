package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PrimListModel implements ListModel<Integer> {
	
	private List<ListDataListener> listeners = new ArrayList<>();
	private List<Integer> data = new ArrayList<>();
	private int current = 2;
	{
		data.add(1);
	}
	
	public void next() {
		int next = 0;
		for (boolean cont = true; cont; current++) {
			cont = !Prime.isPrime(current);
			if (!cont)
				next = current;
		}
		data.add(next);
		notifyListeners();
	}
	
	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
	private void notifyListeners() {
		int lastIndex = data.size() - 1;
		ListDataEvent evt = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, lastIndex, lastIndex);
		for (ListDataListener l : listeners) {
			l.intervalAdded(evt);
		}
	}
	
}
