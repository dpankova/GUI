//Class for Holding Data
package service;

import java.util.*;

public class DataSet
{
  private int N;
  public String name = null;
  public Boolean graphed = false;
  public Boolean histgraphed = false; 
  public int[] isamp;
  public int[] adc;
  public int[] time; 
  public int[] tot; 
  public int[] eoe;
  
  public DataSet(int N) 
    {  
      this.N = N;
      isamp = new int[N];
      adc = new int[N];
      time = new int[N];
      tot = new int[N];
      eoe = new int[N];
    }
  public void UpdateSize( int New)
    { 
      this.N = New;
      isamp = new int[New];
      adc = new int[New];
      time = new int[New];
      tot = new int[New];
      eoe = new int[New];    
    }

  public void Zero(){
    Arrays.fill(isamp,0);
    Arrays.fill(adc,0);
    Arrays.fill(time,0);
    Arrays.fill(tot,0);
    Arrays.fill(eoe,0);

  }
  public int getLength(){
    return N;	
  }	
}
