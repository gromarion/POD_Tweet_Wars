package ar.edu.itba.pod.mmxivii.alumno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list_a = new ArrayList<String>();
		list_a.add("1");
		list_a.add("2");
		map.put("a", list_a);
		map.get("a").add("3");
		for (String string : map.keySet()) {
			for (String string2 : map.get(string)) {
				System.out.println(string2);
			}
		}
	}
}
