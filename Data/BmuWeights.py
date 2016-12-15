import numpy as np
import mlpyPCA
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches


bmu_weights = np.loadtxt('BmuWeights.csv', delimiter=',')
x, y = bmu_weights[:, :11], bmu_weights[:, 11].astype(np.int) # x: (observations x attributes) matrix, y: classes (1: b011212563001, 2: 100, 3: 21670, 4: 23355, 5: admin)

pca = mlpyPCA.PCA() # new PCA instance
pca.learn(x) # learn from data
bmu_pca = pca.transform(x, k=2) # embed x into the k=2 dimensional subspace

plt.set_cmap(plt.cm.Paired)
fig1 = plt.figure(1)
title = plt.title("BMU - PCA on Telco Dataset")
colors = ['#660000', '#336600', '#FF00FF', '#3333FF', '#FF0000'] #we'll be cycling through these colors
plt.scatter(bmu_pca[:, 0], bmu_pca[:, 1], color=np.array(colors)[y-1])

classes = ['SBC 1','SBC 2','SBC 3', 'SBC 4', 'SBC 5']
class_colours = ['#660000', '#336600', '#FF00FF', '#3333FF', '#FF0000']
recs = []
for i in range(0,len(class_colours)):
    recs.append(mpatches.Rectangle((0,0),1,1,fc=class_colours[i]))
plt.legend(recs,classes,loc=4)

labx = plt.xlabel("Component A")
laby = plt.ylabel("Component B")
plt.show()
