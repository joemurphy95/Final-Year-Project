package fyp;

/*
 * Joseph Murphy, 2019
 */

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class GameOfLifeSimulator implements Runnable{
  Random rd = new Random();
  static int cores = Runtime.getRuntime().availableProcessors();
  static CyclicBarrier cb = new CyclicBarrier(cores);

  static int[][] cells, sucessor; //(nDim)x(nDim)
  static int nDim;
  static String fGen, border;
  int upperLimit, lowerLimit, thread;

  /*
   * Constructor
   * 	@param  int upperLimit        upper limit
   * 	@param  int lowerLimit        lower limit
   * 	@param  int thread            thread number
   */
  public GameOfLifeSimulator(int ulimit, int llimit, int thread){
    this.upperLimit=ulimit;
    this.lowerLimit=llimit;
    this.thread=thread;
  }

  public GameOfLifeSimulator(){}

  public void run(){
    caComputation();
    try{Thread.sleep(2); }catch(InterruptedException e){}
  }

  /*
   * Initializes the cellular automaton
   * 	@param int    d cellular automaton Dimension
   * 	@param String f type of the first generation
   * 	@param String b type of the border
   */
  public void inizialize(int d, String f, String b){
    nDim = d;
    fGen = f;
    border = b;
    cells = new int[nDim][nDim];
    sucessor = new int[nDim][nDim];
    firstgen();
  }

  /*
   * Initializes the first generation
   */
  public void firstgen(){
    switch(fGen){
      case "Random":
        for(int i=0; i<nDim; ++i){
          for(int j=0; j<nDim; ++j){
            cells[i][j]=rd.nextInt(2);
          }
        }
      break;

      case "Center":
        for(int i=nDim/2-20; i<nDim/2+20; ++i){
          for(int j=nDim/2-20; j<nDim/2+20; ++j){
            cells[i][j]=rd.nextInt(2);
          }
        }
      break;

      case "Glider":
        cells[0][4]=1;  cells[0][5]=1;  cells[1][4]=1;  cells[1][5]=1;
        cells[10][4]=1; cells[10][5]=1; cells[10][6]=1; cells[11][3]=1;
        cells[11][7]=1; cells[12][2]=1; cells[12][8]=1; cells[13][2]=1;
        cells[13][8]=1; cells[14][5]=1; cells[15][3]=1; cells[15][7]=1;
        cells[16][4]=1; cells[16][5]=1; cells[16][6]=1; cells[17][5]=1;
        cells[20][2]=1; cells[20][3]=1; cells[20][4]=1; cells[21][2]=1;
        cells[21][3]=1; cells[21][4]=1; cells[22][1]=1; cells[22][5]=1;
        cells[24][0]=1; cells[24][1]=1; cells[24][5]=1; cells[24][6]=1;
        cells[34][2]=1; cells[34][3]=1; cells[35][2]=1; cells[35][3]=1;
      break;
      default: break;
    }
  }

  /*
   * returns the cellular automaton
   */
  public int[][] get_cells(){
    return cells;
  }

  /*
   * Counts how many live cells the CA has
   */
  public int numLiveCells(){
      int cont=0;
      for(int i=0; i<nDim ; ++i){
          for(int j=0; j<nDim; ++j){
              cont+=cells[i][j];
          }
      }
      return cont;
  }

  /*
   * Computes the 'next generation'
   */
  public void nextGen(){
    for(int i=lowerLimit; i<upperLimit ; ++i){
      for(int j=0; j<nDim; ++j){
        if(cells[i][j]==1){
          if(livingNeighbours(i,j)<2||3<livingNeighbours(i,j)){
            sucessor[i][j]=0;
          }else{
            sucessor[i][j]=1;
          }
        }else{
          if(livingNeighbours(i,j)==3){
            sucessor[i][j]=1;
          }
        }
      }
    }
    try{
      cb.await();
    }catch(Exception e){}
  }

  /*
   * Counts how many living neighbours the cell has
   * @param  int f             cell row
   * @param  int c             cell column
   * @return     How many living neighbours the cell has
   */
  public int livingNeighbours(int f, int c){
    int cont=0;
    switch(border){
      case "Null":
        if(f==0){
          if(c==0){
            cont=cells[f][c+1]+cells[f+1][c]+cells[f+1][c+1];
          }
          else{
            if(c==nDim-1){
              cont=cells[f][c-1]+cells[f+1][c-1]+cells[f+1][c] ;
            }
            else{
              cont=cells[f+1][c-1]+cells[f+1][c]+cells[f+1][c+1]+cells[f][c-1]+cells[f][c+1];
            }
          }
        }else{
          if(f==nDim-1){
            if(c==0){
              cont=cells[f-1][c]+cells[f-1][c+1]+cells[f][c+1];
            }
            else{
              if(c==nDim-1){
                cont=cells[f-1][c-1]+cells[f-1][c]+cells[f][c-1];
              }
              else{
                cont=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1];
              }
            }
          }
          else{
            if(c==0){
              cont=cells[f-1][c+1]+cells[f][c+1]+cells[f+1][c+1]+cells[f-1][c]+cells[f+1][c];
            }
            else{
              if(c==nDim-1){
                cont=cells[f-1][c-1]+cells[f][c-1]+cells[f+1][c-1]+cells[f-1][c]+cells[f+1][c];
              }
              else{
                cont=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][c+1];
              }
            }
          }
        }
      break;

      case "Periodic":
        if(f==0){
          if(c==0){
            cont+=cells[nDim-1][nDim-1]+cells[nDim-1][c]+cells[nDim-1][c+1]+cells[f][nDim-1]+cells[f][c+1]+cells[f+1][nDim-1]+cells[f+1][c] +cells[f+1][c+1];
          }
          else{
            if(c==nDim-1){
              cont+=cells[nDim-1][c-1]+cells[nDim-1][c]+cells[nDim-1][0]+cells[f][c-1]+cells[f][0]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][0];
            }
            else{
              cont+=cells[nDim-1][c-1]+cells[nDim-1][c]+cells[nDim-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][c+1];
            }
          }
        }
        else{
          if(f==nDim-1){
            if(c==0){
              cont+=cells[f-1][nDim-1]+cells[nDim-1][c]+cells[nDim-1][c+1]+cells[f][nDim-1]+cells[f][c+1]+cells[0][nDim-1]+cells[0][c] +cells[0][c+1];
            }
            else{
              if(c==nDim-1){
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][0]+cells[f][c-1]+cells[f][0]+cells[0][c-1]+cells[0][c] +cells[0][0];
              }
              else{
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[0][c-1]+cells[0][c] +cells[0][c+1];
              }
            }
          }
          else{
            if(c==0){
              cont+=cells[f-1][nDim-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][nDim-1]+cells[f][c+1]+cells[f+1][nDim-1]+cells[f+1][c] +cells[f+1][c+1];
            }
            else{
              if(c==nDim-1){
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][0]+cells[f][c-1]+cells[f][0]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][0];
              }
              else{
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][c+1];
              }
            }
          }
        }
      break;

      default: break;
    }
    return cont;
  }

  /*
   * Computes the CA
   */
  public void caComputation(){
    nextGen();
    if(thread==0){ //Thread 0 makes the copy
        for(int i=0; i<nDim; ++i){
            for(int j=0; j<nDim; ++j){
                cells[i][j]=sucessor[i][j];
            }
        }
    }
  }
}
