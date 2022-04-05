import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked"})
public class AutoCategorizer<C, V> extends Categorizer<C, V> {
	private Map<Predicate<V>, C> cases = new HashMap<Predicate<V>, C>();
	
	public void addCase(Predicate<V> predicate, C category) {
		cases.put(predicate, category);
	}
	
	@Override
	public void add(V value, C... categories) {
		var predicates = checkPredicates(value);
		for(var category : categories)
			predicates.add(category);
		super.add(value, (C[]) predicates.toArray());
	}
	
	private final List<C> checkPredicates(final V value) {
		var list = new ArrayList<C>();
		for(var predicate : cases.keySet())
			if(predicate.test(value))
				list.add(cases.get(predicate));
		return list;
	}
}
