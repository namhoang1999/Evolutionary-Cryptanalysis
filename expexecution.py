import subprocess
from shutil import copy
import os
import math

seeds = list(range(30))
mut = [0.05,0.1,0.15,0.20]   # Mutation
pop = [[64,128,256,512],[128,256,512,1024],[256,512,1024,2048]]     # Population size
elt = [0.01,0.05,0.10,0.15]   # Elitism

src_file = "out.stat"
src_folder = "C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj"

###########################################################################################################
# SETTINGS TO SET BEFORE RUN
###########################################################################################################

# Mutation or Elitism
stg = elt
# File to run
exe_file = "app/gpevolve/dcevolve.params"
# number of rounds (approximation rounds)

for i,rounds in enumerate([3,4,5]):
    des_folder = "C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/gp/dc/{}-rounds/elt".format(rounds)
    
###########################################################################################################
###########################################################################################################
###########################################################################################################
    
    for p in pop[i]: 
        generations = min(int(math.pow(16,rounds)/p),512)
        for s in stg:
            for seed in seeds:
                des_file = "out-{}-{}-{}-{}.stat".format(p,generations-1,str(int(s*100)),seed)
                
                if not os.path.isfile(des_folder + "/" + des_file): # if file not exist yet
                    """param = ["seed.0={}".format(seed),
                             "pop.subpop.0.size={}".format(p),
                             "generations={}".format(generations),
                             "pop.subpop.0.species.mutation-prob={}".format(0.05),
                             "breed.elite-fraction.0={}".format(s),
                             "pop.subpop.0.species.genome-size={}".format(rounds*4)
                             ]
                    """
                    param = ["seed.0={}".format(seed),
                             "pop.subpop.0.size={}".format(p),
                             "generations={}".format(generations),
                             "breed.elite-fraction.0={}".format(s),
                             "pop.subpop.0.species.pipe.source.0.prob={}".format(1-mut[0]),
                             "pop.subpop.0.species.pipe.source.1.prob={}".format(mut[0]),
                             "eval.problem.x={}".format(rounds)
                             ]
                    
                    # Execute command
                    os.system("java -jar target/ecj-27.jar -from {} -p {}".format(exe_file," -p ".join(param)))
                    
                    # Copy file to result folder
                    copy(src_folder + "/" + src_file, des_folder + "/" + des_file)