package All;

import java.util.*;

/**
 * Suggests similar strings to searchedString
 */
public class Autocompleter {
    private String currentRoad;
    private ArrayList<String> currentRoads;

    public Autocompleter(){
        currentRoad = "";
        currentRoads = new ArrayList<>();
    }

    /**
     * Returns a list of suggestions based on the searchString
     * @param streetMap A map from a streetname without housenumber to streetnames with housenumbers
     * @param searchString A string used to filter strings from the suggestion list/Map
     * @return a list of strings (suggestions)
     */

    public ArrayList<String> match(HashMap<String, ArrayList<String>> streetMap, String searchString){
        Set<String> streetSet = new HashSet<>();
        ArrayList<String> streetList = new ArrayList<>();
        for (String s: streetMap.keySet()) {
            if (s.toLowerCase().startsWith(searchString.toLowerCase().trim())) {
                streetSet.add(s);
                if (streetSet.size() >= 100) {
                    currentRoad = "";
                    currentRoads.clear();
                    streetList.addAll(streetSet);
                    sort(streetList);
                    return streetList;
                }
            }
        }
        if (streetSet.isEmpty()) {
            for (String s : streetMap.keySet()) {
                try {
                    if (s.toLowerCase().startsWith(searchString.toLowerCase().split(" ")[0])) {
                        currentRoads.add(s);
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                }
            }
            Collections.sort(currentRoads);
        }

        ArrayList<String> streetAddressesList = new ArrayList<>();
        if (streetSet.size() == 1){
            streetList.addAll(streetSet);
            currentRoad = streetList.get(0);
            streetAddressesList = streetMap.get(streetList.get(0));
            sort(streetAddressesList);
            return streetAddressesList;
        }
        if (streetSet.size() == 0){
            if (!currentRoad.isEmpty()) {
                for (String s : streetMap.get(currentRoad)) {
                    if (s.toLowerCase().contains(searchString.toLowerCase())) {
                        streetAddressesList.add(s);
                    }
                }
                sort(streetAddressesList);
                return streetAddressesList;
            }
            if (!currentRoads.isEmpty()){
                for (String road: currentRoads) {
                    for (String s : streetMap.get(road)) {
                        if (s.toLowerCase().contains(searchString.toLowerCase()) && !streetAddressesList.contains(s)) {
                            streetAddressesList.add(s);
                        }
                    }
                }
                sort(streetAddressesList);
                return streetAddressesList;
            }
        }

        String first = "";
        int i = 0;
        for (String s: streetSet){
            if (i==0){
                first = s.split(",")[0];
            }
            if (s.split(",")[0].equals(first)){
                i+=1;
            }else{
                break;
            }
        }

        if (i == streetSet.size()){
            for (String s: streetSet){
                if (s.toLowerCase().contains(searchString.toLowerCase())) {
                    streetAddressesList.addAll(streetMap.get(s));
                }
                currentRoads.add(s);
            }
            sort(streetAddressesList);
            return streetAddressesList;
        }

        for (String road : streetMap.keySet()) {
            if (streetMap.get(road).contains(searchString.toLowerCase())) {
                streetSet.add(road);
            }
            else if (currentRoads.contains(road)){
                streetSet.add(road);
            }
        }
        if (!streetSet.isEmpty()){
            currentRoad = "";
            currentRoads.clear();
        }
        streetList.addAll(streetSet);
        sort(streetList);
        return streetList;
        }

    /**
     * Gets addresses on searched street
     * @param streetMap
     * @param searchString
     * @return a list of addresses on that street
     */
    public ArrayList<String> streetMatch(HashMap<String, ArrayList<String>> streetMap, String searchString){
        try {
            ArrayList<String> streetList = new ArrayList<>(streetMap.get(searchString));
            sort(streetList);
            return streetList;
        } catch (NullPointerException e) {
            return null;
        }
    }
        // Source: https://stackoverflow.com/questions/13973503/sorting-strings-that-contains-number-in-java, date: 2019-04-30
    private void sort(ArrayList<String> strings){
        try{
            Collections.sort(strings, new Comparator<>() {
                public int compare(String o1, String o2) {
                    return extractInt(o1) - extractInt(o2);
                }

                private int extractInt(String s) {
                    String num = s.replaceAll("\\D", "");
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            });}
        catch (NumberFormatException e){} //if this error occurs just ignore, be aware it might make the list weird
        }


}
