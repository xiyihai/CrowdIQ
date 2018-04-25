package zlf0320.method;



public class LCSProblem   
{  
	public static void main(String[] args) {  
		LCSProblem obj = new LCSProblem();  
        String str1 = "1S: Survey";  
        String str2 = "1S: Survey3: Enhance Pest ID & Technology3:";  
        System.out.println("LongestCommonSubsequence: " + obj.getMaxLength(str1, str2));  
    }  
    public static double getMaxLength(String str1, String str2){  
        int m = str1.length();  
        int n = str2.length();  
        //use tempArray to restore the data  
        int tempArray[] = new int[n];  
        double maxLength = 0;  
        for(int i = 0; i < m; i++){  
            for(int j = n-1; j >= 1; j--){  
                if(str1.charAt(i) != str2.charAt(j))  
                    tempArray[j] = 0;  
                else{  
                    tempArray[j] = tempArray[j - 1] + 1;  
                    if(tempArray[j] > maxLength)  
                        maxLength = tempArray[j];  
                }  
            }  
            if(str1.charAt(i) != str2.charAt(0)){  
                tempArray[0] = 0;  
                if(tempArray[0] > maxLength)  
                    maxLength = tempArray[0];  
            }  
            /** 
             * output for test 
             ****************************************/  
            for(int k = 0; k < n; k++)  
                System.out.print(tempArray[k] + ", ");  
            System.out.println();  
            /****************************************/  
              
        }  
        return maxLength/Math.max(str1.length(),str2.length());  
    }  
}  