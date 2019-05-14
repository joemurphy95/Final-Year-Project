package fyp;

/*
 * Joseph Murphy, 2019
 */

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class ElementaryCASimulator implements Runnable{
  Random random = new Random();
  static int cores = Runtime.getRuntime().availableProcessors();
  static CyclicBarrier cb = new CyclicBarrier(cores);

  static int[][] cells; //(nGen+1)x(nCells)
  int[] sucessor; //next generation
  static int numGen, numCells;
  static int[] ruleBit = new int[8];
  int upperLimit, lowerLimit, gen, cel;

  /*
   * Constructor
   * 	@param  int ulimit        upper limit (upper limit to generations)
   * 	@param  int llimit        lower limit (lower limit to generations)
   */
  public ElementaryCASimulator(int ulimit, int llimit){
    this.upperLimit=ulimit;
    this.lowerLimit=llimit;
    this.sucessor = new int[numCells];
  }

  public ElementaryCASimulator(){}

  public void run(){
    for(gen=0; gen<numGen; gen++){ caComputation();}
    try{Thread.sleep(2); }catch(InterruptedException e){}
  }
  /*
   * Initializes the cellular automaton
   * 	@param int g number of generations
   * 	@param int c number of cells
   * 	@param int r rule
   */
  public void inizialize(int g, int c, int r){
    numGen = g;
    numCells = c;
    toBinary(r);
    cells = new int[numGen+1][numCells];
    cells[0][numCells/2] = 1; //initializes the first generation
  }

  /*
   * Returns the cellular automaton (i.e. array of cells)
   */
  public int[][] get_cells(){
    return cells;
  }

  /*
   * Transforms a decimal number to binary
   */
  public void toBinary(int rule) {
    final boolean[] ret = new boolean[8];

    for (int i = 0; i < 8; i++){
      ret[8 - 1 - i] = (1 << i & rule) != 0;
    }

    for (int i = 0; i < 8; i++){
      if(ret[i]==true){ ruleBit[i]=1;}
      else{ ruleBit[i]=0;}
    }
  }

  /*
   * Computes the next generation
   */
  public void nextGen(){
    int zero=0;
    for(int i=lowerLimit; i<upperLimit; i++){
      if(i==0){
        if(zero==1&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[0];}
          else{if(zero==1&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[1];}
            else{if(zero==1&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[2];}
              else{if(zero==1&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[3];}
                else{if(zero==0&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[4];}
                  else{if(zero==0&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[5];}
                    else{if(zero==0&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[6];}
                      else{if(zero==0&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[7];}
                    }
                  }
                }
              }
            }
          }
        }
      }else{
        if(i==(numCells-1)){
          if(cells[gen][i-1]==1&&cells[gen][i]==1&&zero==1){sucessor[i]=ruleBit[0];}
            else{if(cells[gen][i-1]==1&&cells[gen][i]==1&&zero==0){sucessor[i]=ruleBit[1];}
              else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&zero==1){sucessor[i]=ruleBit[2];}
                else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&zero==0){sucessor[i]=ruleBit[3];}
                  else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&zero==1){sucessor[i]=ruleBit[4];}
                    else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&zero==0){sucessor[i]=ruleBit[5];}
                      else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&zero==1){sucessor[i]=ruleBit[6];}
                        else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&zero==0){sucessor[i]=ruleBit[7];}
                      }
                    }
                  }
                }
              }
            }
          }
        }else{
          if(cells[gen][i-1]==1&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[0];}
            else{if(cells[gen][i-1]==1&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[1];}
              else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[2];}
                else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[3];}
                  else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[4];}
                    else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[5];}
                      else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[6];}
                        else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[7];}
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  /*
   * Computes the cellular automaton
   */
  public void caComputation(){
    nextGen();
    for(int i=lowerLimit; i<upperLimit ;i++){
      cells[gen+1][i]=sucessor[i];
    }
    try{
      cb.await();
    }catch(Exception e){}
  }

}
