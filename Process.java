import java.util.*;
import java.time.format.*;
import java.time.*;

// program for processing dictionary elements
class Process{
    public static void main(String[] args){
        ArrayList<HashMap<String,Integer>> cases = new ArrayList<>();
        HashMap<String,Integer> hm  = new HashMap<>(){{put("2020-01-01",6);put("2020-01-04",12);put("2020-01-05",14);put            ("2020-01-06",2);put("2020-01-07",4);}};
        cases.add(hm);
        hm = new HashMap<>(){{put("2020-01-01",4);put("2020-01-02",4);put("2020-01-03",6);put("2020-01-04",8);put("2020-01-05",2);put("2020-01-06",-6);put("2020-01-07",2);put("2020-01-08",-2);}};
        cases.add(hm);
        hm = new HashMap<>(){{put("2020-01-06",2);put("2020-01-05",14);}};
        cases.add(hm);
        hm = new HashMap<>(){{put("2020-01-01",6);put("2020-01-04",12);put("2020-01-07",4);}};
        cases.add(hm);
        cases.add(new HashMap<String,Integer>());
        hm = new HashMap<String,Integer>(){{put("Jan-01-2020",10);put("2020-01-04",12);}};
        cases.add(hm);
        hm = new HashMap<String,Integer>(){{put("2100-01-05",100);}};
        cases.add(hm);
        hm = new HashMap<String,Integer>(){{put("2020-05-10",10000000);}};
        cases.add(hm);
        try{
            for(HashMap<String,Integer> current : cases){
                  if(current.isEmpty()){
                      System.out.println("Dictionary is empty");
                      continue;
                  }
                  System.out.println("Input : " + current);
                  HashMap<String,Long> dict = find(current);
                  System.out.println(dict);
                  
            }
        }catch(NullPointerException noInput){
            System.out.println("Provide a HashMap having dates as key and integer numbers as value");
        }
    }
    
    // method takes dictionary as arguments and provide a dictionary with days as keys and sum of values for 
    // for that day as value and mean of previous and next day if not found
    static HashMap<String,Long> find(HashMap<String,Integer> in){
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        long[] sum = new long[8];
        LocalDate start = LocalDate.of(1970,1,1), end = LocalDate.of(2100,1,1);
        HashMap<String,Long> result = new HashMap<>();
        for(Map.Entry<String,Integer> cur : in.entrySet()){
            String k = cur.getKey();
            try{
                  Integer v = cur.getValue();
                  if(v>1000000 || v<-1000000){
                      System.out.println("All values should be between -10,00,000 and 10,00,000 inclusive");
                      return result;
                  }
                  LocalDate date = LocalDate.parse(k,form);
                  if(date.isBefore(start) || date.isAfter(end)){ // if date date is in specified range
                      System.out.println("All dates should be  between 1st January 1700 and 1st January 2100 inclusive");
                      return result;
                  }
                  int val = date.getDayOfWeek().getValue();
                  sum[val] += v;
         
            }catch(DateTimeException valid){// checking for validity of date format
                  System.out.println("All dates should be in yyyy-MM-dd format");
                  System.out.println("For example enter 2020-02-01 for 1st February 2020");
                  return result;
            } 
        }
        // Values for Monday and Sunday should be provided
        if(sum[1]==0 && sum[7]==0){
            System.out.println("Values for Monday and Sunday are not provided");
            return result;
        }
        if(sum[1]==0){
            System.out.println("Value for Monday is not provided");
            return result;
        }
        if(sum[7]==0){
            System.out.println("Value for Sunday is not provided");
            return result;
        }
        String[] days = {"","Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        int next = 1;    // day having a value
        for(int i = 1; i<8; i++){
            // sum for ith day of week is not found
            if(sum[i]==0){
                // next nearest day having +ve sum is not known     
                if(next<=i){
                    // searching for that day  
                    for(int j = i+1; j<8; j++){  
                        if(sum[j]>0){
                            next = j;
                            break;
                        }
                    }
                }
                sum[i] = sum[i-1]*(next-i) + sum[next];  // sum for ith day is mean of previous and next day values
                sum[i] /= (next-i+1); // with each value multiplied by its ratio and their sum divided by total distance 
                                                          
            }
            result.put(days[i],sum[i]);
        }
        return result;  
    }
}