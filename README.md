## Project File Overview
All Java files can be found in `src\main\java\ec\app`, and all parameter files are stored in `src\main\resources\ec\app`. There are 3 main applications in this project
- `gaestimate`: Using Genetic Algorithm in estimating characteristic.
- `gaevolve`: Using Genetic Algorithm in evolving S-box positions.
- `gpevolve`: Using Genetic Programming in evolving S-box posistions.

Datasets of plaintext/ciphertext pairs are stored in `data` folder

Results of all experiment can be found in the `results` folder, with all result files organised in experiment type (`ga`,`gp` or `estimate`), Linear (`lc`) or Differential (`dc`) Cryptanalysis, and the number of rounds (e.g. `3-rounds`, `4-rounds`, ...).

Finally, the `execution.py` can be used to perform multiple experiments, storing and organising files to the `results` folder.

## Building ECJ with Maven

The easiest way to build ECJ is to run the Maven `package` target:

```
mvn clean package
```

This will build ECJ and run its unit test suite, after automatically installing 
ECJ's dependencies into your local Maven repository, downloading packages 
from Maven's central repository as needed.

## Running ECJ

You will now find a compiled jar at `target/ecj-xx.jar` (where `xx` is the 
current version number).  Various runtime dependency packages will also be 
placed in the `target/dependency` directory.

Take ECJ for a test drive by running one if its example apps (Genetic Algorithm evolving 4-round Linear approximation):

```
java -jar target/ecj-27.jar -from app/gaevolve/lcevolve.params  
```

Which will results in the following, if setup correctly:

```
| ECJ
| An evolutionary computation system (version 27)
| By Sean Luke
| Contributors: E. Scott, L. Panait, G. Balan, S. Paus, Z. Skolicki,
|               R. Kicinger, E. Popovici, K. Sullivan, J. Harrison, J. Bassett,
|               R. Hubley, A. Desai, A. Chircop, J. Compton, W. Haddon,
|               S. Donnelly, B. Jamil, J. Zelibor, E. Kangas, F. Abidi,
|               H. Mooers, J. O'Beirne, L. Manzoni, K. Talukder, S. McKay,
|               J. McDermott, J. Zou, A. Rutherford, D. Freelan, E. Wei,
|               S. Rajendran, A. Dhawan, B. Brumbac, J. Hilty, A. Kabir
| URL: http://cs.gmu.edu/~eclab/projects/ecj/
| Mail: ecj-help@cs.gmu.edu
|       (better: join ECJ-INTEREST at URL above)
| Date: July 1, 2017
| Current Java: 15.0.1 / Java HotSpot(TM) 64-Bit Server VM-15.0.1+9-18
| Required Minimum Java: 1.5


Threads:  breed/1 eval/1
Seed: 1
Job: 0
Setting up
Reading from: C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/lc-4.txt
Initializing Generation 0
Subpop 0 best fitness of generation Fitness: 0.006591796875
Generation 1    Evaluations So Far 200
Subpop 0 best fitness of generation Fitness: 0.00439453125
Generation 2    Evaluations So Far 400
Subpop 0 best fitness of generation Fitness: 0.00439453125
Generation 3    Evaluations So Far 600
Subpop 0 best fitness of generation Fitness: 0.0098876953125
Generation 4    Evaluations So Far 800
Subpop 0 best fitness of generation Fitness: 0.007415771484375
Generation 5    Evaluations So Far 1000
Subpop 0 best fitness of generation Fitness: 0.007415771484375
Generation 6    Evaluations So Far 1200
Subpop 0 best fitness of generation Fitness: 0.01483154296875
Generation 7    Evaluations So Far 1400
Subpop 0 best fitness of generation Fitness: 0.0087890625
Generation 8    Evaluations So Far 1600
Subpop 0 best fitness of generation Fitness: 0.0087890625
Generation 9    Evaluations So Far 1800
Subpop 0 best fitness of generation Fitness: 0.007415771484375
Generation 10   Evaluations So Far 2000
Subpop 0 best fitness of generation Fitness: 0.01483154296875
Generation 11   Evaluations So Far 2200
Subpop 0 best fitness of generation Fitness: 0.0098876953125
Generation 12   Evaluations So Far 2400
Subpop 0 best fitness of generation Fitness: 0.007415771484375
Generation 13   Evaluations So Far 2600
Subpop 0 best fitness of generation Fitness: 0.006591796875
Generation 14   Evaluations So Far 2800
Subpop 0 best fitness of generation Fitness: 0.0111236572265625
Generation 15   Evaluations So Far 3000
Subpop 0 best fitness of generation Fitness: 0.01483154296875
Generation 16   Evaluations So Far 3200
Subpop 0 best fitness of generation Fitness: 0.01483154296875
Generation 17   Evaluations So Far 3400
Subpop 0 best fitness of generation Fitness: 0.019775390625
Individual 178 of subpopulation 0 has an ideal fitness.
Total Evaluations 3600
Subpop 0 best fitness of run: Fitness: 0.019775390625
Null count: 3021


Unaccessed Parameters
===================== (Ignore parent.x references)
```

The full results can also be found in `out.stat`.

## Control Parameters
Users can fiddle around with some parameters (just append the parameters to the end of the running command e.g `java -jar target/ecj-27.jar -from app/gaevolve/lcevolve.params -p seed.0=1234`):
- Population size: `-p pop.subpop.0.size=2048`
- Generations: `-p generations=50`
- Seed: `-p seed.0=1234`
- Mutation: `-p pop.subpop.0.species.mutation-prob=0.2` (mutation ranging from 0 to 1)
- Elitism: `-p breed.elite-fraction.0=0.2` (elitism raning from 0 to 1) 
- Number of Approximation Round: `-p pop.subpop.0.species.genome-size=12` (12 for 3-round problem, 16 for 4-round problem, 20 for 5-round problem)
- To switch to Differential Cryptanalysis, run `java -jar target/ecj-27.jar -from app/gaevolve/dcevolve.params` instead 

