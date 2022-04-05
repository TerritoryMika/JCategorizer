import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "serial"})
public class Categorizer<C, V> implements Iterable<V> {
	private Map<C, List<V>> categoriesMap = new HashMap<C, List<V>>();
	private Map<V, C[]> valuesMap = new HashMap<V, C[]>();
	
	public void add(V value, C... categories) {
		
		if(containValue(value)) {
			replace(value, categories);
			return;
		}
		
		for(C category : categories) {
			if(categoriesMap.containsKey(category)) {
				categoriesMap.get(category).add(value);
			}else {
				categoriesMap.put(category, new ArrayList<V>() {{ add(value); }});
			}
		}
		valuesMap.put(value, categories);
		
	}
	
	public void addIfAbsent(V value, C... categories) {
		if(!containValue(value))
			add(value, categories);
	}
	
	public void replace(V value, C... categories) {
		remove(value);
		add(value, categories);
	}
	
	public List<V> lookUpOr(C... categories) {
		List<V> list = new LinkedList<V>();
		for(C category : categories)
			list.addAll(categoriesMap.get(category));
		return list;
	}
	
	public List<V> lookUpAnd(C... categories) {
		List<V> head = categoriesMap.get(categories[0]);
		return head.stream()
				.filter((t) -> { return isSubset(categories, valuesMap.get(t));})
				.collect(Collectors.toList());
	}
	
	public List<V> search(Predicate<V> condition) {
		return values().stream()
				.filter(condition)
				.collect(Collectors.toList());
	}
	
	public List<C> getCategories(V value) {
		return List.of(valuesMap.get(value));
	}
	
	public Set<V> values() {
		return valuesMap.keySet();
	}
	
	public Set<C> categories() {
		return categoriesMap.keySet();
	}
	
	public boolean containValue(V value) {
		return values().contains(value);
	}
	
	public boolean containCategory(C category) {
		return categories().contains(category);
	}
	
	public void remove(V value) {
		var categories =  valuesMap.remove(value);
		for(var category : categories)
			categoriesMap.get(category).remove(value);
	}

	public boolean isEmpty() { return size() == 0;}
	
	public int size() { return valuesMap.size();}
	
	@Override
	public Iterator<V> iterator() { return values().iterator();}

	public Object[] toArray() { return values().toArray();}

	public boolean containsAll(Collection<? extends V> values) {
		for(var value : values)
			if(containValue(value))
				return false;
		return true;
	}

	public void addAll(Collection<? extends V> values) {
		for(var value : values)
			add(value);
	}

	public void removeAll(Collection<? extends V> values) {
		for(var value : values)
			remove(value);
	}

	public void clear() {
		categoriesMap = new HashMap<C, List<V>>();
		valuesMap = new HashMap<V, C[]>();
	}
	

	private static final <T> boolean isSubset(final T[] a, final T[] b) {
		nextEle : 
		for(final T eleA : a) {
			for(final T eleB : b)
				if(eleA.equals(eleB))
					continue nextEle;
			return false;
		}
		return true;
	}
}
