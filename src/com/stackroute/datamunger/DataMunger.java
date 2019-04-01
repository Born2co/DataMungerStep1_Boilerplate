package com.stackroute.datamunger;

/*There are total 5 DataMungertest files:
* 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
* a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
* 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
* 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
* a)getFields() b) getConditionsPartQuery() c) getConditions()
* 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
* 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
* a)getLogicalOperators() b) getOrderByFields()
* 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
* 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
* a)getGroupByFields()  b) getAggregateFunctions()
* 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
* 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
* the test cases together.
*/

public class DataMunger {

       /*
       * This method will split the query string based on space into an array of
       * words and display it on console
       */

       public String[] getSplitStrings(String queryString) {
              

              String result = queryString.toLowerCase();
              return result.split(" ");
       }

       /*
       * Extract the name of the file from the query. File name can be found after
       * a space after "from" clause. Note: ----- CSV file can contain a field
       * that contains from as a part of the column name. For eg:
       * from_date,from_hrs etc.
       * 
        * Please consider this while extracting the file name in this method.
       */

       public String getFileName(String queryString) {
              Boolean flag = false;
              String result = null;
              String[] splitInput = queryString.split(" ");
              for (String str : splitInput) {
                     if (flag) {
                           flag = false;
                           result = str;
                     }
                     if (str.equalsIgnoreCase("from")) {
                           flag = true;
                     }

              }
              return result;
       }

       /*
       * This method is used to extract the baseQuery from the query string.
       * BaseQuery contains from the beginning of the query till the where clause
       * 
        * Note: ------- 1. The query might not contain where clause but contain
       * order by or group by clause 2. The query might not contain where, order
       * by or group by clause 3. The query might not contain where, but can
       * contain both group by and order by clause
       */

       public String getBaseQuery(String queryString) {
              String[] splitInput = queryString.split(" ");
              StringBuilder output = new StringBuilder();
              for (String str : splitInput) {
                     if (str.equalsIgnoreCase("where") || str.equalsIgnoreCase("group")) {
                           break;
                     }
                     output = output.append(str + " ");
              }
              String result = output.substring(0, output.length() - 1);

              return result;
       }

       /*
       * This method will extract the fields to be selected from the query string.
       * The query string can have multiple fields separated by comma. The
       * extracted fields will be stored in a String array which is to be printed
       * in console as well as to be returned by the method
       * 
        * Note: 1. The field name or value in the condition can contain keywords as
       * a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
       * name can contain '*'
       * 
        */

       public String[] getFields(String queryString) {
              String output = queryString.substring(queryString.indexOf("select"), queryString.indexOf(" from"))
                           .replaceFirst("select ", "");
              String[] result = output.split(",");
              return result;
       }

       /*
       * This method is used to extract the conditions part from the query string.
       * The conditions part contains starting from where keyword till the next
       * keyword, which is either group by or order by clause. In case of absence
       * of both group by and order by clause, it will contain till the end of the
       * query string. Note: 1. The field name or value in the condition can
       * contain keywords as a substring. For eg: from_city,job_order_no,group_no
       * etc. 2. The query might not contain where clause at all.
       */

       public String getConditionsPartQuery(String queryString) {
              String output = null;
              if(!queryString.toLowerCase().contains("where")){
                     if(!queryString.toLowerCase().contains("1")){
                           return null;
                     }else{
                           output = "1";
                           return output;
                     }
              }else{
                     if (queryString.toLowerCase().contains("order")) {
                           output = queryString.toLowerCase().substring(queryString.indexOf("where"), queryString.indexOf(" order by"))
                                         .replaceFirst("where ", "");
                     } else if (queryString.toLowerCase().contains("group")) {
                           output = queryString.toLowerCase().substring(queryString.indexOf("where"), queryString.indexOf(" group by"))
                                         .replaceFirst("where ", "");
                     }else {
                           output = queryString.toLowerCase().substring(queryString.indexOf("where")).replaceFirst("where ", "");
                     }
                     return output;
              }
              
       }

       /*
       * This method will extract condition(s) from the query string. The query
       * can contain one or multiple conditions. In case of multiple conditions,
       * the conditions will be separated by AND/OR keywords. for eg: Input:
       * select city,winner,player_match from ipl.csv where season > 2014 and city
       * ='Bangalore'
       * 
        * This method will return a string array
       * ["season > 2014","city ='bangalore'"] and print the array
       * 
        * Note: ----- 1. The field name or value in the condition can contain
       * keywords as a substring. For eg: from_city,job_order_no,group_no etc. 2.
       * The query might not contain where clause at all.
       */

       public String[] getConditions(String queryString) {
              
              String mod = "";
              String[] output = null;
              if (queryString.contains("where")) {
                     String[] splitInput = queryString.toLowerCase().split("where ", 0);
                     int length = splitInput.length;
                     String map = splitInput[length - 1];
                     if (map.contains("order")) {
                           mod = map.substring(0, map.indexOf(" order"));
                     } else if (map.contains("group")) {
                           mod = map.substring(0, map.indexOf(" group"));
                     } else {
                           mod = map;
                     }
                     output = mod.split(" and | or ");
              } else {
              }
              return output;
       }

       /*
       * This method will extract logical operators(AND/OR) from the query string.
       * The extracted logical operators will be stored in a String array which
       * will be returned by the method and the same will be printed Note: 1.
       * AND/OR keyword will exist in the query only if where conditions exists
       * and it contains multiple conditions. 2. AND/OR can exist as a substring
       * in the conditions as well. For eg: name='Alexander',color='Red' etc.
       * Please consider these as well when extracting the logical operators.
       * 
        */

       public String[] getLogicalOperators(String queryString) {
              
              
               int countForAnd = 0;
               int countForOr = 0;
               String[] res = null;
               if(queryString.toLowerCase().contains(" where ")) {
               String[] splitInput = queryString.toLowerCase().split(" ");
               for (String str : splitInput) {
                   if (str.equals("and")) {
                       countForAnd++;
                   }
                   if (str.equals("or")) {
                       countForOr++;
                   }
               }
               splitInput = null;

               if(countForAnd !=0 || countForOr != 0) {
                 res = new String[countForAnd + countForOr];
               }
               int i=0;         
               splitInput = queryString.toLowerCase().split(" ");
                   for (String str : splitInput) {
                       if (str.equals("and")) {
                           res[i++] = "and";
                       }
                       if (str.equals("or")) {
                           res[i++] = "or";
                       }
                   }
                   
               }
               return res;
       }

       /*
       * This method extracts the order by fields from the query string. Note: 1.
       * The query string can contain more than one order by fields. 2. The query
       * string might not contain order by clause at all. 3. The field
       * names,condition values might contain "order" as a substring. For
       * eg:order_number,job_order Consider this while extracting the order by
       * fields
       */

       public String[] getOrderByFields(String queryString) {
              
          String[] output=null;
       int i=0;
       if(queryString.toLowerCase().contains(" order by ")){
           String res = queryString.toLowerCase().split(" order by ")[1];
           output = new String[1];
           output[0] = res;
       }  
       return output;
}

       
       /*
       * This method extracts the group by fields from the query string. Note: 1.
       * The query string can contain more than one group by fields. 2. The query
       * string might not contain group by clause at all. 3. The field
       * names,condition values might contain "group" as a substring. For eg:
       * newsgroup_name
       * 
        * Consider this while extracting the group by fields
       */

       public String[] getGroupByFields(String queryString) {
                String[] output=null;
          int i=0;
          if(queryString.toLowerCase().contains(" group by ")){
              String res = queryString.toLowerCase().split(" group by ")[1];
              output = new String[1];
              output[0] = res;
          }return output;
              
       }
       /*
       * This method extracts the aggregate functions from the query string. Note:
       * 1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
       * followed by "(" 2. The field names might
       * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
       * account_number,consumed_qty,nominee_name
       * 
        * Consider this while extracting the aggregate functions
       */

       public String[] getAggregateFunctions(String queryString) {
              
              String[] filteredOutput = null;
              String output = null;
              if (!queryString.contains("*")) {
        output = queryString.substring(queryString.indexOf("select"),queryString.indexOf(" from")).replaceFirst("select ", "");
        String[] result = output.split(",");
        int i=0;
        int count=0;
        for(String str : result){
            if(str.matches("\\w+\\(\\w+\\)")){
                count++;
            }
        }
        filteredOutput = new String[count];
        for(String str: result){
            if(str.matches("\\w+\\(\\w+\\)")){
                filteredOutput[i++]= str;
            }
        }
    }
    return filteredOutput;
    }

}
